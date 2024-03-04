package com.shoumh.core.mapper;

import com.shoumh.core.pojo.Student;
import com.shoumh.core.pojo.StudentCourseAll;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class StudentCourseAllMapperTest {

    @Autowired
    private StudentCourseAllMapper studentCourseAllMapper;

    @Test
    void selectById() {
        StudentCourseAll res = studentCourseAllMapper.selectById(2);
        log.debug(res.toString());
    }

    @Test
    void select() {
        Student student = new Student();
        List<StudentCourseAll> res = studentCourseAllMapper.select(student, 2024, 1);
        log.debug(res.toString());
    }

    @Test
    void deleteById() {
        studentCourseAllMapper.deleteById("1");
    }
}