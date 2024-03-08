package com.shoumh.core.pojo;

import com.shoumh.core.common.ChoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 选课表单项 courseId 及状态 courseStatus 
 * 对每一个选课表单项，指明该项状态
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceResult implements Serializable {
    private String courseId;
    private ChoiceStatus status;

    @Serial
    private static final long serialVersionUID = 1L;
}
