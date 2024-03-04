package com.shoumh.core.controller;

import com.shoumh.core.pojo.*;
import com.shoumh.core.service.CourseService;
import com.shoumh.core.service.StudentCourseService;
import com.shoumh.core.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentCourseService studentCourseService;
    @Autowired
    private CourseService courseService;

    @PostMapping("/get_stu_info")
    public Result getStuInfo(@RequestBody @NotNull Student stu) {
        if (stu.getStuId() == null) {
            return new Result(1, "failed to parse stuId", null);
        } else {
            Student student = studentService.getStuInfo(stu.getStuId());
            return Result.success(student);
        }
    }

    @PostMapping("/exist_user")
    public Result existUser(@RequestBody @NotNull Student stu) {
        if (stu.getStuId() == null) {
            return new Result(1, "failed to parse stuId", null);
        } else {
            Boolean exist = studentService.existUser(stu.getStuId());
            return Result.success(exist);
        }
    }

    @PostMapping("/get_current_course_chosen_by_stu")
    public Result getCurrentCourseByStudent(@RequestBody @NotNull Student stu) {
        if (stu.getStuId() == null) {
            return new Result(1, "failed to parse stuId", null);
        } else {
            List<StudentCourseAll> courses = studentCourseService.getCourseChosenByStudent(stu);
            return Result.success(courses);
        }
    }

    @GetMapping("/get_current_public_course")
    public Result getCurrentCourse(@Param("start") Integer start, @Param("pagesize") Integer pagesize) {
        return Result.success(courseService.getCurrentPublicCourse(start, pagesize));
    }

    @PostMapping("/get_current_major_course_all")
    public Result getCurrentMajorCourseAll(@RequestBody @NotNull Student stu) {
        if (stu.getMajor() == null) {
            return new Result(1, "failed to parse major", null);
        } else {
            List<Course> courses = courseService.getCurrentMajorCourseAll(stu);
            return Result.success(courses);
        }
    }

    @PostMapping("/get_major_course_ended")
    public Result getMajorCourseEnded(@RequestBody @NotNull Student stu) {
        if (stu.getMajor() == null) {
            return new Result(1, "failed to parse major", null);
        } else {
            List<Course> courses = courseService.getMajorCourseEnded(stu);
            return Result.success(courses);
        }
    }

    @PostMapping("/get_major_course_chosen")
    public Result getMajorCourseChosen(@RequestBody @NotNull Student stu) {
        if (stu.getMajor() == null) {
            return new Result(1, "failed to parse major", null);
        } else {
            List<Course> courses = courseService.getMajorCourseChosen(stu);
            return Result.success(courses);
        }
    }

    @PostMapping("/get_major_course_unchosen")
    public Result getMajorCourseUnchosen(@RequestBody @NotNull Student stu) {
        if (stu.getMajor() == null) {
            return new Result(1, "failed to parse major", null);
        } else {
            List<Course> courses = courseService.getMajorCourseUnchosen(stu);
            return Result.success(courses);
        }
    }

    @PostMapping("/choose_course")
    public Result chooseCourse(@RequestBody @NotNull CourseSheet sheet) {
        if (sheet.getStuId() == null || sheet.getCourses() == null) {
            return new Result(1, "failed to parse stu id and course info", null);
        } else {
            List<Course> courses = courseService.chooseCourse(sheet);
            if (courses.isEmpty()) return Result.success();
            else return new Result(2, "cannot choose some of the courses", courses);
        }
    }
}
