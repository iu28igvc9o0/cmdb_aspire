package com.aspire.mirror.alert.server.task.monthReport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.aspire.mirror.alert.server.dao.monthReport.AlertMonthReportSyncDao;

@EnableScheduling
@Component
@ConditionalOnProperty(value = "ReSyncCloudSysSyncTask.flag", havingValue = "normal")
public class ReSyncCloudSysSyncTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReSyncCloudSysSyncTask.class);


	@Autowired
	private CloudSysSyncKafkaTask cloudSysSyncKafkaTask;
	
	@Autowired
	private AlertMonthReportSyncDao alertMonthReportSyncDao;

	
	@Value("${ReSyncCloudSysSyncTask.syncDay:1}")
	private int syncDay;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	
	@Value("${ReSyncCloudSysSyncTask.vmFlag:true}")
	private boolean  VMFlag;
	@Value("${ReSyncCloudSysSyncTask.phyFlag:true}")
	private boolean  PHYFlag;

	//@Scheduled(cron = "${ReSyncCloudSysSyncTask.initVmJob:0 */2 * * * ?}")
	public	void initVmJob() {
		LOGGER.info("*****苏研数据补偿Vm*********");
		if(VMFlag) {
			LOGGER.info("*****苏研数据补偿--initVmJobBegin*********");
			
			Runnable runnable = new Runnable() {
				public void run() {
					 try {
						 getReData("云主机");
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				}
			};
			taskExecutor.execute(runnable);
			 LOGGER.info("*****苏研数据补偿--initVmJobEnd*********");
		}
		
	}
	
	
	@Scheduled(cron = "${ReSyncCloudSysSyncTask.initPhyJob:0 30 0 * * ?}")
	public void initPhyJob() {
		LOGGER.info("*****苏研数据补偿Phy*********");
	if(PHYFlag) {
		LOGGER.info("****苏研数据补偿--phyTaskBegin*********");
		
		Runnable runnable = new Runnable() {
			public void run() {
				 try {
					 getReData("X86服务器");
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			}
		};
		taskExecutor.execute(runnable);
	
		 LOGGER.info("***苏研数据补偿--phyTaskEnd*********");
	}

	
}
		


	public void getReData(String deviceType) throws Exception {
		LOGGER.info("{}苏研数据补偿-getReData-begin**",deviceType);
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		 calendar.set(Calendar.HOUR_OF_DAY, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         Date end = calendar.getTime();
		calendar.add(Calendar.DATE, -syncDay);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
         Date start = calendar.getTime();
         long period = 10*60*1000;
         String startTime = sdf.format(start);
         String endTime = sdf.format(end);
         List<String> timeList =  alertMonthReportSyncDao.querySuyanConfigLogsByTime(startTime, endTime
        		 ,deviceType,"success");
        for(long i=start.getTime();i<end.getTime();i+=period) {
        	Date start1 = new Date(i);
        	String timeStr = sdf.format(start1);
        	if(timeList.contains(timeStr)) {
        		continue;
        	}
        	calendar.setTime(start1);
        	calendar.add(Calendar.MINUTE, 10);
        	Date end1 = calendar.getTime();
        	cloudSysSyncKafkaTask.getData(timeStr, sdf.format(end1), deviceType, "re"+deviceType, new Date());
        }
       
		LOGGER.info("{}苏研数据补偿-getReData-end**",deviceType);
	}
	
	
	
	
	

}
