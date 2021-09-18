package com.aspire.mirror.thirdparty.alertnotify.biz;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.aspire.mirror.thirdparty.alertnotify.domain.AlertNotifyWrap;
import com.aspire.mirror.thirdparty.alertnotify.domain.RtzAlertNotifyMeta;
import com.aspire.mirror.thirdparty.alertnotify.util.JsonUtil;
import com.cmcc.family.alertagent.AlertAgent;

@Component
public class RtzAlertNotifyBiz {
	private final Map<String, AlertAgent>	kafkaMeta	= new HashMap<>();
	private final Object					lockObj		= new Object();
	
	public void doAlertNotify(AlertNotifyWrap alertMsg) {
		RtzAlertNotifyMeta meta = JsonUtil.jacksonConvert(alertMsg.getMeta(), RtzAlertNotifyMeta.class);
		
		String kafkaServers = meta.getKafkaServers();
		if (StringUtils.isBlank(kafkaServers)) {
			throw new RuntimeException("The kafkaservers should not be empty.");
		}
		
		AlertAgent agent = getAlertAgent(kafkaServers);
		agent.sendAlert(meta.getKafkaTopic(), alertMsg.getAlertTitle(), alertMsg.getAlertContent(), meta.isAsyc());
	}
	
	private AlertAgent getAlertAgent(String kafkaServers) {
		AlertAgent agent = kafkaMeta.get(kafkaServers);
		if (agent != null) {
			return agent;
		}
		synchronized (lockObj) {
			agent = new AlertAgent(kafkaServers);
			kafkaMeta.put(kafkaServers, agent);
			return agent;
		}
	}
	
	@PreDestroy
	public void destroy() {
		AlertAgent.close();
	}
}
