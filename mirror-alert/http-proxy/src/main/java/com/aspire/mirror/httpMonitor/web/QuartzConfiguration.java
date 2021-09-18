package com.aspire.mirror.httpMonitor.web;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.aspire.mirror.httpMonitor.sevice.JobFactory;

@Configuration
public class QuartzConfiguration {

	private JobFactory jobFactory;
	
	public QuartzConfiguration(JobFactory jobFactory){
        this.jobFactory = jobFactory;
    }
	
	 @Bean
	    public SchedulerFactoryBean schedulerFactoryBean() {
	        // Spring提供SchedulerFactoryBean为Scheduler提供配置信息,并被Spring容器管理其生命周期
	        SchedulerFactoryBean factory = new SchedulerFactoryBean();
	        // 设置自定义Job Factory，用于Spring管理Job bean
	        factory.setJobFactory(jobFactory);
	        return factory;
	    }
	 /**
     * 创建调度工厂并返回
     * @return
	 * @throws SchedulerException 
     */
    @Bean("scheduler")
    public Scheduler createSchedulerFactory() throws SchedulerException{
		/*
		 * DirectSchedulerFactory schedulerFactory =
		 * DirectSchedulerFactory.getInstance(); // 表示以3个工作线程初始化工厂
		 * schedulerFactory.createVolatileScheduler(3); Scheduler scheduler =
		 * schedulerFactory.getScheduler(); return scheduler;
		 */
    	return schedulerFactoryBean().getScheduler();
    }
}
