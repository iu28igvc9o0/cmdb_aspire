package com.aspire.ums.cmdb.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.aspire.ums.cmdb.util.TraceUtil;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/12 11:19
 */
@Configuration
@EnableAsync
public class EventExecutorConfig {

    @Bean("eventTaskExecutor")
    @Primary
    public ThreadPoolTaskExecutor asyncServiceExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new TraceUtil.ThreadPoolTaskExecutorMdcWrapper();
        // 设置核心线程数
        // executor.setCorePoolSize(cores);
        executor.setCorePoolSize(20);
        // 设置最大线程数
        executor.setMaxPoolSize(10 * 5);
        // 设置队列容量
        executor.setQueueCapacity(10000);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("event-thread-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    @Bean("instanceExecutor")
    public ThreadPoolTaskExecutor instanceExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new TraceUtil.ThreadPoolTaskExecutorMdcWrapper();
        // 设置核心线程数
        // executor.setCorePoolSize(cores);
        executor.setCorePoolSize(20);
        // 设置最大线程数
        executor.setMaxPoolSize(10 * 5);
        // 设置队列容量
        executor.setQueueCapacity(10000);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("instance-thread-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}
