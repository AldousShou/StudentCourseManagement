package com.shoumh.core.mapper;

import com.google.gson.Gson;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.Student;
import org.junit.jupiter.api.Test;
import org.mockito.internal.SuppressSignatureCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseMapperTest {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private Gson gson;

    @Test
    void selectAllSeletive() {
        Student student = new Student();
//        student.setStuId("s_0000");
        Course course = new Course();
        course.setYear(2023);
        String json = gson.toJson(courseMapper.selectAllSeletive(student, course, "ended", null, null));
        System.out.println(json);
    }

    @Test
    void selectUnchosenSelective() {
        Student student = new Student();
        student.setStuId("s_0000");
        Course course = new Course();
        course.setYear(2024);
        course.setSemester(1);
        course.setHasMajorDemand(0);
        String json = gson.toJson(courseMapper.selectUnchosenSelective(student, course, 0, 50));
        System.out.println(json);
    }

}