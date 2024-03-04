package com.shoumh.core.service;

import com.shoumh.core.pojo.Student;
import com.shoumh.core.pojo.StudentCourseAll;

import java.util.List;

public interface StudentCourseService {
    public List<StudentCourseAll> getCourseChosenByStudent(Student student);
    public List<StudentCourseAll> getCurrentCourseSelectedByStudent(Student student);

}
