package com.aspire.cmdb.agent.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
* 本地机房kafka属性映射类    <br/>
* Project Name:index-proxy
* File Name:AgentKafkaProperties.java
* Package Name:com.aspire.mirror.indexproxy.config
* ClassName: AgentKafkaProperties <br/>
* date: 2018年10月15日 下午5:32:18 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@ConfigurationProperties(prefix = "spring.kafka")
public class AgentKafkaProperties extends KafkaProperties {
	private final Consumer consumer = new Consumer();
	private final Producer producer = new Producer();

	/**
	* 获取本地kafka配置的默认分区数. <br/>
	*
	* 作者： pengguihua
	* @return
	*/  
	public Integer getKafkaConfigPartionCount() {
		return super.getListener().getConcurrency();
	}

	@Override
	public Map<String, Object> buildConsumerProperties() {
		Map<String, Object> properties = super.buildConsumerProperties();
		properties.putAll(this.consumer.customProperties());
		return properties;
	}

	@Override
	public Map<String, Object> buildProducerProperties() {
		Map<String, Object> properties = super.buildProducerProperties();
		properties.putAll(this.producer.customProperties());
		return properties;
	}

	public static class Consumer extends KafkaProperties.Consumer {
		
		public Map<String, Object> customProperties() {
			Map<String, Object> additionProps = new HashMap<>();
			// other custom properties setting
			return additionProps;
		}

	}
	
	public static class Producer extends KafkaProperties.Producer {
		
		public Map<String, Object> customProperties() {
			Map<String, Object> additionProps = new HashMap<>();
			// other custom properties setting
			return additionProps;
		}
	}

	public static class Listener extends KafkaProperties.Listener {

		public Map<String, Object> customProperties() {
			Map<String, Object> additionProps = new HashMap<>();
			// other custom properties setting
			return additionProps;
		}
	}
}	
