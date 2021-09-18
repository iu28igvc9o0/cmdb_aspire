package com.aspire.mirror.ops.biz;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.aspire.mirror.ops.biz.model.AutoRepairAlertMessage;
import com.aspire.mirror.ops.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;


/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: AutoRepairAlertMsgListener
 * <p/>
 *
 * 类功能描述: 自愈指标告警消息监听
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月21日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
class AutoRepairAlertMsgListener {
	@Autowired
	private OpsAutoRepairBiz autoRepairBiz;
	
	@KafkaListener(topics = "${autoRepair.alert.message.topic:ops_auto_repair_alert}")
	public void listen(ConsumerRecord<?, String> cr) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("AutoRepairAlertMsgListener received msg: " + cr.value());
		}
		AutoRepairAlertMessage alertMsg = JsonUtil.jacksonConvert(cr.value(), AutoRepairAlertMessage.class);
		autoRepairBiz.handleAlertMessage4AutoRepair(alertMsg);
	}
}
