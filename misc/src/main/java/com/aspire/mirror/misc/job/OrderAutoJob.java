package com.aspire.mirror.misc.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.aspire.mirror.misc.dao.MiscDao;
import com.aspire.mirror.misc.dispatcher.SnmpUtilSendTrap;
import com.aspire.mirror.misc.model.SnmpEvent;
import com.aspire.mirror.misc.model.SnmpEventNode;
import com.aspire.mirror.misc.util.CommonUtil;

@Component
public class OrderAutoJob {

	@Resource(name="${province.name}")
	private SnmpUtilSendTrap sendTrap;
	
	@Autowired
	private MiscDao miscDao;
	
	@Value("${province.ip.port}")
	private String ipPort;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderAutoJob.class);
	
	@Scheduled(cron = "${orderAutoGen.cron}")
	public void executeJob() {
		
		//查询出待上传的告警数据集合
		List<SnmpEvent> snmpEventList = new ArrayList<SnmpEvent>();
		//int count = miscDao.findSnmpEventSucInfoCount();
		snmpEventList = miscDao.ListAllSnmpEvent();
		
		
		//查询出描述字段对应的OID，然后转化成一一对应的map集合
		List<SnmpEventNode> snmpNodeList = miscDao.listSnmpEventNode();
		Map<String,String> nodeMap = getSnmpNodeMap(snmpNodeList);
		
		
		
		/*为"待上传的告警数据集合"逐条插入dataVersion
		  判断每一条 alertStatus为2 的告警数据,是否在"上传成功表"中有为1 (即告警) 的对应记录
		  (匹配条件是：alertId相同 & alertStatus为 1 & "告警"信息的时间要晚于 "消警"信息)
		 目的是,当数据的alertStatus为2(消警),要验证在"上传成功表"中是否有对应的为1(告警)的数据
		 如果有，把消警信息发送
		 如果没有，把AlertStatus设置为1  后发送告警信息，然后再发送消警信息
		*/
		String dataVersion = CommonUtil.DateToString(new Date());
		
		if(snmpEventList.size() > 0) {
			
			LOGGER.info("查询出 "+snmpEventList.size()+" 条数据，开始上传处理");
			
		}
		
		for(int i=0;i<snmpEventList.size();i++){
			
			SnmpEvent snmpEvent = snmpEventList.get(i);
			Date alertStartTime = snmpEvent.getAlertStartTime();
			Date removeDate = snmpEvent.getRemoveDate();
			String alertStatus = snmpEvent.getAlertStatus();
			if(alertStartTime == null && alertStatus.equals("1")){
				LOGGER.info("告警id "+snmpEvent.getAlertId()+"-----没有告警开始时间，不予处理");
				continue;
			}
			if(removeDate == null && alertStatus.equals("2")){
				LOGGER.info("消警id "+snmpEvent.getAlertId()+"-----没有消警结束时间，不予处理");
				continue;
			}
			snmpEvent.setDataVersion(dataVersion);
			if("2".equals(alertStatus) && snmpEvent.getCreateTime() != null) {
				
				/*
				判断每一条"消警"信息,是否发送过对应的"告警"信息,如果没有，则先发送"告警"信息
				(匹配条件是：alertId相同 & alertStatus为 1 & "消警"信息的时间要晚于 "告警"信息)
				*/
				String info = miscDao.findSnmpEventAlert(snmpEvent);
				
				if(info == null) {
					//这里要clone复制资源.否则多线程会读脏公共数据
					SnmpEvent snmpEventClone = new SnmpEvent();
					BeanUtils.copyProperties(snmpEvent,snmpEventClone);
					snmpEventClone.setAlertStatus("1");
					snmpEventClone.setRemoveDate(null);
					snmpEventClone.setEventStatus(1);
					//调用snmp4j工具,发送报文
					sendTrap.sendPDU(snmpEventClone,nodeMap,ipPort);
				}
			}
			
			//调用snmp4j工具,发送报文
			sendTrap.sendPDU(snmpEvent,nodeMap,ipPort);
		}
	}
	
	
	
	private Map<String,String> getSnmpNodeMap(List<SnmpEventNode> snmpNodeList){
		Map<String, String> snmpMap = new HashMap<String, String>();
		for(int i=0;i<snmpNodeList.size();i++){
			SnmpEventNode snmpEventNode = snmpNodeList.get(i);
			snmpMap.put(snmpEventNode.getNodeMiscName(), snmpEventNode.getNodeMiscId());
		}
		return snmpMap;
	}
}
