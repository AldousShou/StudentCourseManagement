package com.shoumh.core.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoumh.core.common.SystemConstant;
import com.shoumh.core.dao.CourseDao;
import com.shoumh.core.dao.RedisUtil;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.CourseSheet;
import com.shoumh.core.pojo.Student;
import com.shoumh.core.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private RedisUtil redisUtil;

    private static final Integer YEAR = SystemConstant.YEAR;
    private static final Integer SEMESTER = SystemConstant.SEMESTER;

    private final ReentrantLock cacheReadyLock = new ReentrantLock();
    private final String cacheReadyString = "course:cached";

    private final Gson gson = new Gson();
    private final Type LIST_COURSE = new TypeToken<List<Course>>(){}.getType();

    @Deprecated
    @Override
    public List<Course> getCurrentCourse(Integer start, Integer pagesize) {
        return getCourse(YEAR, SEMESTER, start, pagesize);
    }

    @Deprecated
    @Override
    public List<Course> getCourse(Integer year, Integer semester, Integer start, Integer pagesize) {
        return courseDao.selectByYearAndSemester(year, semester, start, pagesize);
    }

    @Override
    public List<Course> getCurrentPublicCourse(Integer start, Integer pagesize) {
        return getPublicCourse(YEAR, SEMESTER, start, pagesize);
    }

    @Override
    public List<Course> getPublicCourse(Integer year, Integer semester, Integer start, Integer pagesize) {
        if (start == null) start = 0;

        String yearStr = year != null? year.toString(): "null";
        String semStr = semester != null? semester.toString(): "null";
        String startStr = start.toString();
        String pagesizeString = pagesize != null ? pagesize.toString(): "null";

        String pubCourseKey = redisUtil.concatKeys("pub_course_key", yearStr, semStr, startStr, pagesizeString);
        if (redisUtil.hasKey(pubCourseKey)) {
            return gson.fromJson(redisUtil.get(pubCourseKey), LIST_COURSE);
        } else {
            List<Course> courses = courseDao.selectByYearAndSemester(year, semester, start, pagesize);
            redisUtil.setWithExpiration(pubCourseKey, gson.toJson(courses), 1, TimeUnit.HOURS);
            return courses;
        }
    }

    @Override
    public List<Course> getCurrentMajorCourseAll(Student student) {
        return getMajorCourseAll(YEAR, SEMESTER, student);
    }

    @Override
    public List<Course> getMajorCourseAll(Integer year, Integer semester, Student student) {
        return courseDao.selectAll(year, semester, student);
    }

    @Override
    public List<Course> getMajorCourseEnded(Student student) {
        List<Course> courses = courseDao.selectEnded(student);
        ArrayList<Course> res = new ArrayList<>();
        for (Course course: courses) {
            if (course.getHasMajorDemand()) {

            }
        }
        return
    }  // #TODO: 此处存在问题，selectended 为所有的 course，而 service 中则是所有的专业课

    @Override
    public List<Course> getMajorCourseChosen(Student student) {
        return courseDao.selectChosen(student);
    }

    @Override
    public List<Course> getMajorCourseUnchosen(Student student) {
        return courseDao.selectUnchosen(student);
    }

    @Override
    public List<Course> chooseCourse(CourseSheet sheet) {
        String stuId = sheet.getStuId();
        Integer major = sheet.getMajor();  // #TODO: 检查是否符合该专业

        ArrayList<Course> failure = new ArrayList<>();
        for (Course course: sheet.getCourses()) {
            if (course.getCourseId() == null) {
                failure.add(course);
                continue;
            }

            // 查看 redis 中缓存是否有效
            // WARNING: 请先使用 warm-up 将所有课程数据读入 cache
            cacheReadyLock.lock();
            if (redisUtil.hasKey(cacheReadyString)) cacheReadyLock.unlock();
            else {
                // 获取所有的课程
                List<Course> courses = courseDao.selectByYearAndSemester(YEAR, SEMESTER, 0, null);
                for (Course c: courses) {
                    String redisKey = redisUtil.concatKeys("course", "count", c.getCourseId());
                    Long avail = courseDao.getAvail(course);
                    if (avail == null) avail = 0L;
                    redisUtil.set(redisKey, avail.toString());
                }
                // 设置 cacheready
                redisUtil.set(cacheReadyString, "OK");
                cacheReadyLock.unlock();
            }

            // 尝试选课
            String courseKey = redisUtil.concatKeys("course", "count", course.getCourseId());
            if (redisUtil.hasKey(courseKey)) {
                // redis 中存在 key
                if (redisUtil.decr(courseKey) >= 0) {
                    // 成功有抢课的资格
                    // 判断是否合法


                    courseDao.choose(stuId, course);
                } else {
                    // 数量小于 0，未成功选到
                    failure.add(course);
                }
            } else {
                // redis 中不存在 key, 则认为不存在该课程
                failure.add(course);
            }
        }

        return failure;
    }

}
