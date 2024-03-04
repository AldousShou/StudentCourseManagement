package com.shoumh.core.service;

import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.CourseSheet;
import com.shoumh.core.pojo.Student;
import com.shoumh.core.pojo.StudentCourseAll;

import java.util.List;

public interface CourseService {
    @Deprecated
    public List<Course> getCurrentCourse(Integer start, Integer pagesize);
    @Deprecated
    public List<Course> getCourse(Integer year, Integer semester, Integer start, Integer pagesize);

    public List<Course> getCurrentPublicCourse(Integer start, Integer pagesize);
    public List<Course> getPublicCourse(Integer year, Integer semester, Integer start, Integer pagesize);
    public List<Course> getCurrentMajorCourseAll(Student student);
    public List<Course> getMajorCourseAll(Integer year, Integer semester, Student student);
    public List<Course> getMajorCourseEnded(Student student);
    public List<Course> getMajorCourseChosen(Student student);
    public List<Course> getMajorCourseUnchosen(Student student);

    public List<Course> chooseCourse(CourseSheet sheet);

}
