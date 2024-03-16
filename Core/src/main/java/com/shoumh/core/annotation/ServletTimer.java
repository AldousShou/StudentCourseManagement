package com.shoumh.core.annotation;

import java.lang.annotation.*;

/**
 * 实现写在 interceptor 中
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServletTimer {
    public boolean writeToConsole() default true;
    public boolean writeToMQ() default true;
}
