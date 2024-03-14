package com.shoumh.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 采用 google 的 Guava 包实现限流，原理是定时产生令牌，如果不能得到令牌则被限流
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {

    /**
     * 是否限流
     */
    public boolean limit() default true;

    /**
     * 根据 Key 限速，如果未填写，则默认使用 Class.Function 来进行限速
     */
    public String key() default "";

    /**
     * 获取令牌最长等待时间
     */
    public long timeout() default 300;

    /**
     * 每秒做多有几次提交
     */
    public double permitsPerSecond() default 500.0;

    /**
     * 时间窗口单位
     */
    public TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 当无法获取令牌时，返回的错误消息
     */
    public String msg() default "network too busy";
}
