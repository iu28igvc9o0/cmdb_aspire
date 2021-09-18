package com.aspire.mirror.zabbixintegrate.biz.alert;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.mirror.zabbixintegrate.domain.AlertModel;
import com.aspire.mirror.zabbixintegrate.util.AppConfigHelper;
import com.aspire.mirror.zabbixintegrate.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
* 告警扫描任务    <br/>
* Project Name:zabbix-integrate
* File Name:AlertScanJob.java
* Package Name:com.aspire.mirror.zabbixintegrate.biz
* ClassName: AlertScanJob <br/>
* date: 2018年10月19日 上午10:42:09 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Component
class AlertScanJob {
	@Value("${alertScan.batchCount}")
	private Integer							batchCount;
	private Integer[]						actionIdArr;
	@Value("${alertScan.centerRoom.kafka.topicName:TOPIC_SYSTEM_ALERTS}")
	private String							topicName;
	@Value("${alertScan.centerRoom.kafka.partionsCount}")
	private Integer							topicPartionsCount;

	@Autowired
	private AlertScanIndexHolder			scanIdxHolder;
	@Autowired
	private ZabbixBiz						zabbixBiz;
	@Autowired
	private KafkaTemplate<String, String>	kafkaTemplate;
	
	@PostConstruct
	private void init() {
		String actionIds = AppConfigHelper.getProperty("alertScan.actionIds");
		String[] actionIdArr = actionIds.split(",");
		List<Integer> actionIdList = new ArrayList<>();
		for (String actionId : actionIdArr) {
			actionIdList.add(Integer.parseInt(actionId.trim()));
		}
		this.actionIdArr = actionIdList.toArray(new Integer[actionIdList.size()]);
	}
	
	/**
	* 告警扫描任务. <br/>
	*
	* 作者： pengguihua
	*/  
	@Scheduled(fixedDelayString = "${alertScan.run.fixedDelay}")
	public void run() {
		Long scanIdx = scanIdxHolder.getScanIndex();
		List<AlertModel> fetchAlertList = zabbixBiz.fetchAlertList(scanIdx, actionIdArr, batchCount);
		if (CollectionUtils.isEmpty(fetchAlertList)) {
			return;
		}
		reportAlertDataList(fetchAlertList);
		fetchAlertList.clear();
	}
	
	/**
	* 上报告警数据:  发送到kafka. 分区规则为: 取IP最后一节， 按照总分区数取余 <br/>
	*
	* 作者： pengguihua
	* @param fetchAlertList
	*/  
	private void reportAlertDataList(List<AlertModel> fetchAlertList) {
		log.info("Begin to send the alert list with size {} to center room kafka topic {}.", 
				fetchAlertList.size(), topicName);
//		Long maxAlertId = scanIdxHolder.getScanIndex();
		for (AlertModel alert : fetchAlertList) {
			String deviceIp = alert.getDeviceIP();
			String lastIpNum = deviceIp.substring(deviceIp.lastIndexOf(".") + 1);
			if (StringUtils.isNumeric(lastIpNum)) {
				int partionNum = Integer.parseInt(lastIpNum) % topicPartionsCount;
//			String key = alert.getMonitorRoom() + "_" + deviceIp;
				kafkaTemplate.send(topicName, partionNum, JsonUtil.toJacksonJson(alert));
			} else {
				//ip配置不对的也发送到ums，后续人工定位问题
				kafkaTemplate.send(topicName, JsonUtil.toJacksonJson(alert));
			}
//			Long alertIdNum = Long.parseLong(alert.getZbxAlertId());
//			if (alertIdNum > maxAlertId) {
//				maxAlertId = alertIdNum;
//			}
		}
		fetchAlertList.clear();
//		// 更新缓存中的 扫描游标
//		scanIdxHolder.updateScanIndex(maxAlertId);
	}
}
