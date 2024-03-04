package com.shoumh.core.dao;

import com.shoumh.core.mapper.StudentMapper;
import com.shoumh.core.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDao {

    @Autowired
    private StudentMapper studentMapper;

    public Student getStuInfo(String stuId) {
        return studentMapper.selectById(stuId);
    }
}
