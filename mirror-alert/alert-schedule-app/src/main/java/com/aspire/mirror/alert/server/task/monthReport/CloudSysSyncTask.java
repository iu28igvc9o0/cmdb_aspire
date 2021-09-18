package com.aspire.mirror.alert.server.task.monthReport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.aspire.mirror.alert.server.dao.monthReport.AlertMonthReportSyncDao;


@Component
@ConditionalOnProperty(value = "CloudSysSyncTask.flag", havingValue = "normal")
public class CloudSysSyncTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudSysSyncTask.class);


	@Autowired
	private CloudSysSyncKafkaTask cloudSysSyncKafkaTask;
	


	
	@Value("${CloudSysSyncTask.periodTime:120}")
	private int periodTime;
	
	@Value("${CloudSysSyncTask.waiteTime:10}")
	private int waiteTime;
	
	@Value("${CloudSysSyncTask.periodTime2:180}")
	private int periodTime2;

	@Value("${CloudSysSyncTask.waiteTime2:70}")
	private int waiteTime2;


	@Value("${CloudSysSyncTask.phyStartTimeConfig:20200401000000}")
	private String phyStartTimeConfig;

	@Value("${CloudSysSyncTask.phyEndTimeConfig:20200418235959}")
	private String phyEndTimeConfig;
	
	@Value("${CloudSysSyncTask.vmStartTimeConfig:20200401000000}")
	private String vmStartTimeConfig;

	@Value("${CloudSysSyncTask.vmEndTimeConfig:20200418235959}")
	private String vmEndTimeConfig;

	private boolean phyFlag = false;
	private boolean vmFlag = false;
	
	@Value("${CloudSysSyncTask.VMJobFlag:false}")
	private boolean  VMJobFlag;
	@Value("${CloudSysSyncTask.PHYJobFlag:false}")
	private boolean  PHYJobFlag;
	
	@Value("${CloudSysSyncTask.VMJobFlag2:false}")
	private boolean  VMJobFlag2;
	@Value("${CloudSysSyncTask.PHYJobFlag2:false}")
	private boolean  PHYJobFlag2;
	
	@Autowired
	private AlertMonthReportSyncDao alertMonthReportSyncDao;
	
	//@PostConstruct
		void initJob() {
		
		if(VMJobFlag) {
			LOGGER.info("*****initJobCloudSysSync--vmTaskBegin*********");
			ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
			 service.scheduleAtFixedRate(() -> {
			        try {
			        	getVmData();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			    }, this.waiteTime, this.periodTime, TimeUnit.SECONDS);
		
			 LOGGER.info("*****initJobCloudSysSync--vmTaskEnd*********");
		}
		if(PHYJobFlag) {
			LOGGER.info("****initJobCloudSysSync--phyTaskBegin*********");
			ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
			 service.scheduleAtFixedRate(() -> {
			        try {
			        	getPhyData();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			    }, this.waiteTime+40, this.periodTime, TimeUnit.SECONDS);
		
			 LOGGER.info("*********initJobCloudSysSync--phyTaskEnd*********");
		}
		
		if(PHYJobFlag2) {
			LOGGER.info("****initJobCloudSysSync--phyTask2Begin*********");
			ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
			service.scheduleAtFixedRate(() -> {
				try {
					getPhyData2();
					if (phyFlag) {
						service.shutdown();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}, this.waiteTime2+20,this.periodTime2, TimeUnit.SECONDS);
			LOGGER.info("*********initJobCloudSysSync--phyTaskEnd2*********");
		}
		if(VMJobFlag2) {
			LOGGER.info("*********initJobCloudSysSync--phyTask2Begin*********");
			ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
			service.scheduleAtFixedRate(() -> {
				try {
					getVMData2();
					if (vmFlag) {
						service.shutdown();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}, this.waiteTime2+60,this.periodTime2, TimeUnit.SECONDS);
			LOGGER.info("*********initJobCloudSysSync--phyTaskEnd2*********");
		}
		
	}
		


	public void getVmData() throws Exception {
		LOGGER.info("**CloudSysSyncTask-getVMData-begin*********");
		List<String> execList = cloudSysSyncKafkaTask.getExecDuration();
		String startStr =execList.get(0);
		String endStr = execList.get(1);
		Date start = new Date();
		
		Map<String,Object> map = alertMonthReportSyncDao.querySuyanConfigLogsByToTime(endStr,"云主机"
				,"success");
		if(null!=map && map.size()>0) {
			LOGGER.info("云主机CloudSysSyncTaskGetData--该时间段已经同步过了:{}-{}*",startStr,endStr);
			return;
		}
		cloudSysSyncKafkaTask.getData(startStr,endStr,"云主机","云主机",start);
		LOGGER.info("**CloudSysSyncTask-getVMData-end*********");
	}
	
	
	public void getPhyData() throws Exception {
		LOGGER.info("**CloudSysSyncTask-getPhyData-begin*********");
		List<String> execList = cloudSysSyncKafkaTask.getExecDuration();
		String startStr =execList.get(0);
		String endStr = execList.get(1);
		Date start = new Date();

		Map<String,Object> map = alertMonthReportSyncDao.querySuyanConfigLogsByToTime(endStr,"X86服务器"
				,"success");
		if(null!=map && map.size()>0) {
			LOGGER.info("*X86服务器CloudSysSyncTaskGetData--该时间段已经同步过了*");
			return;
		}
		cloudSysSyncKafkaTask.getData(startStr,endStr,"X86服务器","X86服务器",start);
		LOGGER.info("**CloudSysSyncTask-getPhyData-end*********");
	}
	
	public void getPhyData2() throws Exception {
		LOGGER.info("**CloudSysSyncTask-getPhyData2-begin*********");
		Date start = DateUtils.parseDate(phyStartTimeConfig, new String[] { "yyyyMMddHHmmss" });
		Date end = DateUtils.parseDate(phyEndTimeConfig, new String[] { "yyyyMMddHHmmss" });

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(Calendar.MINUTE, 10);
		Date endTime = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		String startStr = sdf.format(start);

		String endStr = sdf.format(endTime);
		cloudSysSyncKafkaTask.getData(startStr,endStr,"X86服务器","X86服务器2",new Date());
		if (endTime.getTime() >= end.getTime()) {
			this.phyFlag = true;
		}
		phyStartTimeConfig = endStr;
		LOGGER.info("**CloudSysSyncTask-getPhyData2-end*********");
	}
	
	public void getVMData2() throws Exception {
		LOGGER.info("**CloudSysSyncTask-getVMData2-begin*********");
		Date start = DateUtils.parseDate(vmStartTimeConfig, new String[] { "yyyyMMddHHmmss" });
		Date end = DateUtils.parseDate(vmEndTimeConfig, new String[] { "yyyyMMddHHmmss" });

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(Calendar.MINUTE, 10);
		Date endTime = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		String startStr = sdf.format(start);

		String endStr = sdf.format(endTime);
		cloudSysSyncKafkaTask.getData(startStr,endStr,"云主机","云主机2",new Date());
		if (endTime.getTime() >= end.getTime()) {
			this.vmFlag = true;
		}
		vmEndTimeConfig = endStr;
		LOGGER.info("**CloudSysSyncTask-getVMData2-End*********");
	}
	

}
