package com.shoumh.core.annotation;

import com.google.gson.Gson;
import com.shoumh.core.common.LogType;
import com.shoumh.core.pojo.LogSheet;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.ApiStatus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class AutoLogAspect {

    @Autowired
    private Gson gson;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Around("@annotation(com.shoumh.core.annotation.AutoLog)")
    public Object methodExporter(ProceedingJoinPoint jointPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) jointPoint.getSignature();
        Method method = methodSignature.getMethod();
        AutoLog autoLog = method.getAnnotation(AutoLog.class);

        String uuid = UUID.randomUUID().toString();

        LocalDateTime startDateTime = LocalDateTime.now();
        StringJoiner joiner = new StringJoiner(", ");
        String[] params = ((MethodSignature) jointPoint.getSignature()).getParameterNames();
        for (String param: params) {
            joiner.add(param);
        }
        String paramString = joiner.toString();
        LogSheet sheet = LogSheet.builder()
                .uuid(uuid)
                .className(jointPoint.getTarget().getClass().getName())
                .functionName(jointPoint.getSignature().getName())
                .params(paramString)
                .startDateTime(startDateTime)
                .description(autoLog.description())
                .build();
        if (autoLog.writeToConsole()) {
            if (autoLog.logType() == LogType.FUNCTION_NAME_ONLY) {
                log.debug("[AutoLog] target:{}.{}, start at: {}",
                        sheet.getClassName(),
                        sheet.getFunctionName(),
                        sheet.getStartDateTime());
            } else if (autoLog.logType() == LogType.FUNCTION_NAME_AND_PARAMS) {
                log.debug("[AutoLog] target:{}.{}, params: {}, start at: {}",
                        sheet.getClassName(),
                        sheet.getFunctionName(),
                        sheet.getParams(),
                        sheet.getStartDateTime());
            }
        }
        if (autoLog.writeToMQ() && (!autoLog.exchangeName().isEmpty())) {
            rabbitTemplate.convertAndSend(autoLog.exchangeName(), "", sheet);
        }


        Object proceed = jointPoint.proceed();


        LocalDateTime endDateTime = LocalDateTime.now();
        sheet.setEndDateTime(endDateTime);
        Duration duration = Duration.between(startDateTime, endDateTime);
        sheet.setDurationInMillis(duration.toMillis());
        if (autoLog.writeToConsole()) {
            if (autoLog.logType() == LogType.FUNCTION_NAME_ONLY) {
                log.debug("[AutoLog] target:{}.{}, start at: {}, end at: {}, interval: {} ms",
                        sheet.getClassName(),
                        sheet.getFunctionName(),
                        sheet.getStartDateTime(),
                        sheet.getEndDateTime(),
                        duration.toMillis());
            } else if (autoLog.logType() == LogType.FUNCTION_NAME_AND_PARAMS) {
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
        return proceed;
    }
}
