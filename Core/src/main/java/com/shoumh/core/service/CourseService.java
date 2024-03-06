package com.shoumh.core.service;

import com.shoumh.core.common.CourseStatus;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.CourseCapacity;
import com.shoumh.core.pojo.CourseSheet;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CourseService {
    public List<Course> getCurrentPublic(Integer start, Integer pagesize);
    public List<Course> getPublic(Integer year, Integer semester, Integer start, Integer pagesize);

    public List<Course> getCurrentPublicChosenByStudent(@NotNull String stuId, CourseStatus status, Integer start, Integer pagesize);
    public List<Course> getPublicChosenByStudent(@NotNull String stuId, CourseStatus status, Integer year, Integer semester, Integer start, Integer pagesize);

    public List<Course> getCurrentPublicUnchosenByStudent(@NotNull String stuId, Integer start, Integer pagesize);
    public List<Course> getPublicUnchosenByStudent(@NotNull String stuId, Integer year, Integer semester, Integer start, Integer pagesize);

    public List<Course> getCurrentMajor(@NotNull Integer major, Integer start, Integer pagesize);
    public List<Course> getMajor(@NotNull Integer major, Integer year, Integer semester, Integer start, Integer pagesize);

    public List<Course> getCurrentMajorChosenByStudent(@NotNull String stuId, CourseStatus status, Integer start, Integer pagesize);
    public List<Course> getMajorChosenByStudent(@NotNull String stuId, CourseStatus status, Integer year, Integer semester, Integer start, Integer pagesize);

    public List<Course> getCurrentMajorUnchosenByStudent(@NotNull String stuId, Integer start, Integer pagesize);
    public List<Course> getMajorUnchosenByStudent(@NotNull String stuId, Integer year, Integer semester, Integer start, Integer pagesize);

    public CourseCapacity getCapacity(@NotNull String courseId);


    public List<Course> chooseCourse(CourseSheet sheet);

}
