package com.shoumh.core.service;

import com.shoumh.core.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;

public interface StudentService {
    public Student getStuInfo(String stuId);

    public Boolean existUser(String stuId);
}
