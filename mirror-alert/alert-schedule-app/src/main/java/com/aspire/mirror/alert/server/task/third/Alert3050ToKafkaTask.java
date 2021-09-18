package com.aspire.mirror.alert.server.task.third;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.dao.third.Alerts3050Dao;

@EnableScheduling
@Component
@ConditionalOnProperty(value = "task3050ToKafka.flag", havingValue = "normal")
public class Alert3050ToKafkaTask {

	private static final Logger LOGGER = Logger.getLogger(Alert3050ToKafkaTask.class);
	@Autowired
	private Alerts3050Dao alerts3050Dao;
	@Value("${task3050ToKafka.intervalMinute:5}")
	private int intervalMinute;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Value("${task3050ToKafka.topik:TOPIC_3D_ALERTS}")
	private String kafkaTopic;
	@Value("${task3050ToKafka.type:all}")
	private String type;
	@Value("${AlertStandardizationToKafkaTask.partionsCount:3}")
	private Integer topicPartionsCount;
	/**
	 * 告警自动化派单定时任务
	 */
	@Scheduled(cron = "${task3050ToKafka.cron:0 */5 * * * ?}")
	public void syncAlertData() {
		LOGGER.info("synchronization alerts3050 to kafka begin*****************************");
		
		List<Date> list = getExecDuration();
		DateFormat returnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat returnd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date startTime =  list.get(0);
		Date endTime =  list.get(1);
		
		List<Map<String,Object>> alertsData = alerts3050Dao.getAlerts("3050", returnd.format(endTime),returnd1.format(startTime));
		
		for(Map<String,Object> map:alertsData) {
			if(null==map.get("device_ip")) {
				continue;
			}
			Integer partition = getPartition(map.get("device_ip").toString());
			kafkaTemplate.send(kafkaTopic, partition,JSONObject.toJSONString(map));
		}
		if(type.equals("all")) {
			List<Map<String,Object>> alertsHisData = alerts3050Dao.getAlertsHis("3050", returnd.format(endTime),returnd1.format(startTime));
			for(Map<String,Object> map:alertsHisData) {
				if(null==map.get("device_ip")) {
					continue;
				}
				Integer partition = getPartition(map.get("device_ip").toString());
				kafkaTemplate.send(kafkaTopic, partition,JSONObject.toJSONString(map));
			}
		}
		LOGGER.info("synchronization alerts3050 to kafka end*****************************");
		
	}
	
	
	public static List<Date> getExecDuration() {
		// log.info("查询设备利用率es脚本： {}", JSONUtils.valueToString(request));
				Calendar calendar = Calendar.getInstance();
		    	calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE))/5*5);
		    	calendar.set(Calendar.SECOND,0);
		    	calendar.set(Calendar.MILLISECOND,0);
		    	Date late = calendar.getTime();
		    	calendar.add(Calendar.MINUTE, -5);
		    	Date early = calendar.getTime();
		    	List<Date> list =  Arrays.asList(early,late);
		    	return list;

	}
	Integer getPartition(String deviceIp) {
		String lastIpNum = deviceIp.substring(deviceIp.lastIndexOf(".") + 1);
		if (StringUtils.isNumeric(lastIpNum)) {
			int partionNum = Integer.parseInt(lastIpNum) % topicPartionsCount;
			return partionNum;
		} else {
			return null;
		}
	}
}
