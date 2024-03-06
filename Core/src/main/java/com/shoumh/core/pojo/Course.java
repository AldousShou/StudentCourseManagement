package com.shoumh.core.pojo;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName course_view
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course implements Serializable {
    /**
     * 课程 id
     */
    private String courseId;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 教师 id
     */
    private String teacherId;

    /**
     * 学分
     */
    private BigDecimal credit;

    /**
     * 授课位置
     */
    private String location;

    /**
     * 是否存在先继课程
     */
    private Integer hasPredecessor;

    /**
     * 是否限制专业
     */
    private Integer hasMajorDemand;

    /**
     * 可修学年
     */
    private Integer year;

    /**
     * 可修学期
     */
    private Integer semester;

    /**
     * 教师姓名
     */
    private String teacherName;

    @Serial
    private static final long serialVersionUID = 1L;
}