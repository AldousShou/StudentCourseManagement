package com.shoumh.core.mapper;

import com.shoumh.core.pojo.Student;
import com.shoumh.core.pojo.StudentCourseAll;
import org.apache.ibatis.annotations.Mapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
* @author siriusshou
* @description 针对表【student_course_all_view】的数据库操作Mapper
* @createDate 2024-03-02 14:02:23
* @Entity com.shoumh.core.pojo.StudentCourseAllViewMapper
*/
@Mapper
public interface StudentCourseAllMapper {

    List<StudentCourseAll> select(Student student, Integer year, Integer semester);

}
