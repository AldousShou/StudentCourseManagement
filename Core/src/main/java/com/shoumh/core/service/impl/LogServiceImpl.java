package com.shoumh.core.service.impl;

import com.shoumh.core.dao.LogDao;
import com.shoumh.core.pojo.LogSheet;
import com.shoumh.core.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDao logDao;

    @Override
    public void log(LogSheet sheet) {
        logDao.log(sheet);
    }
}
