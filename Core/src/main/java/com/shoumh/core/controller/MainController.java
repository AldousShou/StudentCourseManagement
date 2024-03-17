package com.shoumh.core.controller;

import com.shoumh.core.annotation.AutoLog;
import com.shoumh.core.annotation.RequestLimit;
import com.shoumh.core.annotation.ServletTimer;
import com.shoumh.core.common.course.CourseStatus;
import com.shoumh.core.common.SystemConstant;
import com.shoumh.core.pojo.*;
import com.shoumh.core.service.CourseService;
import com.shoumh.core.service.StudentCourseService;
import com.shoumh.core.service.StudentService;
import com.shoumh.core.service.WarmUpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class MainController implements InitializingBean {

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentCourseService studentCourseService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private WarmUpService warmUpService;

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @GetMapping("/ping")
    public Result ping() {
        return Result.success("PONG");
    }

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @GetMapping("/get_stu_info")
    public Result getStuInfo(String stuId) {
        if (stuId == null) return new Result(1, "params not fulfilled");
        Student student = studentService.getStuInfo(stuId);
        return Result.success(student);
    }

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @GetMapping("/exist_user")
    public Result existUser(String stuId) {
        Boolean exist = studentService.existUser(stuId);
        return Result.success(exist);
    }

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @GetMapping("/get_public")
    public Result getCurrentPublic(@Param("start") Integer start, @Param("pagesize") Integer pagesize) {
        List<Course> courses = courseService.getCurrentPublic(start, pagesize);
        return Result.success(courses);
    }

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @GetMapping("/get_public_chosen")
    public Result getPublicChosen(@Param("stuId") String stuId, @Param("status") String status,
                                  @Param("start") Integer start, @Param("pagesize") Integer pagesize) {
        if (stuId == null)
            return new Result(2, "params not fulfilled");

        List<Course> courses;
        if (status != null)
            if (status.equalsIgnoreCase("normal") || status.equalsIgnoreCase("ended")) {
                courses = courseService.getCurrentPublicChosenByStudent(stuId, CourseStatus.valueOf(status.toUpperCase()), start, pagesize);
            } else {
                return new Result(1, "failed to parse status");
            }
        else {
            courses = courseService.getCurrentPublicChosenByStudent(stuId, null, start, pagesize);
        }
        return Result.success(courses);
    }

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @GetMapping("/get_public_unchosen")
    public Result getPublicUnchosen(String stuId, Integer start, Integer pagesize) {
        if (stuId == null) return new Result(2, "params unfulfilled");
        List<Course> courses = courseService.getCurrentPublicUnchosenByStudent(stuId, start, pagesize);
        return Result.success(courses);
    }

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @GetMapping("/get_major")
    public Result getMajor(Integer major, Integer start, Integer pagesize) {
        return Result.success(courseService.getCurrentMajor(major, start, pagesize));
    }

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @GetMapping("/get_major_chosen")
    public Result getMajorChosen(String stuId, String status, Integer start, Integer pagesize) {
        List<Course> courses;
        if (status != null)
            if (status.equalsIgnoreCase("normal") || status.equalsIgnoreCase("ended")) {
                courses = courseService.getCurrentMajorChosenByStudent(stuId, CourseStatus.valueOf(status.toUpperCase()), start, pagesize);
            } else {
                return new Result(1, "failed to parse status");
            }
        else {
            courses = courseService.getCurrentMajorChosenByStudent(stuId, null, start, pagesize);
        }
        return Result.success(courses);
    }

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @GetMapping("/get_major_unchosen")
    public Result getMajorUnchosen(String stuId, Integer start, Integer pagesize) {
        return Result.success(courseService.getCurrentMajorUnchosenByStudent(stuId, start, pagesize));
    }

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @GetMapping("/get_capacity")
    public Result getCapacity(String courseId) {
        if (courseId == null) return new Result(1, "params not fulfilled");
        return Result.success(courseService.getCapacity(courseId));
    }

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @PostMapping("/choose_course")
    public Result chooseCourse(@RequestBody @NotNull CourseSheet sheet) {
        if (sheet.getStuId() == null || sheet.getCourses() == null) {
            return new Result(1, "failed to parse stu id and course info", null);
        } else {
            String uuid = courseService.chooseCourse(sheet);
            return Result.success(uuid);
        }
    }

    @ServletTimer
    @RequestLimit(permitsPerSecond = 1000)
//    @AutoLog(writeToMQ = true, exchangeName = SystemConstant.LOG_EXCHANGE)
    @GetMapping("/get_sheet_status")
    public Result getSheetStatus(@RequestParam("uuid") String uuid) {
        return Result.success(courseService.getSheetStatus(uuid));
    }

    /**
     * 使用 Spring 提供的 InitializingBean 接口实现暖机
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        warmUpService.warmUp();
    }
}
