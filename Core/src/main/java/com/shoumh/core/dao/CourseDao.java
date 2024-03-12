package com.shoumh.core.dao;

import com.shoumh.core.common.ChoiceStatus;
import com.shoumh.core.common.CourseStatus;
import com.shoumh.core.mapper.CourseMapper;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.CourseCapacity;
import com.shoumh.core.pojo.Student;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDao {

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 根据 year 和 semester 来选择课程
     */
    public List<Course> selectByYearAndSemester(Integer year, Integer semester, Integer start, Integer pagesize) {
//        Course course = CourseTemplate.courseWithYearAndSemester(year, semester);
        Course course = Course.builder().year(year).semester(semester).build();
        return courseMapper.selectAllSelective(null, course, null, start, pagesize);
    }

    /**
     * 获得已经选择的课
     * @param student 要求 stuId 不为空，若为空则返回 null
     */
    public List<Course> selectChosen(@NotNull Student student, Course course, CourseStatus status,
                                     Integer start, Integer pagesize) {
        if (student.getStuId() == null) return null;
//        Student stu = StudentTemplate.studentWithId(student.getStuId());
        return courseMapper.selectAllSelective(student, course, status==null?null:status.toString(), start, pagesize);
    }

    /**
     * 获得已经选择的课
     * @param student 要求 stuId 不为空，若为空则返回 null
     */
    public Boolean hasChosen(@NotNull Student student, Course course, CourseStatus status) {
        if (student.getStuId() == null) return null;
        return courseMapper.hasAllSelective(student, course, status==null?null:status.toString());
    }


    /**
     * 获得所有未选择的课
     * @param student 要求 stuId 不为空，否则返回 null
     */
    public List<Course> selectUnchosen(@NotNull Student student, @NotNull Course course,
                                       Integer start, Integer pagesize) {
        return courseMapper.selectUnchosenSelective(student, course, start, pagesize);
    }

    /**
     * 底层接口
     */
    public List<Course> select(Student student, Course course, CourseStatus status, Integer start, Integer pagesize) {
        return courseMapper.selectAllSelective(student, course, status==null?null:status.toString(), start, pagesize);
    }

    /**
     * 选择该课程的所有先继课
     * @param course 要求其中 courseId 不为空，并且只使用该字段
     */
    public List<Course> selectPredecessor(Course course) {
        return courseMapper.selectPredecessor(course);
    }

    /**
     * 选课接口，不检查数据安全性；只实现往数据库中添加选课记录这一个功能
     * @param course 要求 courseId 不为空，并且只使用 courseId 这一个字段
     */
    public void choose(@NotNull String stuId, Course course) {
        courseMapper.choose(stuId, course);
    }

    /**
     * 获取课程的容量
     * @param course 要求 courseId 不为空，并且只使用 courseId 这一个字段
     */
    public CourseCapacity getCapacity(Course course) {
        return courseMapper.selectCapacity(course);
    }

    /**
     * 记录选课表单单条选课信息及状态
     */
    public void logChoiceStatus(@NotNull String uuid, @NotNull String stuId, @NotNull String courseId, ChoiceStatus status) {
        courseMapper.insertChoiceLog(uuid, stuId, courseId, status);
    }

    /**
     * 更新选课表单单条选课状态
     */
    public void updateChoiceStatus(@NotNull String uuid, @NotNull String stuId, @NotNull String courseId, ChoiceStatus status) {
        courseMapper.updateChoiceLog(uuid, stuId, courseId, status);
    }

    /**
     * 记录选课表单状态
     */
    public void logChoiceSheetStatus(@NotNull String uuid, ChoiceStatus status) {
        courseMapper.insertChoiceSheetLog(uuid, status);
    }

    /**
     * 更新选课表单状态
     */
    public void updateChoiceSheetStatus(@NotNull String uuid, @NotNull ChoiceStatus status) {
        courseMapper.updateChoiceSheetLog(uuid, status);
    }

    /**
     * 获得表单状态 uuid
     */
    public ChoiceStatus selectChoiceSheetStatus(@NotNull String uuid) {
        return courseMapper.selectChoiceStatus(uuid);
    }
}