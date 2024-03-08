package com.shoumh.core.pojo;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生与课程混合表
 * **已弃用**
 * @see Student
 * @see Course
 */
@Deprecated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseAll implements Serializable {

    /**
     * 记录 id
     */
    private String recordId;

    /**
     * 学生 id
     */
    private String stuId;

    /**
     * 课程 id
     */
    private String courseId;

    /**
     * 选课时间
     */
    private Date selectDt;

    /**
     * 学生姓名
     */
    private String stuName;

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

    @Serial
    private static final long serialVersionUID = 1L;
}