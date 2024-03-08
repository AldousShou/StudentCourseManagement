package com.shoumh.core.pojo;

import com.shoumh.core.common.ChoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceResult implements Serializable {
    private String courseId;
    private ChoiceStatus courseStatus;

    @Serial
    private static final long serialVersionUID = 1L;
}
