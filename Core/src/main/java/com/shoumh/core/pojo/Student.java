package com.shoumh.core.pojo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生信息
 * **注意：请使用 builder 来创建**
 * @see Course
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {
    /**
     * 学生 id
     */
    private String stuId;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学生性别
     */
    private String gender;

    /**
     * 入学时间
     */
    private Date entryDt;

    /**
     * 毕业时间
     */
    private Date graduateDt;

    /**
     * 学院
     */
    private Integer college;

    /**
     * 专业
     */
    private Integer major;

    @Serial
    private static final long serialVersionUID = 1L;
}