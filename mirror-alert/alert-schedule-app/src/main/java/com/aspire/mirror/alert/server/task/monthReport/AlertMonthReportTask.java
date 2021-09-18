package com.aspire.mirror.alert.server.task.monthReport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.mirror.alert.server.biz.monthReport.AlertMonthDayBiz;
import com.aspire.mirror.alert.server.util.DateUtils;

@EnableScheduling
@Component
@ConditionalOnProperty(value = "monthReportTask.flag", havingValue = "normal")
public class AlertMonthReportTask {

	private static Logger logger = Logger.getLogger(AlertMonthReportTask.class);
	

	
	@Autowired
	private AlertMonthDayBiz alertMonthDayBiz;
	
	
	/**
	 * 同步租户利用率
	 * @throws Exception 
	 */
	@Scheduled(cron = "${monthReportTask.bizTypeCron:0 20 2 1 * ?}")
	public void syncMonthReportDepartmentData() throws Exception {
		logger.info("synchronization-syncMonthReportDepartmentData-begin*************");
			alertMonthDayBiz.bizSystemMonthData();
		 logger.info("synchronization-syncMonthReportDepartmentData-end************");
		
	}
	
	@Scheduled(cron = "${monthReportTask.exportBizSystemMonthExcelCron:0 20 2 1 * ?}")
	public void exportTemp() throws Exception {
		String month = DateUtils.getLastMonth(null);
		alertMonthDayBiz.getData(month, null, 0);
	}
	
	
	
	/**
	 * 同步网络资源利用率
	 * @throws Exception 
	 */
	@Scheduled(cron = "${monthReportTask.netTypeCron:0 50 2 1 * ?}")
	public void syncNetMonitorData() throws Exception {
		logger.info("synchronization-syncNetMonitorData-begin****************");
		alertMonthDayBiz.netMonthData(null);
		 logger.info("synchronization-syncNetMonitorData-end************");
		
	}
	
	
	/**
	 * 同步资源利用率
	 * @throws Exception 
	 */
	@Scheduled(cron = "${monthReportTask.IdcTypeCron:0 50 1 1 * ?}")
	public void syncMonthReportIdcTypeData() throws Exception {
		logger.info("synchronization-syncMonthReportIdcTypeData-begin******************");
		alertMonthDayBiz.IdcTypeMonthData(null);
		 logger.info("synchronization-AlertMonthReportTaskbegin-end*****************");
		
	}
	
	/**
	 * 同步资源利用率
	 * @throws Exception 
	 */
	@Scheduled(cron = "${monthReportTask.IdcType2Cron:0 10 4 1 * ?}")
	public void syncMonthReportIdcTypeData2() throws Exception {
		logger.info("synchronization-syncMonthReportIdcTypeData2-begin******************");
		alertMonthDayBiz.IdcTypeMonthData2(0,null);
		alertMonthDayBiz.IdcTypeMonthData2(1,null);
		alertMonthDayBiz.IdcTypeMonthData2(2,null);
		alertMonthDayBiz.IdcTypeMonthData2(3,null);
		alertMonthDayBiz.IdcTypeMonthData2(4,null);
		alertMonthDayBiz.IdcTypeMonthData2(5,null);
		alertMonthDayBiz.phyMonthData(6,null);
		 logger.info("synchronization-syncMonthReportIdcTypeData2-end*****************");
		
	}
	
	
	@Scheduled(cron = "${monthReportTask.department2Cron:0 50 3 1 * ?}")
	public void syncMonthDepartment2() throws Exception {
		logger.info("synchronization-syncMonthDepartment2-begin******************");
		alertMonthDayBiz.syncDepartment2Data(1,null);
		alertMonthDayBiz.syncDepartment2Data(2,null);
		alertMonthDayBiz.syncBizSystem2Data(3,null);
		 logger.info("synchronization-syncMonthDepartment2-end*****************");
		
	}
	
	/**
	 * 同步资源利用率 废弃不用了
	 * @throws Exception 
	 */
	//@Scheduled(cron = "${monthReportTask.exportBizSystemMonthExcelCron:0 0 3 1 * ?}")
	public void exportBizSystemMonthExcel() throws Exception {
		logger.info("synchronization-exportBizSystemMonthExcel-begin******************");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date startTime = calendar.getTime();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String month = sdf1.format(startTime);
		alertMonthDayBiz.exportBizSystemMonthExcel(month, null,0);
		 logger.info("synchronization-exportBizSystemMonthExcel-end*****************");
		
	}
	
	/**
	 * 同步资源利用率
	 * @throws Exception 
	 */
	//@Scheduled(cron = "${monthReportTask.exportBizSystemMonthByMinuteExcelCron:0 0 3 1 * ?}")
	public void exportBizSystemMonthByMinuteExcel() throws Exception {
		logger.info("synchronization-exportBizSystemMonthExcel-begin******************");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date startTime = calendar.getTime();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String month = sdf1.format(startTime);
		alertMonthDayBiz.exportBizSystemMonthExcel(month, null,1);
		 logger.info("synchronization-exportBizSystemMonthExcel-end*****************");
		
	}
	
}
