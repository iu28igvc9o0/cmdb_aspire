package com.aspire.mirror.indexproxy.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Listener;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
* 本地机房kafka自动配置    <br/>
* Project Name:index-proxy
* File Name:LocalKafkaConfiguration.java
* Package Name:com.aspire.mirror.indexproxy.config
* ClassName: LocalKafkaConfiguration <br/>
* date: 2018年10月15日 下午5:32:46 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@EnableKafka
@Configuration
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
public class LocalKafkaConfiguration {
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "local.kafka")
	public KafkaProperties localKafkaProperties() {
		return new KafkaProperties();
	}
	
	@Bean
	@Primary
	public KafkaTemplate<?, ?> kafkaTemplate() {
		KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<Object, Object>(kafkaProducerFactory());
		kafkaTemplate.setProducerListener(kafkaProducerListener());
		kafkaTemplate.setDefaultTopic(localKafkaProperties().getTemplate().getDefaultTopic());
		return kafkaTemplate;
	}

	@Bean
	@Primary
	public ProducerListener<Object, Object> kafkaProducerListener() {
		return new LoggingProducerListener<Object, Object>();
	}

	@Bean
	@Primary
	public ConsumerFactory<Object, Object> kafkaConsumerFactory() {
		return new DefaultKafkaConsumerFactory<Object, Object>(localKafkaProperties().buildConsumerProperties());
	}

	@Bean
	@Primary
	public ProducerFactory<Object, Object> kafkaProducerFactory() {
		return new DefaultKafkaProducerFactory<Object, Object>(localKafkaProperties().buildProducerProperties());
	}
	
	@Bean
	@Primary
	public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory 
				= new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		
		factory.setConsumerFactory(kafkaConsumerFactory());
		Listener container = localKafkaProperties().getListener();
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