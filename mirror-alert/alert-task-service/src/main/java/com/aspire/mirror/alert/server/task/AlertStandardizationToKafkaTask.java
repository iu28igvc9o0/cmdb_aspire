package com.aspire.mirror.alert.server.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.dao.alert.AlertRestfulDao;
import com.aspire.mirror.alert.server.dao.alert.po.AlertStandardization;

import lombok.extern.slf4j.Slf4j;

@EnableScheduling
@Component
@ConditionalOnProperty(value = "AlertStandardizationToKafkaTask.flag", havingValue = "normal")
@Slf4j
public class AlertStandardizationToKafkaTask {

	private static final Logger LOGGER = Logger.getLogger(AlertStandardizationToKafkaTask.class);
	@Autowired
	private AlertRestfulDao alertRestfulDao;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	/*
	 * @Autowired private KafkaTemplate<String, String> kafkaTemplateTwo;
	 */

	@Value("${AlertStandardizationToKafkaTask.partionsCount:3}")
	private Integer topicPartionsCount;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;


	/**
	 * 告警自动化派单定时任务
	 */
	 @KafkaListener(topics = "${AlertStandardizationToKafkaTask.alertKafka:topic_alert_datas}",containerFactory = "kafkaListenerContainerFactoryTwo")
	public void syncAlertData(ConsumerRecord<?, String> cr) {

		LOGGER.info("synchronization-syncAlertData-to-kafka--begin*****************************");
		String value = cr.value();
		JSONObject jb = JSONObject.parseObject(value);
		String tableIndex = jb.getString("index");
		if("alert_alerts".equalsIgnoreCase(tableIndex)) {
			jb.put("alert_type", 1);
		}else if("alert_alerts_his".equalsIgnoreCase(tableIndex) ) {
			jb.put("alert_type", 2);
		}else {
			return;
		}
		
		jb.remove("index");
		MyThreadNew mythread = new MyThreadNew(jb);
		taskExecutor.execute(mythread);
		LOGGER.info("synchronization-syncAlertData-to-kafka--end*****************************");

	}
	 
	 private void handleAlert(JSONObject jb) {
		 int alertType = jb.getIntValue("alert_type");
		 List<AlertStandardization> standList = alertRestfulDao.getBookAlerts(null);
			for (AlertStandardization stand : standList) {
				String condition = stand.getConditions();
				//判断是要历史告警还是活动告警或者都要
				Integer countFlag = stand.getCountType();
				if (null != countFlag ) {
					if(countFlag==0) {
						if(alertType==2) {
							continue;
						}
					}else if(countFlag==1){
						if(alertType==1) {
							continue;
						}
					}
					
				}
				
				JSONObject extColOb = null;
				
				String extCol = stand.getExtraCols();
				if(StringUtils.isNotBlank(extCol)) {
					extColOb = JSONObject.parseObject(extCol);
				}else {
					extColOb = new JSONObject();
				}
				 
				extColOb.putAll(jb);
				String kafkaTopic = stand.getTopic();
				if(StringUtils.isEmpty(condition)) {
					sendKafkaMsg(extColOb,kafkaTopic);
					continue;
				}
				String[] conditions = condition.split("&&");
				
				for(String c:conditions) {
					JSONArray ar = JSONObject.parseArray(c);
					boolean flag = true;
					for(int i=0;i<ar.size();i++) {
						JSONObject ob = ar.getJSONObject(i);
						String operator = ob.getString("operator");
						String valueStr = ob.getString("value");
						String key = ob.getString("col");
						String jbValue = jb.getString(key);
						if(operator.equals("=")) {
							//判断null值
							if(null==jbValue) {
								if(valueStr==null) {
									continue;
								}else {
									flag = false;
									break;
								}
							}
							if(!jbValue.equalsIgnoreCase(valueStr)) {
								flag = false;
								break;
							}else {
								continue;
							}
							
						}
						if(operator.equals("!=")) {
							if(null==jbValue) {
								if(valueStr==null) {
									flag = false;
									break;
								}else {
									continue;
								}
							}
							if(jbValue.equalsIgnoreCase(valueStr)) {
								flag = false;
								break;
							}else {
								continue;
							}
							
						}
						if(operator.equals("in")) {
							List<String> valueList = new ArrayList<String>(Arrays.asList(valueStr.split(",")));
							if(StringUtils.isNotBlank(jbValue)) {
								jbValue = jbValue.toLowerCase();
							}
							
							if(valueList.contains(jbValue)) {
								continue;
								
							}else {
								flag = false;
								break;
							}
							
						}
					}
					if(flag) {
						sendKafkaMsg(extColOb,kafkaTopic);
						break;
					}
				}
				
				
			}
		
	}
	 
	 void sendKafkaMsg(JSONObject jb,String kafkaTopic){
		// String value = jb.toJSONString();
		Integer partition = getPartition(jb.getString("device_ip"));
		if (null != partition) {
			kafkaTemplate.send(kafkaTopic, partition, jb.toJSONString());
			//kafkaTemplateTwo.send(kafkaTopic, 0, JSONObject.toJSONString(value));
		} else {
			kafkaTemplate.send(kafkaTopic, jb.toJSONString());
		}
	 }

	//@KafkaListener(topics = "${AlertStandardizationToKafkaTask.alertHisKafka:topic_alert_alerts_his}",containerFactory = "kafkaListenerContainerFactory")
		public void syncAlertHisData(ConsumerRecord<?, String> cr) {

			LOGGER.info("synchronization-syncAlertHisData-to-kafka--begin*****************************");
			String value = cr.value();
			JSONObject jb = JSONObject.parseObject(value);
			jb.put("alert_type", 2);
			
			MyThreadNew mythread = new MyThreadNew(jb);
			taskExecutor.execute(mythread);

			LOGGER.info("synchronization-syncAlertHisData-to-kafka--end*****************************");

		}
	
	public  class  MyThreadNew implements  Runnable
	{
	    private  JSONObject jb;
	    public  MyThreadNew(JSONObject jb)
	    {
	        this.jb = jb;
	      
	    }
	    public  void  run()
	    {
	    	try {
				//查日志,补漏查的数据
	    		handleAlert( jb); 
				
			} catch (Exception e) {
				LOGGER.error("同步性能数据报错", e);
				
			}
	    }
	   
	}


	Integer getPartition(String deviceIp) {
		if(StringUtils.isBlank(deviceIp)) {
			return null;
		}
		String lastIpNum = deviceIp.substring(deviceIp.lastIndexOf(".") + 1);
		if (StringUtils.isNumeric(lastIpNum)) {
			int partionNum = Integer.parseInt(lastIpNum) % topicPartionsCount;
			return partionNum;
		} else {
			return null;
		}
	}
	
	
	
}
