package com.shoumh.core.mapper;

import com.shoumh.core.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author siriusshou
* @description 针对表【student(学生信息表)】的数据库操作Mapper
* @createDate 2024-03-01 19:33:39
* @Entity com.shoumh.core.pojo.StudentMapper
*/
@Mapper
public interface StudentMapper {

    void deleteById(String stuId);

    void insert(Student record);

    void insertSelective(Student record);

    Student selectById(String stuId);

    void updateById(Student record);

    void updateByIdSelective(Student record);

}
