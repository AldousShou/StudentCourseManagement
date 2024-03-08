package com.shoumh.core.service;

import com.shoumh.core.pojo.Student;
import com.shoumh.core.pojo.StudentCourseAll;

import java.util.List;

public interface StudentCourseService {
    /**
     * 获得学生已选课
     * **注意！请使用 CourseService 中的相关接口**
     */
    @Deprecated
    public List<StudentCourseAll> getCourseChosenByStudent(Student student);

    /**
     * 获得学生当前选课
     * **注意！请使用 CourseService 中的相关接口**
     */
    @Deprecated
    public List<StudentCourseAll> getCurrentCourseSelectedByStudent(Student student);

}
