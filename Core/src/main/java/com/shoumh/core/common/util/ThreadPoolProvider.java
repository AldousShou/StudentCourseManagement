package com.shoumh.core.common.util;

import ch.qos.logback.core.pattern.FormatInfo;
import com.shoumh.core.common.ThreadPoolType;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

@Slf4j
@Component
public class ThreadPoolProvider {
    /**
     * cpu 核数，按照生产环境填写
     */
    private static final int CPU_CORE = 8;
    /**
     * 阻塞系数，通常在 0.7到 0.9之间，通过压测得到
     */
    private static final float BLOCKING_RATIO = 0.75F;

    // --------
    // 以下参数通过计算得到，请勿修改
    private static final int MAXIMUM_POOL_SIZE_COMPUTE_INTENSIVE = CPU_CORE + 1;
    private static final int MAXIMUM_POOL_SIZE_IO_INTENSIVE = (int) (CPU_CORE / (1 - BLOCKING_RATIO));
    private static final int CORE_POOL_SIZE_COMPUTE_INTENSIVE = MAXIMUM_POOL_SIZE_COMPUTE_INTENSIVE / 5;
    private static final int CORE_POOL_SIZE_IO_INTENSIVE = MAXIMUM_POOL_SIZE_IO_INTENSIVE / 5;

    private static ExecutorService threadPoolForComputing;
    private static ExecutorService threadPoolForIO;
    /**
     * 认定记录 log 的线程为 IO 密集型
     */
    private static ExecutorService threadPoolForLog;

    private static HashMap<String, ExecutorService> threadPoolMap;

    private ThreadPoolProvider() {}

    public static synchronized ExecutorService getThreadPoolForComputing() {
        if (threadPoolForComputing == null) {
            threadPoolForComputing = new ThreadPoolExecutor(CORE_POOL_SIZE_COMPUTE_INTENSIVE,
                    MAXIMUM_POOL_SIZE_COMPUTE_INTENSIVE,
                    2,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>());
        }
        return threadPoolForComputing;
    }

    public static synchronized ExecutorService getThreadPoolForIO() {
        if (threadPoolForIO == null) {
            threadPoolForIO = new ThreadPoolExecutor(CORE_POOL_SIZE_IO_INTENSIVE,
                    MAXIMUM_POOL_SIZE_IO_INTENSIVE,
                    2,
                    TimeUnit.MINUTES,
                    new LinkedBlockingDeque<>());
        }
        return threadPoolForIO;
    }

    public static synchronized ExecutorService getThreadPoolForLog() {
        if (threadPoolForLog == null) {
            threadPoolForLog = new ThreadPoolExecutor(CORE_POOL_SIZE_IO_INTENSIVE,
                    MAXIMUM_POOL_SIZE_IO_INTENSIVE,
                    2,
                    TimeUnit.MINUTES,
                    new LinkedBlockingDeque<>());
        }
        return threadPoolForLog;
    }

    public static synchronized ExecutorService getOrCreateThreadPool(@NotNull String poolName, ThreadPoolType type) {
        if (poolName.isEmpty()) {
            throw new IllegalArgumentException("pool name must not be empty");
        }
        if (!threadPoolMap.containsKey(poolName)) {
            log.debug("[ThreadPool] thread pool named {} does not exist", poolName);
            log.debug("[ThreadPool] creating new pool named {}", poolName);

            if (type == null || type == ThreadPoolType.COMPUTE_INTENSIVE) {
                ExecutorService threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE_COMPUTE_INTENSIVE,
                        MAXIMUM_POOL_SIZE_COMPUTE_INTENSIVE,
                        2,
                        TimeUnit.MINUTES,
                        new LinkedBlockingDeque<>());
                threadPoolMap.put(poolName, threadPool);
                return threadPool;
            } else if (type == ThreadPoolType.IO_INTENSIVE) {
                ExecutorService threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE_IO_INTENSIVE,
                        MAXIMUM_POOL_SIZE_IO_INTENSIVE,
                        2,
                        TimeUnit.MINUTES,
                        new LinkedBlockingDeque<>());
                threadPoolMap.put(poolName, threadPool);
                return threadPool;
            } else {
                throw new UnsupportedOperationException("this part has not be implemented");
            }

        } else {
            return threadPoolMap.get(poolName);
        }
    }
}
