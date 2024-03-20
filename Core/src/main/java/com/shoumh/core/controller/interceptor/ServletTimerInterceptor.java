package com.shoumh.core.controller.interceptor;

import com.shoumh.core.annotation.ServletTimer;
import com.shoumh.core.common.SystemConstant;
import com.shoumh.core.common.log.LogLevel;
import com.shoumh.core.common.util.ThreadPoolProvider;
import com.shoumh.core.pojo.LogSheet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class ServletTimerInterceptor implements HandlerInterceptor {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ConcurrentHashMap<String, Boolean> writeToConsole = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> writeToMQ = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LogSheet> logSheetMap = new ConcurrentHashMap<>();

    private final ExecutorService threadPool = ThreadPoolProvider.getThreadPoolForLog();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ServletTimer timer = handlerMethod.getMethodAnnotation(ServletTimer.class);
            if (timer != null) {
                request.setAttribute("servlet-uuid", uuid);

                LogSheet logSheet = LogSheet.builder()
                                            .uuid(uuid)
                                            .startDateTime(now)
                                            .className(handlerMethod.getMethod().getDeclaringClass().getName())
                                            .functionName(handlerMethod.getMethod().getName())
                                            .logLevel(LogLevel.INFO)
                                            .source("ServletTimer")
                                            .params(Arrays.toString(handlerMethod.getMethod().getParameters()))
                                            .build();

                writeToConsole.put(uuid, timer.writeToConsole());
                writeToMQ.put(uuid, timer.writeToMQ());
                logSheetMap.put(uuid, logSheet);

                if (timer.writeToConsole()) {
                    log.debug("[ServletTimer] uuid: {}, start: {}",
                            uuid, now);
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Object object = request.getAttribute("servlet-uuid");
        if (object != null) {
            String uuid = object.toString();
            LogSheet logSheet = logSheetMap.get(uuid);
            LocalDateTime start = logSheet.getStartDateTime();
            LocalDateTime end = LocalDateTime.now();
            Duration duration = Duration.between(start, end);
            logSheet.setEndDateTime(end);
            logSheet.setDurationInMillis(duration.toMillis());

            if (writeToConsole.get(uuid)) {
                log.debug("[ServletTimer] uuid: {}, start: {}, end: {}, duration: {}ms",
                        uuid, start, end, duration.toMillis());
            }
            writeToConsole.remove(uuid);

            if (writeToMQ.get(uuid)) {
                rabbitTemplate.convertAndSend(SystemConstant.LOG_EXCHANGE, "", logSheet);
            }
            writeToMQ.remove(uuid);
        }
    }
}
