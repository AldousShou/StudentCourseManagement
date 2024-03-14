package com.shoumh.core.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoumh.core.common.course.ChoiceStatus;
import com.shoumh.core.common.course.CourseStatus;
import com.shoumh.core.common.SystemConstant;
import com.shoumh.core.dao.CourseDao;
import com.shoumh.core.dao.RedisUtil;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.CourseCapacity;
import com.shoumh.core.pojo.CourseSheet;
import com.shoumh.core.pojo.Student;
import com.shoumh.core.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Integer YEAR = SystemConstant.YEAR;
    private static final Integer SEMESTER = SystemConstant.SEMESTER;

    private final ReentrantLock cacheReadyLock = new ReentrantLock();
    private final ReentrantLock pubCourseReadyLock = new ReentrantLock();
    private final String cacheReadyKey = "course:cached";

    private final Gson gson = new Gson();
    private final Type LIST_COURSE = new TypeToken<List<Course>>(){}.getType();

    @Override
    public List<Course> getCurrentPublic(Integer start, Integer pagesize) {
        return getPublic(YEAR, SEMESTER, start, pagesize);
    }

    @Override
    public List<Course> getPublic(Integer year, Integer semester, Integer start, Integer pagesize) {
        String yearStr = year != null? year.toString(): "null";
        String semStr = semester != null? semester.toString(): "null";
        String startStr = start != null? start.toString(): "null";
        String pagesizeString = pagesize != null ? pagesize.toString(): "null";

        // 查看是否有对应分页的内容，如果有则直接返回
        String pageKey = redisUtil.concatKeys("pub_course_key", yearStr, semStr, "page", startStr, pagesizeString);
        pubCourseReadyLock.lock();
        if (redisUtil.hasKey(pageKey)) {
            pubCourseReadyLock.unlock();
            return gson.fromJson(redisUtil.get(pageKey), LIST_COURSE);
        } else {
            List<Course> coursesPage = courseDao.selectByYearAndSemester(year, semester, start, pagesize);
            // 写 全部
            redisUtil.set(pageKey, gson.toJson(coursesPage));
            pubCourseReadyLock.unlock();
            return coursesPage;
        }
    }

    @Override
    public List<Course> getCurrentPublicChosenByStudent(@NotNull String stuId, CourseStatus status,
                                                        Integer start, Integer pagesize) {
        return getPublicChosenByStudent(stuId, status, YEAR, SEMESTER, start, pagesize);
    }

    @Override
    public List<Course> getPublicChosenByStudent(@NotNull String stuId, CourseStatus status,
                                                 Integer year, Integer semester,
                                                 Integer start, Integer pagesize) {
        Student student = new Student();
        student.setStuId(stuId);
        Course course = new Course();
        course.setYear(year);
        course.setSemester(semester);
        course.setHasMajorDemand(0);
        return courseDao.selectChosen(student, course, status, start, pagesize);
    }

    @Override
    public List<Course> getCurrentPublicUnchosenByStudent(@NotNull String stuId, Integer start, Integer pagesize) {
        return getPublicUnchosenByStudent(stuId, YEAR, SEMESTER, start, pagesize);
    }

    @Override
    public List<Course> getPublicUnchosenByStudent(@NotNull String stuId, Integer year, Integer semester, Integer start, Integer pagesize) {
        Student student = new Student();
        student.setStuId(stuId);
        Course course = new Course();
        course.setYear(year);
        course.setSemester(semester);
        course.setHasMajorDemand(0);
        return courseDao.selectUnchosen(student, course, start, pagesize);
    }

    @Override
    public List<Course> getCurrentMajor(@NotNull Integer major, Integer start, Integer pagesize) {
        return getMajor(major, YEAR, SEMESTER, start, pagesize);
    }

    @Override
    public List<Course> getMajor(@NotNull Integer major, Integer year, Integer semester, Integer start, Integer pagesize) {
        String majorStr = major.toString();
        String yearStr = year != null? year.toString(): "null";
        String semStr = semester != null? semester.toString(): "null";
        String startStr = start != null? start.toString(): "null";
        String pagesizeString = pagesize != null ? pagesize.toString(): "null";

        Student student = new Student();
        student.setMajor(major);
        Course course = new Course();
        course.setYear(year);
        course.setSemester(semester);

        // 查看是否有对应分页的内容，如果有则直接返回
        String pageKey = redisUtil.concatKeys("maj_course_key", yearStr, semStr, majorStr, "page", startStr, pagesizeString);
        pubCourseReadyLock.lock();
        if (redisUtil.hasKey(pageKey)){
            pubCourseReadyLock.unlock();
            return gson.fromJson(redisUtil.get(pageKey), LIST_COURSE);
        } else {
            List<Course> coursesPage = courseDao.select(student, course, null, start, pagesize);
            // 写 全部
            redisUtil.set(pageKey, gson.toJson(coursesPage));
            pubCourseReadyLock.unlock();
            return coursesPage;
        }
    }

    @Override
    public List<Course> getCurrentMajorChosenByStudent(@NotNull String stuId, CourseStatus status, Integer start, Integer pagesize) {
        return getMajorChosenByStudent(stuId, status, YEAR, SEMESTER, start, pagesize);
    }

    @Override
    public List<Course> getMajorChosenByStudent(@NotNull String stuId, CourseStatus status,
                                                Integer year, Integer semester,
                                                Integer start, Integer pagesize) {
        Student student = Student.builder().stuId(stuId).build();
        Course course = Course.builder().year(year).semester(semester).hasMajorDemand(1).build();

        return courseDao.selectChosen(student, course, status, start, pagesize);
    }

    @Override
    public List<Course> getCurrentMajorUnchosenByStudent(@NotNull String stuId, Integer start, Integer pagesize) {
        return getMajorUnchosenByStudent(stuId, YEAR, SEMESTER, start, pagesize);
    }

    @Override
    public List<Course> getMajorUnchosenByStudent(@NotNull String stuId, Integer year, Integer semester, Integer start, Integer pagesize) {
        Student student = new Student();
        student.setStuId(stuId);
        Course course = new Course();
        course.setYear(year);
        course.setSemester(semester);
        return courseDao.selectUnchosen(student, course, start, pagesize);
    }

    @Override
    public CourseCapacity getCapacity(@NotNull String courseId) {
        String countKey = redisUtil.concatKeys("course", "count", courseId);
        String capacityKey = redisUtil.concatKeys("course", "capacity", courseId);
        if (redisUtil.hasKey(countKey) && redisUtil.hasKey(capacityKey)) {
            Long capacity = Long.valueOf(redisUtil.get(capacityKey));
            Long count = Long.valueOf(redisUtil.get(countKey));
            Long selection = capacity - count;
            return new CourseCapacity(courseId, capacity, selection);
        } else {
            Course course = new Course(); course.setCourseId(courseId);
            CourseCapacity capacity = courseDao.getCapacity(course);
            redisUtil.set(capacityKey, capacity.getCapacity().toString());
            redisUtil.set(countKey, String.valueOf(capacity.getCapacity() - capacity.getSelection()));
            return capacity;
        }
    }


    @Override
    public String chooseCourse(CourseSheet sheet) {
        String uuid = String.valueOf(UUID.randomUUID());
        sheet.setUuid(uuid);

        try {
            rabbitTemplate.convertAndSend(SystemConstant.CHOICE_BEFORE_EXCHANGE, "", sheet);
        } catch (AmqpException exception) {
            log.info("unable to send to rabbitmq, record uuid: {}", sheet.getUuid());

            new Thread(()->{
                log.info("new thread to handle rabbitmq send err not implemented");
            }).start();
        }

        return uuid;
    }

    @Override
    public ChoiceStatus getSheetStatus(@NotNull String uuid) {
        return courseDao.selectChoiceSheetStatus(uuid);
    }

}
