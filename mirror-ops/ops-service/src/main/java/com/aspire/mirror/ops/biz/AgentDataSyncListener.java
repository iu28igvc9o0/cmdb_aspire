package com.aspire.mirror.ops.biz;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.aspire.mirror.ops.biz.model.AgentHostSyncData;
import com.aspire.mirror.ops.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: AgentDataSyncListener
 * <p/>
 *
 * 类功能描述: agent主机数据同步监听器
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月18日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
class AgentDataSyncListener {
	@Autowired
	private AgentHostDataBiz agentBiz;
	
	@KafkaListener(topics = "${ops.agent-data-sync-up-topic}")
	public void listen(ConsumerRecord<?, String> cr) throws Exception {
		AgentHostSyncData syncData = JsonUtil.jacksonConvert(cr.value(), AgentHostSyncData.class);
		if (!syncData.selfCheck()) {
			log.warn("Received invalid agentHostInfo data, detail: " + syncData);
			return;
		}
		agentBiz.processAgentHostDataSync(syncData);
	}
}
