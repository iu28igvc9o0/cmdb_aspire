package com.aspire.mirror.indexproxy.indexprocess;

import static com.aspire.mirror.indexproxy.indexprocess.Consts.MONITOR_EVENT_TOPIC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.domain.MonitorEventRecord;
import com.aspire.mirror.indexproxy.eventprocess.MonitorEventProcessFacade;
import com.aspire.mirror.indexproxy.util.JsonUtil;

/**
* 监控项事件发布器    <br/>
* Project Name:index-proxy
* File Name:InternalMonitorEventPublisher.java
* Package Name:com.aspire.mirror.indexproxy.indexprocess
* ClassName: InternalMonitorEventPublisher <br/>
* date: 2019年7月1日 下午7:45:21 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Component
public class InternalMonitorEventPublisher {
	
	@Autowired(required=false)
	private KafkaTemplate<String, String>	kafkaTemplate;
	
	@Autowired
	private MonitorEventProcessFacade eventProcessFacade;
	
	public void publishMonitorEvent(MonitorEventRecord event, String ... bizObjArr) {
		if (kafkaTemplate != null) {
			String key = bizObjArr[0];
			this.kafkaTemplate.send(MONITOR_EVENT_TOPIC, key, JsonUtil.toJacksonJson(event));
			return;
		} else {
			eventProcessFacade.process(event);
		}
	}
}
