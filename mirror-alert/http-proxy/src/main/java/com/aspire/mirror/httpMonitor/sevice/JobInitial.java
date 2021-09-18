package com.aspire.mirror.httpMonitor.sevice;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aspire.mirror.httpMonitor.common.MonitorConstant;
import com.aspire.mirror.httpMonitor.dao.MonitorHttpConfigDao;
import com.aspire.mirror.httpMonitor.dao.po.MonitorHttpConfig;
import com.aspire.mirror.httpMonitor.dao.po.MonitorHttpReq;

@Component
public class JobInitial {
	@Value("${idcType}")
	private String defaultIdcType ;
	
	@Value("${isIntranet}")
    private int defaultIsIntranet ;
    @Autowired
    private Scheduler scheduler;
    
    @Autowired
	private MonitorHttpConfigDao monitorHttpConfigDao;
	// LoadProperties pro = new LoadProperties();

	private static final Logger logger = LoggerFactory.getLogger(JobInitial.class);
	
	@PostConstruct
	void initJob() throws SchedulerException {
		MonitorHttpReq pageRequset = new MonitorHttpReq();
		pageRequset.setStatus(1);
		pageRequset.setIsIntranet(defaultIsIntranet);
		if(defaultIsIntranet == 1) {
			pageRequset.setIdcType(defaultIdcType);
		}
    	List<MonitorHttpConfig> configs = monitorHttpConfigDao.pageList(pageRequset);
    	for(MonitorHttpConfig config:configs) {
    		//根据内网外和资源池过滤监控数据
			/*
			 * int isIntranet = config.getIsIntranet(); if (isIntranet == defaultIsIntranet)
			 * { if(isIntranet == 1) { if(!config.getIdcType().equals(defaultIdcType)) {
			 * continue; } } }else { continue; }
			 */
    		try {
                String configId = config.getId().toString();
                String moniName = config.getMonitor_name();
                String jobName = configId + "::" + moniName;
                //removeJob(jobName);
                //判断数据的状态
				/*
				 * if(config.getStatus() == 0) { continue;//可以暂停吗TODO }
				 */
                JobDetail jobDetail = JobBuilder.newJob(MonitorJobImpl.class)
                        .withIdentity(jobName, MonitorConstant.MONITOR_JOB_GROUP_NAME).build();
                jobDetail.getJobDataMap().put("monitorId", config.getId());

                SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobName, MonitorConstant.MONITOR_TRIGGER_GROUP_NAME).startAt(new Date())
                        .withSchedule(simpleSchedule().withIntervalInSeconds(config.getTest_period()*60)
                                .repeatForever())
                        .build();
                scheduler.scheduleJob(jobDetail, simpleTrigger);
                logger.info("scheduleJob"+jobName);
                // 启动
                if (!scheduler.isShutdown()) {
                    scheduler.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    	}
    }
}
