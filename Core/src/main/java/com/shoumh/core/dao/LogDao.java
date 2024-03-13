package com.shoumh.core.dao;

import com.shoumh.core.mapper.LogMapper;
import com.shoumh.core.pojo.LogSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LogDao {

    @Autowired
    private LogMapper logMapper;

    public void log(LogSheet sheet) {
        logMapper.log(sheet);
    }
}
