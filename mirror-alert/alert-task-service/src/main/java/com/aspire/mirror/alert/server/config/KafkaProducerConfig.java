package com.aspire.mirror.alert.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerConfigUtils;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;

import com.aspire.mirror.alert.server.config.KafkaPropertiesTwo.Listener;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: KafkaProducerFactory
 * Author:   hangfang
 * Date:     2020/9/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Configuration
@ConditionalOnClass(KafkaTemplate.class)
@EnableConfigurationProperties(KafkaPropertiesTwo.class)
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
public class KafkaProducerConfig {

	
	    @Autowired
	    private KafkaPropertiesTwo propertiesTwo;

	    @Bean
	    public KafkaTemplate<?, ?> kafkaTemplateTwo(
	            ProducerFactory<Object, Object> kafkaProducerFactoryTwo,
	            ProducerListener<Object, Object> kafkaProducerListenerTwo) {
	        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<Object, Object>(
	        		kafkaProducerFactoryTwo);
	        kafkaTemplate.setProducerListener(kafkaProducerListenerTwo);
	        kafkaTemplate.setDefaultTopic(this.propertiesTwo.getTemplate().getDefaultTopic());
	        return kafkaTemplate;
	    }

	    @Bean
	    public ProducerListener<Object, Object> kafkaProducerListenerTwo() {
	        return new LoggingProducerListener<Object, Object>();
	    }

	    @Bean
	    public ConsumerFactory<?, ?> kafkaConsumerFactoryTwo() {
	        return new DefaultKafkaConsumerFactory<Object, Object>(
	                this.propertiesTwo.buildConsumerProperties());
	    }

	    @Bean
	    public ProducerFactory<?, ?> kafkaProducerFactoryTwo() {
	        return new DefaultKafkaProducerFactory<Object, Object>(
	                this.propertiesTwo.buildProducerProperties());
	    }

	    @Bean
	    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactoryTwo(
	            ConsumerFactory<Object, Object> kafkaConsumerFactoryTwo) {
	        ConcurrentKafkaListenerContainerFactory<Object, Object> factory
	                = new ConcurrentKafkaListenerContainerFactory<Object, Object>();

	        factory.setConsumerFactory(kafkaConsumerFactoryTwo);
	        Listener container = this.propertiesTwo.getListener();
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

	    @EnableKafka
	    @ConditionalOnMissingBean(name = KafkaListenerConfigUtils.KAFKA_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
	    protected static class EnableKafkaConfiguration {

	    }
}