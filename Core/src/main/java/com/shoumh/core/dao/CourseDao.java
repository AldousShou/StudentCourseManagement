package com.shoumh.core.dao;

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
     * 根据 year 和 semester 来选择所有公共课
     * @param year year
     * @param semester semester
     * @return courses
     */
    public List<Course> selectByYearAndSemester(Integer year, Integer semester, Integer start, Integer pagesize) {
        Course course = CourseTemplate.courseWithYearAndSemester(year, semester);
        List<Course> courses = courseMapper.selectAllSeletive(null, course, null, start, pagesize);
        return courses;
    }

    /**
     * 获得已经选择的课
     * @param student 要求 stuId 不为空，若为空则返回 null
     * @param status
     * @return courses
     */
    public List<Course> selectChosen(@NotNull Student student, Course course, CourseStatus status,
                                     Integer start, Integer pagesize) {
        if (student.getStuId() == null) return null;
        Student stu = StudentTemplate.studentWithId(student.getStuId());
        List<Course> courses = courseMapper.selectAllSeletive(stu, course, status==null?null:status.toString(), start, pagesize);
        return courses;
    }

    /**
     * 获得所有未选择的课
     * @param student 要求 stuId 不为空，否则返回 null
     */
    public List<Course> selectUnchosen(@NotNull Student student, @NotNull Course course,
                                       Integer start, Integer pagesize) {
        List<Course> courses = courseMapper.selectUnchosenSelective(student, course, start, pagesize);
        return courses;
    }

    /**
     * 底层接口
     */
    public List<Course> select(Student student, Course course, CourseStatus status, Integer start, Integer pagesize) {
        return courseMapper.selectAllSeletive(student, course, status==null?null:status.toString(), start, pagesize);
    }

    public void choose(String stuId, Course course) {
        courseMapper.choose(stuId, course);
    }

    public CourseCapacity getCapacity(Course course) {
        return courseMapper.selectCapacity(course);
    }


}

class StudentTemplate {
    private StudentTemplate() {}

    public static Student studentWithId(String stuId) {
        Student student = new Student();
        student.setStuId(stuId);
        return student;
    }
}

class CourseTemplate {
    private CourseTemplate() {}

    public static Course courseWithYearAndSemester(Integer year, Integer semester) {
        Course course = new Course();
        course.setYear(year);
        course.setSemester(semester);
        return course;
    }
}