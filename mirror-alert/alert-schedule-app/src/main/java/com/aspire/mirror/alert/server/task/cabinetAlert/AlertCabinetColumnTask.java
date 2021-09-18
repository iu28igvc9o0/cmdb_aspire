package com.aspire.mirror.alert.server.task.cabinetAlert;

import com.aspire.mirror.alert.server.biz.cabinetAlert.AlertCabinetColumnBiz;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@ConditionalOnProperty(value = "AlertCabinetColumnTask.flag", havingValue = "normal")
public class AlertCabinetColumnTask {

	private static final Logger LOGGER = Logger.getLogger(AlertCabinetColumnTask.class);

	@Autowired
	private AlertCabinetColumnBiz cabinetColumnBiz;
	
	/**
	 *初始化列头柜配置数据定时任务
	 * @throws Exception 
	 */
	@Scheduled(cron = "${AlertCabinetColumnTask.cron:0 45 22 * * ?}")
	public void syncAlertData() throws Exception {
		LOGGER.info("AlertCabinetColumnTask_begin*****************************");

		cabinetColumnBiz.CabinetColumnTask();
		LOGGER.info("AlertCabinetColumnTask_end*****************************");
		
	}
	
	
	
}
