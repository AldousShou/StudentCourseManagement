package com.shoumh.core.mapper;

import com.shoumh.core.pojo.Course;
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

    void deleteById(@NotNull String id);

    Course selectById(@NotNull String id);


    /**
     * 找到所有的课程，根据给定的 year 和 semester
     * @param year
     * @param semester
     * @return
     */
    List<Course> selectByYearAndSemester(Integer year, Integer semester, Integer start, Integer pagesize);

    /**
     * 找到所有的课程，根据给定的 year semester 和 student
     * @param year
     * @param semester
     * @param student
     * @return
     */
    List<Course> selectMajorAllByYearSemesterAndStudent(Integer year, Integer semester, Student student);

    /**
     * 找到所有的课程，根据给定的 course
     * @param course
     * @return
     */
    List<Course> selectSelective(Course course);

    /**
     * 找到所有的专业课程，根据给定的 course 和 major
     * @param course
     * @param major
     * @return
     */
    List<Course> selectSelectiveByMajor(Course course, @NotNull Integer major);

    /**
     * 找到所有的学生已修课程，根据给定的 student
     * @param student
     * @return
     */
    List<Course> selectMajorEndedByStudent(Student student);

    /**
     * 找到所有学生已经选择的课程，根据给定的 student
     * @param student
     * @return
     */
    List<Course> selectAllChosenByStudent(Student student);

    /**
     * 找到所有学生未选择的课程，根据给定的 student
     * @param student
     * @return
     */
    List<Course> selectMajorUnchosenByStudent(Student student);

    /**
     * 插入新的课程
     * @param record
     */
    void insert(@NotNull Course record);

    /**
     * 插入新的课程，允许其中部分字段为 null
     * @param record
     */
    void insertSelective(@NotNull Course record);

    /**
     * 更新课程，允许其中部分字段为 null
     * @param record 要求其中 courseId 不为 null
     */
    void updateByIdSelective(@NotNull Course record);

    /**
     * 更新课程
     * @param record 要求其中 courseId 不为 null
     */
    void updateById(@NotNull Course record);

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
    Long selectAvail(@NotNull Course course);

}
