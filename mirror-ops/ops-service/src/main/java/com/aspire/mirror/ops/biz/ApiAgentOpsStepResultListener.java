package com.aspire.mirror.ops.biz;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.aspire.mirror.ops.biz.model.OpsActionAgentResult;
import com.aspire.mirror.ops.util.JsonUtil;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: TaskAgentOpsStepResultListener
 * <p/>
 *
 * 类功能描述: 第三方调用ops操作结果监听
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月26日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Component
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
class ApiAgentOpsStepResultListener {
	@Autowired
	private List<AbstractAgentOpsStepResultHandler> handlerList;
	
	@KafkaListener(topics = "${ops.agent-step-result-up-topic}_2")
	public void listen(ConsumerRecord<?, String> cr) throws Exception {
		OpsActionAgentResult agentStepResult = JsonUtil.jacksonConvert(cr.value(), OpsActionAgentResult.class);
		handlerList.stream().forEach(handler -> {
			if (handler.aware(agentStepResult)) {
				handler.handle(agentStepResult);
			}
		});
	}
}
