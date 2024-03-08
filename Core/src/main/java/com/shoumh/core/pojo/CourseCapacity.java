package com.shoumh.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

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
