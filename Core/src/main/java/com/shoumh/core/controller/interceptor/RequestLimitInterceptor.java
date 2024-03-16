package com.shoumh.core.controller.interceptor;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.shoumh.core.annotation.RequestLimit;
import com.shoumh.core.pojo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Component
public class RequestLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private Gson gson;

    private final Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequestLimit limit = handlerMethod.getMethodAnnotation(RequestLimit.class);
            if (limit == null || (!limit.limit())) {
                return true;
            } else {
                // 存在注解，并且设置为限行
                // 设置 Key
                String limitKey = null;
                if (limit.key().isEmpty()) {
                    String packageName = handlerMethod.getMethod().getDeclaringClass().getPackage().getName();
                    String methodName = handlerMethod.getMethod().getName();
                    limitKey = packageName + "." + methodName;
                } else {
                    limitKey = limit.key();
                }

                RateLimiter limiter = null;
                // 检查是否存在 RateLimiter
                if (limitMap.containsKey(limitKey)) {
                    limiter = limitMap.get(limitKey);
                    limiter.setRate(limit.permitsPerSecond());
                } else {
                    limiter = RateLimiter.create(limit.permitsPerSecond());
                    limitMap.put(limitKey, limiter);
                }

                // 获得令牌
                boolean acquired = limiter.tryAcquire(limit.timeout(), limit.timeUnit());
                if (acquired) {
                    return true;
                } else {
//                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType("application/json");
                    response.getWriter().write(
                            gson.toJson(
                                    new Result(1, "too many requests", null)
                            )
                    );
                    log.debug("[RequestLimitInterceptor] failed to get token from token bucker {}", limitKey);
                    log.debug("[RequestLimitInterceptor] limit request");
                    return false;
                }
            }
        } else {
            log.debug("[Interceptor] handler not instance of Handler method: {}, {}",
                    handler.getClass(), handler);
        }
        return true;
    }
}
