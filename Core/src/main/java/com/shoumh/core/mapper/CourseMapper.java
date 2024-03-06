package com.shoumh.core.mapper;

import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.CourseCapacity;
import com.shoumh.core.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
* @author siriusshou
* @description 针对表【course_view】的数据库操作Mapper
* @createDate 2024-03-02 17:11:01
* @Entity com.shoumh.core.pojo.CourseViewMapper
*/
@Mapper
public interface CourseMapper {

    void deleteById(@NotNull String courseId);

    Course selectById(@NotNull String courseId);

    /**
     * 仅筛选出 student, course, status中不为空的部分字段的信息
     * @param student 学生信息，所有都可为空
     * @param course 课程信息，所有都可为空
     * @param status 课程状态信息，指学生选择该课程后，是否已经结束课程或已经选上，值只有 ended, normal; 可以为空
     * @return courses
     */
    List<Course> selectAllSeletive(Student student, Course course, String status, Integer start, Integer pagesize);

    /**
     * 根据 student.stuId 来筛选出结果，选出该学生尚未选修的课
     * @param student 要求 stuId 不为空
     * @param course 可以为空，根据条件筛选课程
     * @return courses
     */
    List<Course> selectUnchosenSelective(@NotNull Student student, Course course, Integer start, Integer pagesize);

    /**
     * 学生选课
     * @param stuId
     * @param course
     */
    void choose(@NotNull String stuId, @NotNull Course course);

    /**
     * 获取课程余量
     * @param course 课程信息
     * @return 课程剩余数量
     */
    CourseCapacity selectCapacity(@NotNull Course course);

}