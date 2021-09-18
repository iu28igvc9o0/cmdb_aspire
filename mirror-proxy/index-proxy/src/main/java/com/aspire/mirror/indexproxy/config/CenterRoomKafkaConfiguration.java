package com.aspire.mirror.indexproxy.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Listener;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;


/**
* 中央机房kafka自动配置    <br/>
* Project Name:index-proxy
* File Name:CenterRoomKafkaConfiguration.java
* Package Name:com.aspire.mirror.indexproxy.config
* ClassName: CenterRoomKafkaConfiguration <br/>
* date: 2018年10月15日 下午5:34:24 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@EnableKafka
@Configuration
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true} && ${centerRoom.kafka.enable:false}")
public class CenterRoomKafkaConfiguration {
	
	@Bean
	@Qualifier("centerRoom")
	@ConfigurationProperties(prefix = "centerRoom.kafka")
	public KafkaProperties centerRoomKafkaProperties() {
		return new KafkaProperties();
	}

	@Bean
	@Qualifier("centerRoom")
	public KafkaTemplate<?, ?> centerRoomKafkaTemplate() {
		KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<Object, Object>(centerRoomKafkaProducerFactory());
		kafkaTemplate.setProducerListener(centerRoomKafkaProducerListener());
		kafkaTemplate.setDefaultTopic(centerRoomKafkaProperties().getTemplate().getDefaultTopic());
		return kafkaTemplate;
	}
	
	@Bean
	@Qualifier("centerRoom")
	public ProducerListener<Object, Object> centerRoomKafkaProducerListener() {
		return new LoggingProducerListener<Object, Object>();
	}
	
	@Bean
	@Qualifier("centerRoom")
	public ConsumerFactory<Object, Object> centerRoomKafkaConsumerFactory() {
		return new DefaultKafkaConsumerFactory<Object, Object>(centerRoomKafkaProperties().buildConsumerProperties());
	}
	
	@Bean
	@Qualifier("centerRoom")
	public ProducerFactory<Object, Object> centerRoomKafkaProducerFactory() {
		return new DefaultKafkaProducerFactory<Object, Object>(centerRoomKafkaProperties().buildProducerProperties());
	}
	
	@Bean
	@Qualifier("centerRoom")
	public ConcurrentKafkaListenerContainerFactory<?, ?> centerRoomKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory 
			= new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		
		factory.setConsumerFactory(centerRoomKafkaConsumerFactory());
		Listener container = centerRoomKafkaProperties().getListener();
		ContainerProperties containerProperties = factory.getContainerProperties();
		if (container.getAckMode() != null) {
			containerProperties.setAckMode(container.getAckMode());
		}
		if (container.getAckCount() != null) {
			containerProperties.setAckCount(container.getAckCount());
		}
		if (container.getAckTime() != null) {
			containerProperties.setAckTime(container.getAckTime());
		}
		if (container.getPollTimeout() != null) {
			containerProperties.setPollTimeout(container.getPollTimeout());
		}
		if (container.getConcurrency() != null) {
			factory.setConcurrency(container.getConcurrency());
		}
		return factory;
	}
}

