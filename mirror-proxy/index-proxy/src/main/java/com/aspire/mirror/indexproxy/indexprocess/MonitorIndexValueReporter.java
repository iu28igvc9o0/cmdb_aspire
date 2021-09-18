package com.aspire.mirror.indexproxy.indexprocess;

import static com.aspire.mirror.indexproxy.indexprocess.Consts.CENTER_ROOM_MONITOR_VALUE_TOPIC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.domain.StandardIndex;
import com.aspire.mirror.indexproxy.util.JsonUtil;

/**
* 监控指标数据上报    <br/>
* Project Name:index-proxy
* File Name:MonitorIndexValueReporter.java
* Package Name:com.aspire.mirror.indexproxy.indexprocess
* ClassName: MonitorIndexValueReporter <br/>
* date: 2018年8月21日 上午11:13:58 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Order(1)
@Component
@ConditionalOnExpression("${middleware.configuration.switch.kafka:false} && ${centerRoom.kafka.enable:false}")
public class MonitorIndexValueReporter implements IStandardIndexProcess {
	@Autowired
	@Qualifier("centerRoom")
	private KafkaTemplate<String, String> centerRoomKafkaTemplate;
	
	@Override
	public boolean processStandardIndex(StandardIndex index) {
		centerRoomKafkaTemplate.send(CENTER_ROOM_MONITOR_VALUE_TOPIC, index.getItemId(), JsonUtil.toJacksonJson(index));
		return true;
	}

}
