package com.shoumh.core.service;

import com.shoumh.core.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;

public interface StudentService {
    /**
     * 获取学生个人信息
     */
    public Student getStuInfo(String stuId);

    /**
     * 是否存在学生
     */
    public Boolean existUser(String stuId);
}
