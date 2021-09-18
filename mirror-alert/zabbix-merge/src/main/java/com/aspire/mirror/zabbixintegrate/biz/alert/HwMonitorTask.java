package com.aspire.mirror.zabbixintegrate.biz.alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.mirror.zabbixintegrate.bean.HwSyncLog;
import com.aspire.mirror.zabbixintegrate.biz.HwMonitorSyncService;
import com.aspire.mirror.zabbixintegrate.config.HWProperties;
import com.aspire.mirror.zabbixintegrate.daoCmdb.AlertRestfulDao;
import com.aspire.mirror.zabbixintegrate.util.DateUtil;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@Component
@ConditionalOnProperty(value = "HwMonitorTask.flag", havingValue = "normal")
public class HwMonitorTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(HwMonitorTask.class);
	@Value("${HwMonitorTask.moniotrConfigFlag:false}")
	private boolean moniotrConfigFlag;
	@Value("${HwMonitorTask.deviceFlag:false}")
	private boolean deviceFlag;
	@Value("${HwMonitorTask.monitorDataFlag:false}")
	private boolean monitorDataFlag;
	@Value("${HwMonitorTask.reMonitorDataFlag:false}")
	private volatile static boolean reMonitorDataFlag;

	private volatile static Map<String,Map<String, String>> reDateMap = Maps.newLinkedHashMap();

	@Autowired
	private AlertRestfulDao alertRestfulDao;

	@Autowired
	private HwMonitorSyncService hwMonitorSyncService;
	@Autowired
	private HWProperties hWProperties;
	 private int tag_type ;
	 private String idcTypeTag ;
	 @PostConstruct
 	private void init(){
		 tag_type = hWProperties.getTag_type();
		 idcTypeTag = hWProperties.getIdcType()+"_"+tag_type;
 	}
	
	@Scheduled(cron = "${HwMonitorTask.scanMoniotrConfig:0 10 1 1 * ?}")
	public void scanMoniotrConfig() throws Exception {
		log.info("**scanMoniotrConfig--begin**************");
		if (moniotrConfigFlag) {
			HwSyncLog syncLog = new HwSyncLog();// 记录日志
			try {
				formLog(syncLog, null, "scanMoniotrConfig");// 设置日志信息
				hwMonitorSyncService.syncMonitorConfigData();
			} catch (Exception e) {
				log.error("scanMoniotrConfig报错",e);
				syncLog.setStatusFail();
				String message = e.getMessage()==null?"":e.getMessage().toString();
				syncLog.setContent(e.getClass().getName() + ":" + message);
			}finally {
				syncLog.setCreateTime(new Date());
				alertRestfulDao.insertHwSyncLog(syncLog);
			}
		}

		log.info("******scanMoniotrConfig--end**************");
	}

	@Scheduled(cron = "${HwMonitorTask.scanDeviceDatas:0 10 1 1 * ?}")
	public void scanDeviceDatas() throws Exception {
		log.info("**scanDeviceDatas--begin**************");
		if (deviceFlag) {
			HwSyncLog syncLog = new HwSyncLog();// 记录日志
			try {
				formLog(syncLog, null, "scanDeviceDatas");// 设置日志信息
				hwMonitorSyncService.syncDeviceData();
			} catch (Exception e) {
				log.error("scanDeviceDatas报错",e);
				syncLog.setStatusFail();
				String message = e.getMessage()==null?"":e.getMessage().toString();
				syncLog.setContent(e.getClass().getName() + ":" + message);
			}finally {
				syncLog.setCreateTime(new Date());
				alertRestfulDao.insertHwSyncLog(syncLog);
			}
		}

		log.info("******scanDeviceDatas--end**************");
	}

	@Scheduled(cron = "${HwMonitorTask.scanMonitorDatas:0 1 1 * * ?}")
	public void scanMonitorDatas() throws Exception {
		log.info("**scanMonitorDatas--begin**************");
		if (monitorDataFlag) {
			HwSyncLog syncLog = new HwSyncLog();// 记录日志
			// 查询指标详情
			try {
				formLog(syncLog, null, "scanMonitorDatas");// 设置日志信息
				HwSyncLog hwLog = alertRestfulDao.getHwSyncLog(this.idcTypeTag, "scanMonitorDatas");
				String toTime = null;
				if(null!=hwLog) {
					toTime = hwLog.getToTime();
				}
				List<Date> dateList = DateUtil.getExecDuration(hWProperties.getIntervalMinute(), hWProperties.getDelayMinute(), hWProperties.getIntervalMinute());
				Date endTime = dateList.get(1);
				Date startTime = dateList.get(0);
				String startTimeStr = DateUtil.format(startTime, DateUtil.DEFAULT_DATETIME_FMT);
				if (null!=toTime && !toTime.equals(startTimeStr)) {
					
					if(reDateMap.containsKey(toTime)) {
						Map<String, String> d = reDateMap.get(toTime);
						d.put("endTime", startTimeStr);
						reDateMap.put(toTime, d);
					}else {
						Map<String, String> dateMap = Maps.newHashMap();
						dateMap.put("startTime", toTime);
						dateMap.put("endTime", startTimeStr);
						long du = endTime.getTime()+hWProperties.getSyncMinute()*60*1000l;
						Date syacDate = new Date(du);
						String syacDateStr = DateUtil.format(syacDate, DateUtil.DEFAULT_DATETIME_FMT);
						dateMap.put("syncTime", syacDateStr);//默认间隔12个小时再跑一次
						reDateMap.put(toTime, dateMap);
						//reDateList.add(dateMap);
					}
						
					
					reMonitorDataFlag = true;
				}
				hwMonitorSyncService.syncMonitorDatas(startTime, endTime);

				syncLog.setFromTime(startTimeStr);
				syncLog.setToTime(DateUtil.format(endTime, DateUtil.DEFAULT_DATETIME_FMT));
			} catch (Exception e) {
				log.error("scanMonitorDatas报错",e);
				syncLog.setStatusFail();
				String message = e.getMessage()==null?"":e.getMessage().toString();
				syncLog.setContent(e.getClass().getName() + ":" + message);
				
			}finally {
				syncLog.setCreateTime(new Date());
				alertRestfulDao.insertHwSyncLog(syncLog);
			}
		}

		log.info("******scanMonitorDatas--end**************");
	}

	private void formLog(HwSyncLog syncLog, String url, String config_type) {
		syncLog.setUrl(url);
		syncLog.setConfigType(config_type);
		syncLog.setIdcTypeTag(this.idcTypeTag);
		syncLog.setExecTime(new Date());
	}

	@Scheduled(cron = "${HwMonitorTask.reScanMonitorDatas:0 */2 * * * ?}")
	public void reScanMonitorDatas() throws Exception {
		
		if (reMonitorDataFlag) {
			log.info("**reScanMonitorDatas--begin**************");
			if (reDateMap.size() > 0) {
				List<Map<String, String>> reDateList= new ArrayList(reDateMap.values());
				HwSyncLog syncLog = new HwSyncLog();// 记录日志
				for (int i = 0; i < reDateList.size(); i++) {
					try {
						formLog(syncLog, null, "reScanMonitorDatas");// 设置日志信息
						Map<String, String> dateMap = reDateList.get(i);
						String startTime = dateMap.get("startTime");
						String endTime = dateMap.get("endTime");
						Date start = DateUtil.parse(startTime, DateUtil.DEFAULT_DATETIME_FMT);
						Date end = DateUtil.parse(endTime, DateUtil.DEFAULT_DATETIME_FMT);
						
						if(start.getTime()>=end.getTime()) {//开始大于等于结束
							reDateMap.remove(startTime);
							continue;
						}
						
						String syacDateStr = dateMap.get("syncTime");
						if(StringUtils.isNotBlank(syacDateStr)) {
							Date curDate = new Date();
							Date syacDate = DateUtil.parse(syacDateStr, DateUtil.DEFAULT_DATETIME_FMT);
							if(curDate.getTime()<syacDate.getTime()) {//一般隔12个小时在跑
								continue;
							}
						}
						
						long duration = hWProperties.getReMinute() * 60 * 1000;
						
						Date endTemp = new Date(start.getTime() + duration);
						if (endTemp.compareTo(end) >= 0) {
							endTemp = end;
						}
						String time = DateUtil.format(endTemp, DateUtil.DEFAULT_DATETIME_FMT);
						dateMap.put("startTime", time);
						reDateMap.put(time, reDateMap.remove(startTime));
						hwMonitorSyncService.syncMonitorDatas(start, endTemp);
						
						syncLog.setFromTime(startTime);
						syncLog.setToTime(time);
						
						
					} catch (Exception e) {
						log.error("reScanMonitorDatas报错",e);
						syncLog.setStatusFail();
						String message = e.getMessage()==null?"":e.getMessage().toString();
						syncLog.setContent(e.getClass().getName() + ":" + message);
					}finally {
						if(null!=syncLog.getFromTime()) {
							syncLog.setCreateTime(new Date());
							alertRestfulDao.insertHwSyncLog(syncLog);
						}
						
					}

				}
				//reDateList.clear();
				//reMonitorDataFlag = false;

			} else {
				reMonitorDataFlag = false;
			}
			log.info("******reScanMonitorDatas--end**************");
		}
		
	}
	
	public static void setReScanMonitorTime(boolean flag,String startTime,String endTime) {
		reMonitorDataFlag = flag;
		if(reDateMap.containsKey(startTime)) {
			Map<String, String> d = reDateMap.get(startTime);
			d.put("endTime", endTime);
			reDateMap.put(startTime, d);
		}else {
			Map<String, String> dateMap = Maps.newHashMap();
			dateMap.put("startTime", startTime);
			dateMap.put("endTime", endTime);
			reDateMap.put("syncTime", dateMap);
		}
		
	}

}
