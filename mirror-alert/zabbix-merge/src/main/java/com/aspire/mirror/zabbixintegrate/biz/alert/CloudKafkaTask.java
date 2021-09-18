package com.aspire.mirror.zabbixintegrate.biz.alert;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.zabbixintegrate.bean.AlertSuyanMonitor;
import com.aspire.mirror.zabbixintegrate.bean.SuyanMonitorConfig;
import com.aspire.mirror.zabbixintegrate.config.IdcTypeProperties;
import com.aspire.mirror.zabbixintegrate.daoCmdb.CmdbInstanceDao;
import com.aspire.mirror.zabbixintegrate.daoCmdb.po.CmdbInstance;
import com.aspire.mirror.zabbixintegrate.util.SuyanMonitorHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RestController
@ConditionalOnProperty(value = "CloudKafkaTask.flag", havingValue = "normal")
public class CloudKafkaTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudKafkaTask.class);

	/*
	 * @Value("${CloudSysSyncTask.delIdcType:呼和浩特资源池}") private String delIdcType;
	 * 
	 * @Value("${CloudSysSyncTask.delPod:POD-3}") private String delPod;
	 */
	
	@Value("${esType:history}")
	private String type;
	
	@Value("${esTypeUint:history_uint}")
	private String type_uint;
	
	 @Autowired
	private CmdbInstanceDao cmdbInstanceDao;
	 
	 @Autowired
	private SuyanMonitorHelper suyanMonitorHelper;
	 
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	

	@Autowired
	private IdcTypeProperties idcTypeProperties;
	
	   @GetMapping(value = "/v1/alert/suyan")
	   public void consumeSuyanMonitorGet(String data) {
			 LOGGER.info("consumeSuyanMonitorGet-start*********");
			 //String aa = idcTypeProperties.getKafkaTopicIdcTypeMap().get("呼和浩特资源池")+"dddd";
			 //String aa1 = idcTypeProperties.getKafkaTopicIdcTypeMap().get("鹿泉资源池")+"dddd";
			 List<AlertSuyanMonitor> dataList = JSON.parseArray(data,
					 AlertSuyanMonitor.class);
			
			 List<String> idList = Lists.newArrayList();
				for(AlertSuyanMonitor d:dataList) {
			           String resId = d.getResourceId();
			           if(!idList.contains(resId)) {
			        	   idList.add(resId);
			           }
				}
			handleData(dataList,idList);
			LOGGER.info("consumeSuyanMonitorGet-end*********");
		    }
	
	   @KafkaListener(topics = "${CloudKafkaTask.fromKafkaTopic:performancedata-deepwatch}",containerFactory = "batchFactory")
	    private void consumeSuyanMonitor(List<ConsumerRecord<?, String>> list) {	
		 LOGGER.info("consumeSuyanMonitor-start*********");
		 try {
		 List<AlertSuyanMonitor> valuelist = Lists.newArrayList();
		 List<String> idList = Lists.newArrayList();
		 for (ConsumerRecord<?, String> record : list) {
			 try {
				 String value = record.value();
				 AlertSuyanMonitor obj = JSON.parseObject(value, AlertSuyanMonitor.class);
				 if(null==obj) {
					 continue;
				 }
				    valuelist.add(obj); 
				    String resId = obj.getResourceId();
			           if(!idList.contains(resId)) {
			        	   idList.add(resId);
			           }
			 }catch(Exception e) {
				 LOGGER.error("解析kafka消息报错:{}",record);
				 LOGGER.error("解析kafka消息报错",e);
			 }
			   
			  }
		 if(valuelist.size()>0 && idList.size()>0) {
			 handleData(valuelist,idList);
		 }
		 }catch(Exception e) {
			 LOGGER.error("数据融合报错:{}",list);
			 LOGGER.error("数据融合报错",e);
		 }
		LOGGER.info("consumeSuyanMonitor-end*********");
	    }
	 
	 public void handleData(List<AlertSuyanMonitor> dataList,List<String> idList){
		 LOGGER.info("handleData-start*********");
	
		 Map<String, CmdbInstance> map = Maps.newHashMap();

			
			Date from1 = new Date();
			 List<CmdbInstance> dsList = cmdbInstanceDao.getDeviceInfo(idList);
			Date to1 = new Date();
			LOGGER.info("cmdb查询苏研VM数据时间:{}ms",(to1.getTime() - from1.getTime()));
		
			//int	dsSize = dsList.size();

			
			for (CmdbInstance val : dsList) {
				map.put(val.getSuyanUuid(), val);
			}
			
	
			for (AlertSuyanMonitor d:dataList) {
				try {
					String resId = d.getResourceId();
					String topicStr = null;
					Map<String,Object> es = Maps.newHashMap();
					boolean flag = getSuyanData(es,d);
					if(!flag) {
						continue;
					}
					if (map.containsKey(resId)) {
						CmdbInstance instance = map.get(resId);
						
						String idcType = instance.getIdcType()==null?"":instance.getIdcType();
						String pod_name = instance.getPodName()==null?"":instance.getPodName();
						//TODO:pod3还要丢弃吗
					/*
					 * if(idcType.equals(delIdcType) && pod_name.equals(delPod)) { delCount++;
					 * continue; }
					 */
						es.put("host", instance.getIp());
						es.put("resourceId", instance.getInstanceId());
						es.put("bizSystem", instance.getBizSystem());
						es.put("department1", instance.getDepartment1());
						es.put("department2", instance.getDepartment2());
						es.put("deviceClass", instance.getDeviceClass());
						es.put("deviceType", instance.getDeviceType());

						es.put("idcType",idcType);
						es.put("roomId",instance.getRoomId());
						es.put("podName",pod_name);
						es.put("nodeType",instance.getNodeType());
						topicStr = idcTypeProperties.getKafkaTopicIdcTypeMap().get(idcType);
						if(StringUtils.isBlank(topicStr)) {
							LOGGER.error("数据发错资源池数据-苏研resid:{}", resId);
						}
					} else {
						LOGGER.error("苏研查无该设备信息-苏研resid:{}", resId);
					}
					if(null==topicStr) {
						topicStr = idcTypeProperties.getDefaultTopic();
					}
					if(es.get("type").toString().equals(this.type)) {
						kafkaTemplate.send(idcTypeProperties.getToKafkaTopic()+topicStr,JSONObject.toJSONString(es));
					}else {
						kafkaTemplate.send(idcTypeProperties.getToKafkaUintTopic()+topicStr,JSONObject.toJSONString(es));
					}
				}catch(Exception e) {
					LOGGER.info("处理数据报错:{}",d);
					LOGGER.error("处理数据报错}",e);
					
				}
			}
			map.clear();
			 LOGGER.info("handleData-end*********");	
		
	 }

	private boolean getSuyanData(Map<String, Object> es, AlertSuyanMonitor d) {
		String syKey = d.getMetricName();
		SuyanMonitorConfig config = suyanMonitorHelper.getConfigData(syKey);
		Date date = new Date(d.getDatetime()*1000);
		
		
		es.put("datetime", date.toInstant().toString());
		es.put("clock", d.getDatetime());
		es.put("time", d.getTime());
	
		es.put("suyanUuid", d.getResourceId());
		
		
	    String item = d.getMetricName();
	    
	    if(null!=config) {
	    	if(StringUtils.isNotBlank(config.getKey_())) {
	    		item = config.getKey_();
	    	}
	    	if(StringUtils.isNotBlank(config.getSy_uint_())) {
				if(config.getSy_uint_().equals("%")){
					es.put("type",this.type);
				}else {
					es.put("type",this.type_uint);
				}
			}else {
				es.put("type",this.type_uint);
			}
	    }else {
	    	return false;
	    }
		es.put("item", item);
		if(StringUtils.isNotBlank(d.getMetricNodeName())) {
			es.put("item", item+"["+d.getMetricNodeName()+"]");
		}
		es.put("value",d.getValue());
		return true;
	}
}
