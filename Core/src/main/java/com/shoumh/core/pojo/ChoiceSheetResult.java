package com.shoumh.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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
