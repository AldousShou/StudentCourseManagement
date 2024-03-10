package com.shoumh.core.pojo.template;

import com.shoumh.core.pojo.Student;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class StudentTemplate {
    private StudentTemplate() {}

    public static Student studentWithId(@NotNull String stuId) {
        Student student = new Student();
        student.setStuId(stuId);
        return student;
    }

    public static Student studentWithMajor(@NotNull Integer major) {
        Student student = new Student();
        student.setMajor(major);
        return student;
    }

    public static Student studentWithBasicSettings(String stuId, String name, String gender, Integer major) {
        return new Student(stuId, name, gender, null, null, null, major);
    }
}
