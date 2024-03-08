package com.shoumh.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {
    private Integer code;
    private String msg;
    private Object data;

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public static Result success() {
        return new Result(0, "success", null);
    }

    public static Result success(Object data) {
        return new Result(0, "success", data);
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
