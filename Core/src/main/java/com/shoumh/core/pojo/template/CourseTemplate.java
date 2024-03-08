package com.shoumh.core.pojo.template;

import com.shoumh.core.pojo.Course;
import org.jetbrains.annotations.NotNull;

public class CourseTemplate {
    private CourseTemplate() {}

    public static Course courseWithYearAndSemester(Integer year, Integer semester) {
        Course course = new Course();
        course.setYear(year);
        course.setSemester(semester);
        return course;
    }

    public static Course courseWithCourseId(@NotNull String courseId) {
        Course course = new Course();
        course.setCourseId(courseId);
        return course;
    }
}