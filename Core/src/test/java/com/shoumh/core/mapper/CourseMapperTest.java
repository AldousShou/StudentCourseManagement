package com.shoumh.core.mapper;

import com.google.gson.Gson;
import com.shoumh.core.common.CourseStatus;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CourseMapperTest {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private Gson gson;

    @Test
    void selectAllSeletive() {
        Student student = new Student();
//        student.setStuId("s_0000");
        Course course = new Course();
        course.setYear(2023);
        String json = gson.toJson(courseMapper.selectAllSelective(student, course, "ended", null, null));
        System.out.println(json);
    }

    @Test
    void selectUnchosenSelective() {
        Student student = new Student();
        student.setStuId("s_0000");
        Course course = new Course();
        course.setYear(2024);
        course.setSemester(1);
        course.setHasMajorDemand(0);
        String json = gson.toJson(courseMapper.selectUnchosenSelective(student, course, 0, 50));
        System.out.println(json);
    }

    @Test
    void hasAllSelective() {
        String stuId = "01";
        String courseId = "101";
        CourseStatus status = null;
//        Student student = StudentTemplate.studentWithId(stuId);
//        Course course = CourseTemplate.courseWithCourseId(courseId);
        Student student = Student.builder().stuId(stuId).build();
        Course course = Course.builder().courseId(courseId).build();
        Boolean res = courseMapper.hasAllSelective(student, course, status==null?null:status.toString());
        System.out.println(res);
    }

}