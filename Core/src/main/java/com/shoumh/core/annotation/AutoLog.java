package com.shoumh.core.annotation;

import com.shoumh.core.common.log.LogType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {
    boolean writeToConsole() default true;
    boolean writeToMQ() default false;
    boolean catchErrors() default false;
    String exchangeName() default "";
    LogType logType() default LogType.FUNCTION_NAME_ONLY;
    String description() default "";
}
