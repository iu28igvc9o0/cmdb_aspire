package com.aspire.mirror.alert.server.task.monthReport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.mirror.alert.server.biz.monthReport.AlertMonthDayBiz;
import com.aspire.mirror.alert.server.clientservice.CmdbClient;
import com.aspire.mirror.alert.server.clientservice.payload.ConfigDict;
import com.aspire.mirror.alert.server.vo.monthReport.AlertMonthReportVo;

@EnableScheduling
@Component
@ConditionalOnProperty(value = "AlertMonthReportNewDayTask.flag", havingValue = "normal")
public class AlertMonthReportNewDayTask {

	@Autowired
	private AlertMonthDayBiz alertMonthDayBiz;

	private static Logger logger = Logger.getLogger(AlertMonthReportNewDayTask.class);

	@Autowired
	private CmdbClient cmdbClient;
	
	
	

	@Scheduled(cron = "${AlertMonthReportNewDayTask.cron:0 30 0 * * ?}")
	public void syncData() throws Exception {
		logger.info("*AlertMonthReportNewDayTask-syncData-begin**************");
		// List<Map<String,Object>> confgig =
		// alertMonthReportSyncDao.queryDaysConfig("day");
		List<ConfigDict> idcTypeList = cmdbClient.getDictsByType("idcType", null, null, null);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date startTime = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String days = sdf.format(startTime);
		for (ConfigDict c : idcTypeList) {
			String idcType = c.getValue();
			AlertMonthReportVo monthReportRequest = new AlertMonthReportVo();
			monthReportRequest.setIdcType(idcType);
			monthReportRequest.setDeviceTypeNull(false);
			monthReportRequest.setDays(days);
			/*
			 * if(null!=c.get("pod")) { monthReportRequest.setPod(c.get("pod").toString());
			 * }
			 */
			alertMonthDayBiz.syncBizSytemDayNew(monthReportRequest);
		}
		logger.info("*AlertMonthReportNewDayTask-syncData-end**************");
	}
	
	//直真p1.2：3/4接口
	@Scheduled(cron = "${AlertMonthReportNewDayTask.syncDataDeviceTypeAll:0 30 0 * * ?}")
	public void syncDataDeviceTypeAll() throws Exception {
		logger.info("*AlertMonthReportNewDayTask-syncDataDeviceTypeAll-begin**************");
		// List<Map<String,Object>> confgig =
		// alertMonthReportSyncDao.queryDaysConfig("day");
		List<ConfigDict> idcTypeList = cmdbClient.getDictsByType("idcType", null, null, null);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date startTime = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String days = sdf.format(startTime);
		for (ConfigDict c : idcTypeList) {
			String idcType = c.getValue();
			AlertMonthReportVo monthReportRequest = new AlertMonthReportVo();
			//monthReportRequest.setDepartment1(IT_COMPANY_INTERNAL);
			monthReportRequest.setDeviceTypeNull(true);
			monthReportRequest.setIdcType(idcType);
			monthReportRequest.setDays(days);
			/*
			 * if(null!=c.get("pod")) { monthReportRequest.setPod(c.get("pod").toString());
			 * }
			 */
			alertMonthDayBiz.syncBizSytemDayNew(monthReportRequest);
		}
		logger.info("*AlertMonthReportNewDayTask-syncDataDeviceTypeAll-end**************");
	}
	

	
	//@Scheduled(cron = "${AlertMonthReportNewDayTask.syncBizSystemByMinuteDataCron:0 30 0 * * ?}")
	public void syncBizSystemByMinuteData() throws Exception {
		logger.info("*AlertMonthReportNewDayTask-syncBizSystemByMinuteData-begin**************");
		// List<Map<String,Object>> confgig =
		// alertMonthReportSyncDao.queryDaysConfig("day");
		List<ConfigDict> idcTypeList = cmdbClient.getDictsByType("idcType", null, null, null);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date startTime = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String days = sdf.format(startTime);
		for (ConfigDict c : idcTypeList) {
			String idcType = c.getValue();
			AlertMonthReportVo monthReportRequest = new AlertMonthReportVo();
			monthReportRequest.setIdcType(idcType);
			monthReportRequest.setDays(days);
			/*
			 * if(null!=c.get("pod")) { monthReportRequest.setPod(c.get("pod").toString());
			 * }
			 */
			alertMonthDayBiz.syncBizSytemDayByMinite(monthReportRequest);
		}
		logger.info("*AlertMonthReportNewDayTask-syncBizSystemByMinuteData-end**************");
	}
	
	@Scheduled(cron = "${AlertMonthReportNewDayTask.idcTypeCron:0 50 0 * * ?}")
	public void syncIdcTypeData() throws Exception {
		logger.info("*AlertMonthReportNewDayTask-syncIdcTypeData-begin**************");
		// List<Map<String,Object>> confgig =
		// alertMonthReportSyncDao.queryDaysConfig("day");

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date startTime = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String days = sdf.format(startTime);

			AlertMonthReportVo monthReportRequest = new AlertMonthReportVo();
			monthReportRequest.setDays(days);
			/*
			 * if(null!=c.get("pod")) { monthReportRequest.setPod(c.get("pod").toString());
			 * }
			 */
			alertMonthDayBiz.syncIdcTypeDayNew(monthReportRequest);
		logger.info("*AlertMonthReportNewDayTask-syncIdcTypeData-end**************");
	}
	//导出租户利用率日报
	@Scheduled(cron = "${AlertMonthReportNewDayTask.exportDayExcelCron:0 10 2 * * ?}")
	public void exportDayExcel() throws Exception {
		
		logger.info("**exportDayExcel--begin**************");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date startTime = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String days = sdf.format(startTime);
		
		alertMonthDayBiz.exportBizSytemDayExcel(days,0);
		logger.info("******exportDayExcel--end**************");
	}
	//导出设备利用率日报
	@Scheduled(cron = "${AlertMonthReportNewDayTask.exportIdcTypeIpDayExcelCron:0 40 2 * * ?}")
	public void exportIdcTypeIpDayExcel() throws Exception {
		
		logger.info("**exportIdcTypeIpDayExcel--begin**************");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date startTime = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String days = sdf.format(startTime);
		
		alertMonthDayBiz.exportIdcTypeIpDayExcel(days);
		logger.info("******exportIdcTypeIpDayExcel--end**************");
	}
	
	
	//导出租户利用率日报(分钟粒度)
		//@Scheduled(cron = "${AlertMonthReportNewDayTask.exportDayByminuteExcel:0 10 2 * * ?}")
		public void exportDayByminuteExcel() throws Exception {
			
			logger.info("**exportDayByminuteExcel--begin**************");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			Date startTime = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String days = sdf.format(startTime);
			
			alertMonthDayBiz.exportBizSytemDayExcel(days,1);
			logger.info("******exportDayByminuteExcel--end**************");
		}

}
