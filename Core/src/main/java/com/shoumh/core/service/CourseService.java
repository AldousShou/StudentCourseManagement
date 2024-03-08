package com.shoumh.core.service;

import com.shoumh.core.common.ChoiceStatus;
import com.shoumh.core.common.CourseStatus;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.CourseCapacity;
import com.shoumh.core.pojo.CourseSheet;
import com.shoumh.core.pojo.Student;
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


    /**
     * 学生选课
     * @param sheet CourseSheet，选课相关内容
     * @return 返回选课表代码，用于查询选课进度及结果
     */
    public String chooseCourse(CourseSheet sheet);

}
