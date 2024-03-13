package com.shoumh.core.mapper;

import com.shoumh.core.pojo.LogSheet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LogMapperTest {

    @Autowired
    private LogMapper logMapper;

    @Test
    void logTest() {
        LogSheet logSheet = new LogSheet();
        logMapper.log(logSheet);
    }

}