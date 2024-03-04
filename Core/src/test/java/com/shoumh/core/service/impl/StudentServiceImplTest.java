package com.shoumh.core.service.impl;

import com.shoumh.core.pojo.Student;
import com.shoumh.core.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @Test
    void getStuInfo() {
        Student stu = studentService.getStuInfo("123456");
        log.debug(stu.toString());
        Student stu2 = studentService.getStuInfo("000000");
        log.debug(stu2.toString());
    }
}