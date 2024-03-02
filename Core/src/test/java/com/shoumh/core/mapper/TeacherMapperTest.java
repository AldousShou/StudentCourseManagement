package com.shoumh.core.mapper;

import com.shoumh.core.pojo.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;
    private final Teacher teacher = new Teacher("123123", "name", "male", 0);

    @Test
    void deleteById() {
        teacherMapper.deleteById("123123");
        teacherMapper.deleteById("123124");
    }

    @Test
    void insert() {
        teacherMapper.insert(teacher);
    }

    @Test
    void insertSelective() {
        teacher.setId("123124");
        teacherMapper.insertSelective(teacher);
    }

    @Test
    void selectById() {
        teacherMapper.selectById(teacher.getId());
    }

    @Test
    void updateById() {
        teacherMapper.updateById(teacher);
    }

    @Test
    void updateByIdSelective() {
        teacherMapper.updateByIdSelective(teacher);
    }
}