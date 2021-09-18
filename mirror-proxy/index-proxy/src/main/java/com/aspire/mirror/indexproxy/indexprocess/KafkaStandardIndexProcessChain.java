package com.aspire.mirror.indexproxy.indexprocess;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.domain.StandardIndex;
import com.aspire.mirror.indexproxy.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
* 标准指标处理链    <br/>
* Project Name:index-proxy
* File Name:StandardIndexProcessChain.java
* Package Name:com.aspire.mirror.indexproxy.indexprocess
* ClassName: StandardIndexProcessChain <br/>
* date: 2018年8月16日 下午1:37:18 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Component
@ConditionalOnProperty(name="middleware.configuration.switch.kafka", havingValue="true")
class KafkaStandardIndexProcessChain {
	public static final String			STANDARD_INDEX_TOPIC	= "topic_standardIndex";
	
	@Autowired
	private List<IStandardIndexProcess> indexProcessorList;
	
	@KafkaListener(topics = STANDARD_INDEX_TOPIC)
	public void listen(ConsumerRecord<?, String> cr) throws Exception {
		log.debug("StandardIndexProcessChain got standardIndex kafka msg with offset {}", cr.offset());
		StandardIndex standardIdx = JsonUtil.jacksonConvert(cr.value(), StandardIndex.class);
		for (IStandardIndexProcess processor : indexProcessorList) {
			boolean flag = processor.processStandardIndex(standardIdx);
			if (!flag) {
				break;
			}
		}
	}
}
