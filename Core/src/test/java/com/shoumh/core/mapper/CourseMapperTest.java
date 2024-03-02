package com.shoumh.core.mapper;

import com.shoumh.core.pojo.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseMapperTest {
    @Autowired
    private CourseMapper courseMapper;
    private final Course course = new Course("123123", "new course", "123123", BigDecimal.valueOf(2.5), "loc", 0, 0);

    @Test
    void deleteById() {
        courseMapper.deleteById("123123");
        courseMapper.deleteById("123124");
    }

    @Test
    void insert() {
        courseMapper.insert(course);
    }

    @Test
    void insertSelective() {
        course.setId("123124");
        courseMapper.insertSelective(course);
    }

    @Test
    void selectById() {
        courseMapper.selectById(course.getId());
    }

    @Test
    void updateByIdSelective() {
        courseMapper.updateByIdSelective(course);
    }

    @Test
    void updateById() {
        courseMapper.updateById(course);
    }
}