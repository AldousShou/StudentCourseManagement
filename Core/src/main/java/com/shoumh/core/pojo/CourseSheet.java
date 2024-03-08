package com.shoumh.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 学生选课表单，不包含结果信息
 * @see ChoiceSheetResult
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSheet implements Serializable {
    private String uuid;
    private String stuId;
    private Integer major;
    private List<Course> courses;

    @Serial
    private static final long serialVersionUID = 1L;
}
