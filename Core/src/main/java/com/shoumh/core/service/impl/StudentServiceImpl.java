package com.shoumh.core.service.impl;

import com.google.gson.Gson;
import com.shoumh.core.dao.RedisUtil;
import com.shoumh.core.dao.StudentDao;
import com.shoumh.core.pojo.Student;
import com.shoumh.core.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private Gson gson;

    @Override
    public Student getStuInfo(String stuId) {
        if (stuId.isEmpty()) return new Student();

        String stuKey = redisUtil.concatKeys("stu", stuId);
        if (redisUtil.hasKey(stuKey)) {
            return gson.fromJson(redisUtil.get(stuKey), Student.class);
        } else {
            Student student = studentDao.getStuInfo(stuId);
            student = student == null ? new Student() : student;
            String studentJson = gson.toJson(student, Student.class);
            redisUtil.setWithExpiration(stuKey, studentJson, 60, TimeUnit.SECONDS);
            return student;
        }
    }

    @Override
    public Boolean existUser(String stuId) {
        if (stuId.isEmpty()) return false;

        String stuKey = redisUtil.concatKeys("stu", stuId);
        if (redisUtil.hasKey(stuKey)) {
            return true;
        } else {
            Student student = studentDao.getStuInfo(stuId);
            if (student == null) return false;
            String studentJson = gson.toJson(student, Student.class);
            redisUtil.setWithExpiration(stuKey, studentJson, 60, TimeUnit.SECONDS);
            return true;
        }
    }
}
