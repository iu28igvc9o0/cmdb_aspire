package com.aspire.mirror.misc.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@SpringBootConfiguration
@EnableAsync
public class ThreadPoolConfig {

	@Bean("miscAsync")
	public Executor miscAsync() {
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		
		//设置核心线程数(可以理解为初始线程数)
		executor.setCorePoolSize(1000);
		
		//设置最大线程数
		executor.setMaxPoolSize(1000);
		
		//设置等待队列容量(这个最好设置到最大)
		executor.setQueueCapacity(99999);
		
		//设置线程活跃时间(单位秒)
		executor.setKeepAliveSeconds(300);
		
		//配置线程池中的线程的名称前缀(方便在日志中输出观察)
		executor.setThreadNamePrefix("miscAsync-thread-");
		
		//设置拒绝策略：当pool已经达到max size的时候，不再启动新线程中执行任务，而是有调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		
		//执行初始化
		executor.initialize();
		
		return executor;
	}
}
