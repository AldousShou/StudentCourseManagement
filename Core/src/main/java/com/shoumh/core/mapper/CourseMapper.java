package com.shoumh.core.mapper;

import com.shoumh.core.common.course.ChoiceStatus;
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

    /**
     * 根据 courseId 来删除 course 表中的课程
     * **请勿在 core 中来删除课程**
     * 如果需要更变课程，请在其他模块中进行该任务
     */
    @Deprecated
    void deleteById(@NotNull String courseId);

    /**
     * 根据 courseId 来选择课程
     * 请不要使用这个接口，使用 selectAllSelective 来获得更多自定义化设置
     */
    Course selectById(@NotNull String courseId);

    /**
     * 仅筛选出 student, course, status中不为空的部分字段的信息
     * @param student 学生信息，所有都可为空
     * @param course 课程信息，所有都可为空
     * @param status 课程状态信息，指学生选择该课程后，是否已经结束课程或已经选上，值只有 ended, normal; 可以为空
     */
    List<Course> selectAllSelective(Student student, Course course, String status, Integer start, Integer pagesize);

    /**
     * 检索是否含有 包含指定数据 的记录
     */
    Boolean hasAllSelective(Student student, Course course, String status);

    /**
     * 根据 student.stuId 来筛选出结果，选出该学生尚未选修的课
     * @param student 要求 stuId 不为空
     * @param course 可以为空，根据条件筛选课程
     */
    List<Course> selectUnchosenSelective(@NotNull Student student, Course course, Integer start, Integer pagesize);

    /**
     * 找到课程的先导课，如果没有返回 null
     * @param course 要求 id 不为空, 并且只用到 courseId 一个值
     * @return 仅返回 courseId，course 的其他内容为空
     */
    List<Course> selectPredecessor(@NotNull Course course);

    /**
     * 学生选课核心接口，仅往数据库中添加选课记录
     * **注意！不检查选课记录的合法性，仅添加记录**
     */
    void choose(@NotNull String stuId, @NotNull Course course);

    /**
     * 获取课程余量等数据
     * @param course 课程信息
     * @return 课程剩余数量
     */
    CourseCapacity selectCapacity(@NotNull Course course);

    /**
     * 添加选课表单中单条选课记录状态
     */
    void insertChoiceLog(@NotNull String uuid, @NotNull String stuId, @NotNull String courseId, ChoiceStatus status);

    /**
     * 更新选课表单中单条选课记录状态
     */
    void updateChoiceLog(@NotNull String uuid, @NotNull String stuId, @NotNull String courseId, @NotNull ChoiceStatus status);

    /**
     * 添加选课表单状态
     */
    void insertChoiceSheetLog(@NotNull String uuid, ChoiceStatus status);

    /**
     * 添加选课表单状态
     */
    void updateChoiceSheetLog(@NotNull String uuid, @NotNull ChoiceStatus status);

    /**
     * 获取表单状态
     */
    ChoiceStatus selectChoiceStatus(@NotNull String uuid);

}
