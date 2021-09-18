package com.aspire.mirror.alert.server.task.monthReport;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.mirror.alert.server.biz.monthReport.AlertMonthDayBiz;
import com.aspire.mirror.alert.server.clientservice.CmdbClient;
import com.aspire.mirror.alert.server.clientservice.payload.ConfigDict;

@EnableScheduling
@Component
@ConditionalOnProperty(value = "AlertMonitorScanDayTask.flag", havingValue = "normal")
public class AlertMonitorScanDayTask {

	private static Logger logger = Logger.getLogger(AlertMonitorScanDayTask.class);
	@Autowired
	private AlertMonthDayBiz  alertMonthDayBiz;
	
	@Autowired
	private CmdbClient cmdbClient;

	@Value("${AlertMonitorScanDayTask.scanDeviceMonitorDataFlag:false}")
	private boolean scanDeviceMonitorDataFlag;
	
	
	/**
	 * 扫描前一天没有监控数据的设备，然后消除上次已经有数据的告警
	 * 
	 * @throws Exception
	 */
	
	@Scheduled(cron = "${AlertMonitorScanDayTask.scanDeviceMonitorData:0 10 1 1 * ?}")
	public void scanDeviceMonitorData() throws Exception {
		logger.info("**scanDeviceMonitorData--begin**************");
		if(scanDeviceMonitorDataFlag) {
			 List<ConfigDict> idcTypeList = cmdbClient.getDictsByType("idcType", null, null, null);
			 for(ConfigDict c:idcTypeList) {
				 alertMonthDayBiz.scanDeviceMonitorData(c.getId(),c.getValue(),null);
			 }
		}
		 
		logger.info("******scanDeviceMonitorData--end**************");
	}
	
	
	/**
	 * 资源池的设备性能
	 * @throws Exception
	 */
	@Scheduled(cron = "${AlertMonitorScanDayTask.scanIdcTypePerformanceData:0 10 2 * * ?}")
	public void scanIdcTypePerformanceData() throws Exception {
		logger.info("**scanIdcTypePerformanceData--begin**************");
		 List<ConfigDict> idcTypeList = cmdbClient.getDictsByType("idcType", null, null, null);
		 for(ConfigDict c:idcTypeList) {
			alertMonthDayBiz.scanIdcTypePerformanceData(c.getId(),c.getValue(),null);
		 }
		 alertMonthDayBiz.scanIdcTypePerformanceData("all",null,null);
		logger.info("******scanIdcTypePerformanceData--end**************");
	}
	

}
