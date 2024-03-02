package com.shoumh.core.pojo;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教师
 * @TableName teacher
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher implements Serializable {
    /**
     * 教师 id
     */
    private String id;

    /**
     * 教师姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 专业
     */
    private Integer major;

    @Serial
    private static final long serialVersionUID = 1L;
}