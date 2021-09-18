package com.aspire.ums.cmdb.collect.pool;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ThreadPool
 * Author:   zhu.juwang
 * Date:     2019/4/3 20:10
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class ThreadPool {

    private static ExecutorService executorService;
    public static ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(20, 30, 10, TimeUnit.MINUTES,  new LinkedBlockingDeque<>(),
                    new CustomThreadFactory(), new CustomRejectedExecutionHandler());
        }
        return executorService;
    }
    static class CustomThreadFactory implements ThreadFactory {
        private AtomicInteger count = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String threadName = ThreadPool.class.getSimpleName() + count.addAndGet(1);
            t.setName(threadName);
            log.info("Collect thread name -> {}", threadName);
            return t;
        }
    }

    static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            while (executor.getQueue().size() >= 1000) {
                try {
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    log.error("", e);
                    throw new RuntimeException(e);
                }
            }
            //如果线程队列 少于50个. 则追加
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                log.error("队列增加线程失败.", e);
                throw new RuntimeException(e);
            }
        }
    }

    public static void destroy() {
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                log.error("Terminate thread error.", e);
            }
        }
    }

    public static void main(String[] a) {
        ExecutorService executorService = ThreadPool.getExecutorService();
        for (int i = 0; i < 100000; i ++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
