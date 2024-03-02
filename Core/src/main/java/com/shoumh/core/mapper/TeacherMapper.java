package com.shoumh.core.mapper;

import com.shoumh.core.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;

/**
* @author siriusshou
* @description 针对表【teacher(教师)】的数据库操作Mapper
* @createDate 2024-03-01 19:40:39
* @Entity com.shoumh.core.pojo.TeacherMapper
*/
@Mapper
public interface TeacherMapper {

    void deleteById(String id);

    void insert(Teacher teacher);

    void insertSelective(Teacher teacher);

    Teacher selectById(String id);

    void updateByIdSelective(Teacher record);

    void updateById(Teacher record);

}
