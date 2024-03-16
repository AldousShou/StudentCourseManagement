package com.shoumh.core.listener;

import com.shoumh.core.common.SystemConstant;
import com.shoumh.core.common.util.ThreadPoolProvider;
import com.shoumh.core.pojo.ChoiceSheetResult;
import com.shoumh.core.pojo.CourseSheet;
import com.shoumh.core.service.CourseAsyncService;
import com.shoumh.core.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class CourseListener {

    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseAsyncService courseAsyncService;

    private final ExecutorService poolForLog = ThreadPoolProvider.getThreadPoolForLog();
    private final ExecutorService poolForDB = ThreadPoolProvider.getThreadPoolForIO();

    /**
     * 用于处理 before_check 队列中的表单录入 log 数据库的处理
     */
    @RabbitListener(queues = SystemConstant.CHOICE_BEFORE_CHECK_QUEUE_LOG)
    public void listenChoiceBeforeCheckQueueLog(CourseSheet sheet) {
        log.debug("[queue '{}'] logging sheet '{}'", SystemConstant.CHOICE_BEFORE_CHECK_QUEUE_LOG, sheet.getUuid());
        courseAsyncService.logSheetStatus(sheet);
    }

    /**
     * 用于处理 before_check 队列中的表单的课程的合法性检查，
     * 并将检查完的所有课程扔进 CHOICE_CHECKED_EXCHANGE 交换机中
     */
    @RabbitListener(queues = SystemConstant.CHOICE_BEFORE_CHECK_QUEUE_CHECK)
    public void listenChoiceBeforeCheckQueueCheck(CourseSheet sheet) {
        // 从 before_check 队列中拿出元素处理，处理完后扔进交换机中，交换机：CHOICE_CHECKED_EXCHANGE
        log.debug("[queue '{}'] checking sheet '{}'", SystemConstant.CHOICE_BEFORE_CHECK_QUEUE_CHECK, sheet.getUuid());
        courseAsyncService.checkAndSendChoiceSheetLegality(sheet);
    }

    /**
     * 用于处理 checked 队列中的表单的持久化处理，
     */
    @RabbitListener(queues = SystemConstant.CHOICE_CHECKED_QUEUE_DB)
    public void listenChoiceCheckedQueueDB(ChoiceSheetResult sheet) {
        poolForDB.submit(() -> {
            log.debug("[queue '{}'] writing into db sheet '{}'", SystemConstant.CHOICE_CHECKED_QUEUE_DB, sheet.getUuid());
            courseAsyncService.writeChoiceSheet(sheet);
        });
    }

    /**
     * 用于处理 checked 队列中的表单的 log 更新
     */
    @RabbitListener(queues = SystemConstant.CHOICE_CHECKED_QUEUE_LOG)
    public void listenChoiceCheckedQueueLog(ChoiceSheetResult sheet) {
        poolForLog.submit(() -> {
            log.debug("[queue '{}'] updating status sheet '{}'", SystemConstant.CHOICE_CHECKED_QUEUE_LOG, sheet.getUuid());
            courseAsyncService.updateSheetStatus(sheet);
        });
    }

}
