package com.shoumh.core.annotation;

import com.shoumh.core.common.log.LogType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {
    boolean writeToConsole() default true;
    boolean writeToMQ() default false;
    /**
     * 如果正在调试，请不要设置这个为 true，会捕获异常
     */
    boolean catchErrors() default false;
    String exchangeName() default "";
    LogType logType() default LogType.FUNCTION_NAME_ONLY;
    String description() default "";
}
