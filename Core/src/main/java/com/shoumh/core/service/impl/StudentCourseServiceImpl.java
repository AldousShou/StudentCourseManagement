package com.shoumh.core.service.impl;

import com.shoumh.core.common.SystemConstant;
import com.shoumh.core.dao.StudentCourseDao;
import com.shoumh.core.pojo.Student;
import com.shoumh.core.pojo.StudentCourseAll;
import com.shoumh.core.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    static final Integer YEAR = SystemConstant.YEAR;
    static final Integer SEMESTER = SystemConstant.SEMESTER;

    @Autowired
    private StudentCourseDao studentCourseDao;

    @Override
    public List<StudentCourseAll> getCourseChosenByStudent(Student student) {
        return studentCourseDao.getCourse(student, null, null);
    }

    @Override
    public List<StudentCourseAll> getCurrentCourseSelectedByStudent(Student student) {
        return studentCourseDao.getCourse(student, YEAR, SEMESTER);
    }

}
