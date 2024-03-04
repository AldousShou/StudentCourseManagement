package com.shoumh.core.dao;

import com.shoumh.core.mapper.CourseMapper;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.Student;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDao {

    @Autowired
    private CourseMapper courseMapper;

    public List<Course> selectByYearAndSemester(Integer year, Integer semester, @NotNull Integer start, Integer pagesize) {
        return courseMapper.selectByYearAndSemester(year, semester, start, pagesize);
    }

    public List<Course> selectAll(Integer year, Integer semester, Student student) {
        return courseMapper.selectMajorAllByYearSemesterAndStudent(year, semester, student);
    }

    public List<Course> selectEnded(Student student) {
        return courseMapper.selectMajorEndedByStudent(student);
    }

    public List<Course> selectChosen(Student student) {
        return courseMapper.selectAllChosenByStudent(student);
    }

    public List<Course> selectUnchosen(Student student) {
        return courseMapper.selectMajorUnchosenByStudent(student);
    }

    public void choose(String stuId, Course course) {
        courseMapper.choose(stuId, course);
    }

    public Long getAvail(Course course) {
        return courseMapper.selectAvail(course);
    }


}
