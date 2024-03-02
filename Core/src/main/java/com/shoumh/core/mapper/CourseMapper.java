package com.shoumh.core.mapper;

import com.shoumh.core.pojo.Course;
import org.apache.ibatis.annotations.Mapper;

/**
* @author siriusshou
* @description 针对表【course】的数据库操作Mapper
* @createDate 2024-03-01 21:40:54
* @Entity com.shoumh.core.pojo.CourseMapper
*/
@Mapper
public interface CourseMapper {

    void deleteById(String id);

    void insert(Course record);

    void insertSelective(Course record);

    Course selectById(String id);

    void updateByIdSelective(Course record);

    void updateById(Course record);

}
