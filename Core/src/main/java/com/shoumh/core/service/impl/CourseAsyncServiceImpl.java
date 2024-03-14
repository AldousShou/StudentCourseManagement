package com.shoumh.core.service.impl;

import com.google.gson.Gson;
import com.shoumh.core.common.course.ChoiceStatus;
import com.shoumh.core.common.course.CourseStatus;
import com.shoumh.core.common.SystemConstant;
import com.shoumh.core.dao.CourseDao;
import com.shoumh.core.dao.RedisUtil;
import com.shoumh.core.dao.StudentDao;
import com.shoumh.core.pojo.*;
import com.shoumh.core.service.CourseAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CourseAsyncServiceImpl implements CourseAsyncService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private Gson gson;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void checkAndSendChoiceSheetLegality(@NotNull CourseSheet sheet) {
        ChoiceSheetResult result = new ChoiceSheetResult();
        result.setStuId(sheet.getStuId());
        result.setUuid(sheet.getUuid());
        ArrayList<ChoiceResult> choiceResults = new ArrayList<>();
//        Student student = StudentTemplate.studentWithId(sheet.getStuId());
        Student student = Student.builder().stuId(sheet.getStuId()).build();

        for (Course course: sheet.getCourses()) {
            ChoiceStatus status = checkChoiceLegality(student, course);
            assert status != null;
            choiceResults.add(new ChoiceResult(course.getCourseId(), status));
        }

        result.setChoiceResults(choiceResults);

        rabbitTemplate.convertAndSend(SystemConstant.CHOICE_CHECKED_EXCHANGE, "", result);
    }

    @Override
    public ChoiceStatus checkChoiceLegality(@NotNull Student student, @NotNull Course course) {
        if (student.getStuId() == null || course.getCourseId() == null) {
            return null;  // 要求 stuId，courseId 不为空
        }

        // 要求 major 不为空
        if (student.getMajor() == null) {
            student = studentDao.getStuInfo(student.getStuId());
        }

        // 预选课
        String courseCountkey = redisUtil.concatKeys("course", "count", course.getCourseId());
        // 查看是否在内存中 **注意：需要先暖机**
        if (!redisUtil.hasKey(courseCountkey)) {
            CourseCapacity capacity = courseDao.getCapacity(course);
            if (capacity == null) {
                log.warn("[CourseAsyncService] getting course '{}' with capacity null", course.getCourseId());
                redisUtil.set(courseCountkey, "0");
            }
            else {
                redisUtil.set(courseCountkey, String.valueOf(capacity.getCapacity() - capacity.getSelection()));
            }
        }
        if (redisUtil.decr(courseCountkey) < 0) {
            redisUtil.incr(courseCountkey);
            return ChoiceStatus.NOT_RUBBED;
        }

        // 从 redis 中读出 course 数据
        String courseKey = redisUtil.concatKeys("course", "info", course.getCourseId());
        if (redisUtil.hasKey(courseKey)) course = gson.fromJson(redisUtil.get(courseKey), Course.class);
        else {
            course = courseDao.select(null, course, null, null, null).get(0);
            redisUtil.setWithExpiration(courseKey, gson.toJson(course), 1, TimeUnit.DAYS);
        }

        // 查看是否已经选择该课程
        if (courseDao.hasChosen(student, course, CourseStatus.NORMAL)) {
            redisUtil.incr(courseCountkey);
            return ChoiceStatus.CHOICE_DUPLICATED;
        }

        // 查看是否符合专业
        if (course.getHasMajorDemand() > 0 && courseDao.select(
                student, course, CourseStatus.ENDED, null, null).isEmpty()) {
            redisUtil.incr(courseCountkey);
            return ChoiceStatus.MAJOR_UNFULFILLED;
        }

        // 查看是否选修先导课
        if (course.getHasPredecessor() > 0) {
            List<Course> predecessorCourses = courseDao.selectPredecessor(course);
            for (Course c: predecessorCourses) {
                if (!courseDao.hasChosen(student, c, CourseStatus.ENDED)) {
                    redisUtil.incr(courseCountkey);
                    return ChoiceStatus.PREDECESSOR_UNCHOSEN;
                }
            }
        }
        return ChoiceStatus.SUCCESS;
    }

    @Override
    public void logSheetStatus(@NotNull CourseSheet sheet) {
        courseDao.logChoiceSheetStatus(sheet.getUuid(), null);
        for (Course course: sheet.getCourses()) {
            courseDao.logChoiceStatus(sheet.getUuid(), sheet.getStuId(), course.getCourseId(), null);
        }
    }

    @Override
    public void updateSheetStatus(@NotNull ChoiceSheetResult sheet) {
        // 更新 sheet 状态
        courseDao.updateChoiceSheetStatus(sheet.getUuid(), ChoiceStatus.SUCCESS);

        // 更新 course 状态
        for (ChoiceResult choiceResult: sheet.getChoiceResults()) {
            courseDao.updateChoiceStatus(sheet.getUuid(), sheet.getStuId(),
                    choiceResult.getCourseId(),
                    choiceResult.getStatus());
        }
    }

    @Override
    public void writeChoiceSheet(@NotNull ChoiceSheetResult sheetResult) {
        for (ChoiceResult choiceResult: sheetResult.getChoiceResults()) {
            if (choiceResult.getStatus().equals(ChoiceStatus.SUCCESS)) {
                courseDao.choose(sheetResult.getStuId(),
//                        CourseTemplate.courseWithCourseId(choiceResult.getCourseId())
                        Course.builder().courseId(choiceResult.getCourseId()).build()
                );
            }
        }
    }

}
