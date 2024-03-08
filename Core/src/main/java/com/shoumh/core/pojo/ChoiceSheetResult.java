package com.shoumh.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 表单结果
 * 即学生选课表单中各个课程的选课成功状态
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceSheetResult implements Serializable {
    String stuId;
    String uuid;
    List<ChoiceResult> choiceResults;

    @Serial
    private static final long serialVersionUID = 1L;
}
