package com.shoumh.core.service;

import com.shoumh.core.common.course.ChoiceStatus;
import com.shoumh.core.common.course.CourseStatus;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.CourseCapacity;
import com.shoumh.core.pojo.CourseSheet;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CourseService {
    /**
     * 获得当期的公共课
     */
    public List<Course> getCurrentPublic(Integer start, Integer pagesize);

    /**
     * 获得公共课
     */
    public List<Course> getPublic(Integer year, Integer semester, Integer start, Integer pagesize);


    /**
     * 获得学生已选择的当期公共课
     */
    public List<Course> getCurrentPublicChosenByStudent(@NotNull String stuId, CourseStatus status, Integer start, Integer pagesize);

    /**
     * 获得学生已选择的公共课
     */
    public List<Course> getPublicChosenByStudent(@NotNull String stuId, CourseStatus status, Integer year, Integer semester, Integer start, Integer pagesize);


    /**
     * 获得学生未选择的当期公共课
     */
    public List<Course> getCurrentPublicUnchosenByStudent(@NotNull String stuId, Integer start, Integer pagesize);

    /**
     * 获得学生未选择的公共课
     */
    public List<Course> getPublicUnchosenByStudent(@NotNull String stuId, Integer year, Integer semester, Integer start, Integer pagesize);


    /**
     * 获得当期专业课
     */
    public List<Course> getCurrentMajor(@NotNull Integer major, Integer start, Integer pagesize);

    /**
     * 获得专业课
     */
    public List<Course> getMajor(@NotNull Integer major, Integer year, Integer semester, Integer start, Integer pagesize);


    /**
     * 获得当期学生已选择专业课
     */
    public List<Course> getCurrentMajorChosenByStudent(@NotNull String stuId, CourseStatus status, Integer start, Integer pagesize);

    /**
     * 获得学生已选择专业课
     */
    public List<Course> getMajorChosenByStudent(@NotNull String stuId, CourseStatus status, Integer year, Integer semester, Integer start, Integer pagesize);


    /**
     * 获得学生未选择的当期公共课
     */
    public List<Course> getCurrentMajorUnchosenByStudent(@NotNull String stuId, Integer start, Integer pagesize);

    /**
     * 获得学生未选择的专业课
     */
    public List<Course> getMajorUnchosenByStudent(@NotNull String stuId, Integer year, Integer semester, Integer start, Integer pagesize);

    /**
     * 获得课程容量
     */
    public CourseCapacity getCapacity(@NotNull String courseId);


    /**
     * 学生选课
     * @param sheet CourseSheet，选课相关内容
     * @return 返回选课表代码，用于查询选课进度及结果
     */
    public String chooseCourse(CourseSheet sheet);

    /**
     * 获取选课表单状态
     * @param uuid 选课表单 uuid
     */
    public ChoiceStatus getSheetStatus(@NotNull String uuid);

}
