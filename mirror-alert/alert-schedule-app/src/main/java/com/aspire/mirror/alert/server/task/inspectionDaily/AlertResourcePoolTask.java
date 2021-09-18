package com.aspire.mirror.alert.server.task.inspectionDaily;

import com.aspire.mirror.alert.server.biz.inspectionDaily.AlertResourcePoolBiz;
import com.aspire.mirror.alert.server.clientservice.CmdbClient;
import com.aspire.mirror.alert.server.clientservice.CommonServiceClient;
import com.aspire.mirror.alert.server.clientservice.payload.ConfigDict;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.vo.inspectionDaily.AlertRourcePoolVo;
import com.aspire.mirror.common.constant.SystemConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@EnableScheduling
@Component
public class AlertResourcePoolTask {

	private static final Logger LOGGER = Logger.getLogger(AlertResourcePoolTask.class);
	@Autowired
	private AlertResourcePoolBiz alertResourcePoolBiz;
	 @Autowired
	private CmdbClient  cmdbClient;

	@Autowired
	private CommonServiceClient commonServiceClient;

	@Value("${systemType:simple}")
	private String systemType;

	/**
	 * 告警自动化派单定时任务
	 */
//	@Scheduled(cron = "${alertResourcePoolTask.cron:0 0 1 1 * ?}")
	public void syncAlertData() {
		LOGGER.info("---------------告警自动化定时任务开始AlertResourcePoolTask-start start---------------");
		AlertRourcePoolVo pageRequset = new AlertRourcePoolVo();
		
		//获取上个月
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		Date m = c.getTime();
		String mon = format.format(m);
		pageRequset.setMonth(mon);
		setDeviceType(pageRequset);
		
		//pageRequset.setDeviceDictType(AlertCommonConstant.DEVICE_DICT_TYPE);
		pageRequset.setIdcType(alertResourcePoolBiz.getDefaultIdcType());
		
		//如果有上个月数据 清空 
		trucatData(mon);
		
		LOGGER.info("---------------统计资源池告警总数-start start---------------");
   	 	alertResourcePoolBiz.syncAlertTotal(pageRequset);
   	 LOGGER.info("---------------统计资源池告警总数-end end---------------");
		
   	LOGGER.info("---------------统计资源池告警分配-start start---------------");
		alertResourcePoolBiz.syncDistributionAlert(pageRequset);
		LOGGER.info("---------------统计资源池告警分配-end end---------------");
		
		LOGGER.info("---------------统计资源池监控对象top10-start start---------------");
		alertResourcePoolBiz.syncMoniterTop10Alert(pageRequset);
		LOGGER.info("---------------统计资源池监控对象top10-end end---------------");
		
		LOGGER.info("---------------统计资源池设备分类top10-start start---------------");
		alertResourcePoolBiz.syncDeviceTop10Alert(pageRequset);
		LOGGER.info("---------------统计资源池设备分类top10-end end---------------");
		
		LOGGER.info("---------------告警自动化定时任务开始AlertResourcePoolTask-end end---------------");
	}
	
	void trucatData(String month){
		alertResourcePoolBiz.deleteCountByMonth(month);
		alertResourcePoolBiz.deleteDeviceByMonth(month);
		alertResourcePoolBiz.deleteMoniterByMonth(month);
		alertResourcePoolBiz.deleteRecordByMonth(month);
	}

	void setDeviceType(AlertRourcePoolVo pageRequset){
		pageRequset.setPhysicServer(AlertCommonConstant.PHYSICSERVER_DEVICETYPE);
		pageRequset.setPhysicserverPrometheus(AlertCommonConstant.PHYSICSERVER_PROMETHEUS);
		pageRequset.setPhysicserverZabbix(AlertCommonConstant.PHYSICSERVER_ZABBIX);

		List<ConfigDict> prometheus;
		if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
			prometheus = commonServiceClient.getDictsByType(AlertCommonConstant.PHYSICSERVER_SOURCE_PROMETHEUS, null, null, null);
		} else {
			prometheus = cmdbClient.getDictsByType(AlertCommonConstant.PHYSICSERVER_SOURCE_PROMETHEUS, null, null, null);
		}

		if(null !=prometheus && prometheus.size()>0) {
			pageRequset.setSourcePrometheus(prometheus.get(0).getValue());
		}
		List<ConfigDict> zabbix;
		if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
			zabbix = commonServiceClient.getDictsByType(AlertCommonConstant.PHYSICSERVER_SOURCE_ZABBIX, null, null, null);
		} else {
			zabbix = cmdbClient.getDictsByType(AlertCommonConstant.PHYSICSERVER_SOURCE_ZABBIX, null, null, null);
		}
		if(null !=prometheus && prometheus.size()>0) {
			pageRequset.setSourceZabbix(zabbix.get(0).getValue());
		}
		
		
		pageRequset.setNetworkType(AlertCommonConstant.NETWORK_DEVICETYPE);
		pageRequset.setOther(AlertCommonConstant.OTHER_DEVICETYPE);
	}
	
}
