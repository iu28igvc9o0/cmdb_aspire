package com.aspire.mirror.alert.server.task.monthReport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.mirror.alert.server.biz.monthReport.AlertPoorEfficiencyDeviceBiz;

/**
 * 
 * @author lf
 *低利用率设备性能
 */
@EnableScheduling
@Component
@ConditionalOnProperty(value = "AlertPoorEfficiencyDeviceTask.flag", havingValue = "normal")
public class AlertPoorEfficiencyDeviceTask {

	private static Logger logger = Logger.getLogger(AlertMonthReportNewDayTask.class);

	
	
	
	@Autowired
	private AlertPoorEfficiencyDeviceBiz alertPoorEfficiencyDeviceBiz;
	
	/**
	 * 扫描前一天没有监控数据的设备，然后消除上次已经有数据的告警
	 * 
	 * @throws Exception
	 */
	
	@Scheduled(cron = "${AlertPoorEfficiencyDeviceTask.weekJob:0 10 1 1 * ?}")
	public void weekJon() throws Exception {
		logger.info("**weekJon--begin**************");
		alertPoorEfficiencyDeviceBiz.getPoorEfficiencyDeviceWeekData("cpu",0);
		alertPoorEfficiencyDeviceBiz.getPoorEfficiencyDeviceWeekData("memory",0);
		logger.info("******weekJon--end**************");
	}
	
	
	/**
	 * 资源池的设备性能
	 * @throws Exception
	 */
	@Scheduled(cron = "${AlertPoorEfficiencyDeviceTask.monthJob:0 0 4 * * ?}")
	public void monthJob() throws Exception {
		logger.info("**monthJob--begin**************");
		alertPoorEfficiencyDeviceBiz.getPoorEfficiencyDeviceMonthData("cpu",0);
		alertPoorEfficiencyDeviceBiz.getPoorEfficiencyDeviceMonthData("memory",0);
		logger.info("******monthJob--end**************");
	}


}
