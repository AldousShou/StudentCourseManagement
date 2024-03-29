package com.shoumh.core.pojo;

import com.shoumh.core.common.log.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogSheet implements Serializable {
    private String uuid;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    /**
     * 使用 ms 作为单位
     */
    private Long durationInMillis;
    private String className;
    private String functionName;
    private String params;
    private LogLevel logLevel;
    private String source;
    private String description;
    /**
     * 编码为 json 格式
     */
    private String data;

    @Serial
    private static final long serialVersionUID = 1L;
}
