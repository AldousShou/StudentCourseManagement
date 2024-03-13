package com.shoumh.core.listener;

import com.shoumh.core.common.SystemConstant;
import com.shoumh.core.pojo.LogSheet;
import com.shoumh.core.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogListener {

    @Autowired
    private LogService logService;

    @RabbitListener(queues = SystemConstant.LOG_QUEUE)
    public void listenLogQueue(LogSheet logSheet) {
        log.debug("[queue '{}'] log sheet: {}", SystemConstant.LOG_QUEUE, logSheet);
        logService.log(logSheet);
    }
}
