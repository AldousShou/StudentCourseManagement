package com.shoumh.core.annotation.aspect;

import com.google.gson.Gson;
import com.shoumh.core.annotation.AutoLog;
import com.shoumh.core.common.log.LogLevel;
import com.shoumh.core.common.log.LogType;
import com.shoumh.core.common.util.ThreadPoolProvider;
import com.shoumh.core.pojo.LogSheet;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Aspect
@Component
@Slf4j
public class AutoLogAspect {

    @Autowired
    private Gson gson;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private final ExecutorService threadPool = ThreadPoolProvider.getThreadPoolForLog();

    @Around("@annotation(com.shoumh.core.annotation.AutoLog)")
    public Object methodExporter(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        AutoLog autoLog = method.getAnnotation(AutoLog.class);

        String uuid = UUID.randomUUID().toString();

        LocalDateTime startDateTime = LocalDateTime.now();
        StringJoiner joiner = new StringJoiner(", ");
        String[] params = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        for (String param: params) {
            joiner.add(param);
        }
        String paramString = joiner.toString();
        LogSheet sheet = LogSheet.builder()
                .uuid(uuid)
                .className(joinPoint.getTarget().getClass().getName())
                .functionName(joinPoint.getSignature().getName())
                .params(paramString)
                .logLevel(LogLevel.INFO)
                .startDateTime(startDateTime)
                .description(autoLog.description())
                .build();

        String target;
        switch (autoLog.logType()) {
            case FUNCTION_NAME_ONLY -> {
                target = String.format("target: %s.%s", sheet.getClassName(), sheet.getFunctionName());
            }
            case FUNCTION_NAME_AND_PARAMS -> {
                target = String.format("target: %s.%s, params: %s", sheet.getClassName(), sheet.getFunctionName(),
                        sheet.getParams());
            }
            default -> {
                throw new Exception("Not Implemented");
            }
        }
        // 开始
        threadPool.submit(() -> {
            if (autoLog.writeToConsole()) {
                log.debug("[AutoLog] {}, start at: {}",
                        target,
                        sheet.getStartDateTime());
            }
            if (autoLog.writeToMQ() && (!autoLog.exchangeName().isEmpty())) {
                rabbitTemplate.convertAndSend(autoLog.exchangeName(), "", sheet);
            }
        });

        // 执行
        Object proceed = null;
        if (autoLog.catchErrors()) {
            try {
                proceed = joinPoint.proceed();
            } catch (RuntimeException e) {
                threadPool.submit(() -> {
                    sheet.setLogLevel(LogLevel.WARNING);
                    if (autoLog.writeToConsole()) {
                        log.debug("[AutoLog] {}, start at: {}, catch exception: {}",
                                target, sheet.getStartDateTime(), e.getMessage());
                    }
                    if (autoLog.writeToMQ()) {
                        rabbitTemplate.convertAndSend(autoLog.exchangeName(), "", sheet);
                    }
                });
                throw e;
            } catch (Exception e) {
                threadPool.submit(() -> {
                    sheet.setLogLevel(LogLevel.ERROR);
                    if (autoLog.writeToConsole()) {
                        log.debug("[AutoLog] {}, start at: {}, catch exception: {}",
                                target, sheet.getStartDateTime(), e.getMessage());
                    }
                    if (autoLog.writeToMQ()) {
                        rabbitTemplate.convertAndSend(autoLog.exchangeName(), "", sheet);
                    }
                });
                throw e;
            }
        } else {
            proceed = joinPoint.proceed();
        }


        // 结束
        threadPool.submit(() -> {
            LocalDateTime endDateTime = LocalDateTime.now();
            sheet.setEndDateTime(endDateTime);
            Duration duration = Duration.between(startDateTime, endDateTime);
            sheet.setDurationInMillis(duration.toMillis());
            sheet.setLogLevel(LogLevel.INFO);
            if (autoLog.writeToConsole()) {
                if (autoLog.logType().equals(LogType.FUNCTION_NAME_ONLY)) {
                    log.debug("[AutoLog] target:{}.{}, start at: {}, end at: {}, interval: {} ms",
                            sheet.getClassName(),
                            sheet.getFunctionName(),
                            sheet.getStartDateTime(),
                            sheet.getEndDateTime(),
                            duration.toMillis());
                } else if (autoLog.logType().equals(LogType.FUNCTION_NAME_AND_PARAMS)) {
                    log.debug("[AutoLog] target:{}.{}, params: {}, start at: {}, interval: {} ms",
                            sheet.getClassName(),
                            sheet.getFunctionName(),
                            sheet.getParams(),
                            sheet.getStartDateTime(),
                            sheet.getDurationInMillis());
                }
            }
            if (autoLog.writeToMQ() && (!autoLog.exchangeName().isEmpty())) {
                rabbitTemplate.convertAndSend(autoLog.exchangeName(), "", sheet);
            }
        });
        return proceed;
    }
}
