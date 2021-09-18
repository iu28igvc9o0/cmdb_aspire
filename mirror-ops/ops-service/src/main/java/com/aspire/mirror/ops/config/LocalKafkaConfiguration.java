package com.aspire.mirror.ops.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Listener;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

/**
 * 本地机房kafka自动配置    <br/>
 * Project Name:index-proxy
 * File Name:LocalKafkaConfiguration.java
 * Package Name:com.aspire.mirror.indexproxy.config
 * ClassName: LocalKafkaConfiguration <br/>
 * date: 2018年10月15日 下午5:32:46 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Configuration
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
public class LocalKafkaConfiguration {
   
    @Qualifier("local")
    @Bean("localKafkaProperties")
    @ConfigurationProperties(prefix = "local.kafka")
    public KafkaProperties properties() {
        return new KafkaProperties();
    }

    @Bean("kafka2Template")
    public KafkaTemplate<?, ?> kafka2Template() {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<Object, Object>(
                (ProducerFactory<Object, Object>) this.kafka2ProducerFactory());
        kafkaTemplate.setProducerListener(this.kafka2ProducerListener());
        kafkaTemplate.setDefaultTopic(this.properties().getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }

    @Bean("kafka2ProducerListener")
    public ProducerListener<Object, Object> kafka2ProducerListener() {
        return new LoggingProducerListener<Object, Object>();
    }

    @Bean("kafka2ConsumerFactory")
    public ConsumerFactory<?, ?> kafka2ConsumerFactory() {
        return new DefaultKafkaConsumerFactory<Object, Object>(
                this.properties().buildConsumerProperties());
    }

    @Bean("kafka2ProducerFactory")
    public ProducerFactory<?, ?> kafka2ProducerFactory() {
        return new DefaultKafkaProducerFactory<Object, Object>(
                this.properties().buildProducerProperties());
    }

    @Bean("kafka2ListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafka2ListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory
                = new ConcurrentKafkaListenerContainerFactory<Object, Object>();

        factory.setConsumerFactory((ConsumerFactory<Object, Object>) kafka2ConsumerFactory());
        Listener container = this.properties().getListener();
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