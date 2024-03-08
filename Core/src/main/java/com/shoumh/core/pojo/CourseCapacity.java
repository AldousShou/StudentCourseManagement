package com.shoumh.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 课程容量信息，包含课程大小和课程已选择人数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCapacity implements Serializable {
    private String courseId;
    private Long capacity;
    private Long selection;

    @Serial
    private static final long serialVersionUID = 1L;
}
