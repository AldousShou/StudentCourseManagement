package com.shoumh.core.pojo;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName course
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {
    /**
     * 课程 id
     */
    private String id;

    /**
     * 课程名
     */
    private String name;

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

    @Serial
    private static final long serialVersionUID = 1L;
}