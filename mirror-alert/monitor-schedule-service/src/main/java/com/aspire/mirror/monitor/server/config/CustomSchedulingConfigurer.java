package com.aspire.mirror.monitor.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * spring默认使用单线程调度 @Scheduled方法, 重写使用多线程 <br/>
 * Project Name:index-proxy 
 * File Name:CustomSchedulingConfigurer.java 
 * Package Name:com.aspire.mirror.zabbixintegrate.config 
 * ClassName: CustomSchedulingConfigurer <br/>
 * date: 2018年10月10日 下午7:56:17 <br/>
 * 
 * @author pengguihua
 * @version
 * @since JDK 1.6
 */
@Configuration
public class CustomSchedulingConfigurer implements SchedulingConfigurer {
	@Value("${spring.schedule.poolsize:10}")
	private Integer schedulePoolSize;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler());
	}

	@Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(schedulePoolSize);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
