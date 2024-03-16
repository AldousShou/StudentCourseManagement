package com.shoumh.core.annotation.aspect;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.shoumh.core.annotation.RequestLimit;
import com.shoumh.core.pojo.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
//@Component
@Deprecated
public class RequestLimitAspect {

    private Gson gson;
    private final Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    @Around("@annotation(com.shoumh.core.annotation.RequestLimit)")
    public Object requestLimitAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequestLimit requestLimit = method.getAnnotation(RequestLimit.class);

        // 如果不限流，直接放行
        if (!requestLimit.limit()) {
            return joinPoint.proceed();
        }

        // 设置 limitKey
        String limitKey = requestLimit.key();
        if (requestLimit.key().isEmpty()) {
            String className = joinPoint.getTarget().getClass().getName();
            String functionName = method.getName();
            limitKey = className + "." + functionName;
        }


        RateLimiter limiter = null;
        if (!limitMap.containsKey(limitKey)) {
            limiter = RateLimiter.create(requestLimit.permitsPerSecond());
            limitMap.put(limitKey, limiter);
            log.debug("[RequestLimit] new token bucket named {} created, size: {}",
                    limitKey,
                    requestLimit.permitsPerSecond());
        } else {
            limiter = limitMap.get(limitKey);
        }

        boolean acquire = limiter.tryAcquire(requestLimit.timeout(), requestLimit.timeUnit());
        if (!acquire) {
            log.debug("[RequestLimit] failed to get token from token bucket named {}", limitKey);
            responseFail(requestLimit.msg());
            return null;
        }
        log.debug("[RequestLimit] got token from token bucked named {}", limitKey);
        return joinPoint.proceed();
    }

    private void responseFail(String msg) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            try {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write(
                        gson.toJson(
                                new Result(1, msg)
                        )
                );
            } catch (IOException e) {
                log.warn("[RateLimit] failed to set response");
            }
        } else {
            log.error("[RateLimit] failed to get response");
        }
    }
}
