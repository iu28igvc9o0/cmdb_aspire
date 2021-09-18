package com.aspire.mirror.indexproxy.eventprocess;

import static com.aspire.mirror.indexproxy.indexprocess.Consts.MONITOR_EVENT_TOPIC;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.domain.MonitorEventRecord;
import com.aspire.mirror.indexproxy.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
* 监控事件监听执行入口    <br/>
* Project Name:index-proxy
* File Name:MonitorEventProcessChain.java
* Package Name:com.aspire.mirror.indexproxy.eventprocess
* ClassName: MonitorEventProcessChain <br/>
* date: 2018年8月24日 下午4:01:33 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Component
@ConditionalOnProperty(name="middleware.configuration.switch.kafka", havingValue="true")
class KafkaMonitorEventProcessEntry {
	@Autowired
	private MonitorEventProcessFacade eventProcessFacade;
	
	@KafkaListener(topics = MONITOR_EVENT_TOPIC)
	public void listen(ConsumerRecord<?, String> cr) throws Exception {
		log.debug("MonitorEventProcessChain got kafka msg with offset {}", cr.offset());
		MonitorEventRecord event = JsonUtil.jacksonConvert(cr.value(), MonitorEventRecord.class);
		eventProcessFacade.process(event);
	}
}
