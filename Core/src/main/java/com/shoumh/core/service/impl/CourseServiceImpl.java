package com.shoumh.core.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoumh.core.common.CourseStatus;
import com.shoumh.core.common.SystemConstant;
import com.shoumh.core.dao.CourseDao;
import com.shoumh.core.dao.RedisUtil;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.CourseCapacity;
import com.shoumh.core.pojo.CourseSheet;
import com.shoumh.core.pojo.Student;
import com.shoumh.core.service.CourseService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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
        Student student = new Student();
        student.setStuId(stuId);
        Course course = new Course();
        course.setYear(year);
        course.setSemester(semester);
        course.setHasMajorDemand(1);

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
            if (redisUtil.hasKey(cacheReadyKey)) cacheReadyLock.unlock();
            else {
                // 获取所有的课程, 并为其创建 redis 索引
                List<Course> courses = courseDao.selectByYearAndSemester(YEAR, SEMESTER, null, null);
                for (Course c: courses) {
                    String redisKey = redisUtil.concatKeys("course", "count", c.getCourseId());
                    CourseCapacity capacity = courseDao.getCapacity(course);
                    long avail = capacity.getCapacity() - capacity.getSelection();
                    redisUtil.set(redisKey, String.valueOf(avail));
                }
                // 设置 cacheready
                redisUtil.set(cacheReadyKey, "OK");
                cacheReadyLock.unlock();
            }

            // 尝试选课
            String courseKey = redisUtil.concatKeys("course", "count", course.getCourseId());
            if (redisUtil.hasKey(courseKey)) {
                // redis 中存在 key
                if (redisUtil.decr(courseKey) >= 0) {
                    // 成功有抢课的资格
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
