package com.shoumh.core.dao;

import com.shoumh.core.mapper.StudentCourseAllMapper;
import com.shoumh.core.pojo.Student;
import com.shoumh.core.pojo.StudentCourseAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentCourseDao {

    @Autowired
    private StudentCourseAllMapper studentCourseAllMapper;

    public List<StudentCourseAll> getCourse(Student student, Integer year, Integer semester) {
        return studentCourseAllMapper.select(student, year, semester);
    }

    public List<StudentCourseAll> getCourseAll(Student student) {
        return studentCourseAllMapper.select(student, null, null);
    }

}
