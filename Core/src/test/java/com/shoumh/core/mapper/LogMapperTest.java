package com.shoumh.core.mapper;

import com.google.gson.Gson;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.LogSheet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LogMapperTest {

    @Autowired
    private LogMapper logMapper;
    @Autowired
    private Gson gson;

    @Test
    void logTest() {
        LogSheet logSheet = new LogSheet();
        Course course = new Course();
        String json = gson.toJson(course);
        logSheet.setData(json);
        logMapper.log(logSheet);
    }

}