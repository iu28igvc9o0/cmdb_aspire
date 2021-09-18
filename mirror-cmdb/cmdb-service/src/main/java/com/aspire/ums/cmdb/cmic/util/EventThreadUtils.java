package com.aspire.ums.cmdb.cmic.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.aspire.ums.cmdb.util.SpringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/12 11:18
 */
@Slf4j
public class EventThreadUtils {

    public final static ThreadPoolTaskExecutor FIXED_POOL = SpringUtils.getBean("eventTaskExecutor", ThreadPoolTaskExecutor.class);

    public final static ThreadPoolTaskExecutor INSTANCE_POOL = SpringUtils.getBean("instanceExecutor",
            ThreadPoolTaskExecutor.class);

    public final static ThreadPoolTaskExecutor NORMAL_POOL = SpringUtils.getBean("normalTaskExecutor",
            ThreadPoolTaskExecutor.class);

    public static void printStats(ThreadPoolExecutor threadPool) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("=========================");
            log.info("Pool Size: {}", threadPool.getPoolSize());
            log.info("Active Threads: {}", threadPool.getActiveCount());
            log.info("Number of Tasks Completed: {}", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());

            log.info("=========================");
        }, 0, 1, TimeUnit.SECONDS);
    }
}
