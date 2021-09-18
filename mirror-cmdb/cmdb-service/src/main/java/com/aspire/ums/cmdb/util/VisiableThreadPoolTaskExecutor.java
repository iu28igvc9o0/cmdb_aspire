package com.aspire.ums.cmdb.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private static final Logger logger = LoggerFactory.getLogger(VisiableThreadPoolTaskExecutor.class);

    private static final long serialVersionUID = 1228455424732927018L;

    private void showThreadPoolInfo(String prefix) {

        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

        if (null == threadPoolExecutor) {
            return;
        }
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("visiable-pool-%d").build();
        ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1, namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        pool.scheduleAtFixedRate(() -> {
            logger.info("=========================");
            logger.info("Pool Size: {}", threadPoolExecutor.getPoolSize());
            logger.info("Active Threads: {}", threadPoolExecutor.getActiveCount());
            logger.info("Number of Tasks Completed: {}", threadPoolExecutor.getCompletedTaskCount());
            logger.info("Number of Tasks in Queue: {}", threadPoolExecutor.getQueue().size());

            logger.info("=========================");
        }, 0, 1, TimeUnit.SECONDS);
        //
        // logger.info("{}, {},taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]",
        //
        // this.getThreadNamePrefix(),
        //
        // prefix,
        //
        // threadPoolExecutor.getTaskCount(),
        //
        // threadPoolExecutor.getCompletedTaskCount(),
        //
        // threadPoolExecutor.getActiveCount(),
        //
        // threadPoolExecutor.getQueue().size());

    }

    @Override

    public void execute(Runnable task) {

        showThreadPoolInfo("1. do execute");

        super.execute(task);

    }

    @Override

    public void execute(Runnable task, long startTimeout) {

        showThreadPoolInfo("2. do execute");

        super.execute(task, startTimeout);

    }

    @Override

    public Future<?> submit(Runnable task) {

        showThreadPoolInfo("1. do submit");

        return super.submit(task);

    }

    @Override

    public <T> Future<T> submit(Callable<T> task) {

        showThreadPoolInfo("2. do submit");

        return super.submit(task);

    }

    @Override

    public ListenableFuture<?> submitListenable(Runnable task) {

        showThreadPoolInfo("1. do submitListenable");

        return super.submitListenable(task);

    }

    @Override

    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {

        showThreadPoolInfo("2. do submitListenable");

        return super.submitListenable(task);

    }

}
