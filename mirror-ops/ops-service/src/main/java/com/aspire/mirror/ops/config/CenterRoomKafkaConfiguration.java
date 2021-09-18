package com.aspire.mirror.ops.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Listener;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
 * 中央机房kafka配置    <br/>
 * Project Name:alert-service
 * File Name:CenterRoomKafkaConfiguration.java
 * Package Name:com.aspire.mirror.alert.server.config
 * ClassName: CenterRoomKafkaConfiguration <br/>
 * date: 2018年10月20日 下午3:57:59 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Configuration
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
public class CenterRoomKafkaConfiguration {

    @Primary
    @Bean("ceterRoomKafkaProperties")
    @ConfigurationProperties(prefix = "spring.kafka")
    public KafkaProperties properties() {
        return new KafkaProperties();
    }

    @Bean("kafkaTemplate")
    @Primary
    public KafkaTemplate<?, ?> kafkaTemplate() {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<Object, Object>(
                (ProducerFactory<Object, Object>) this.kafkaProducerFactory());
        kafkaTemplate.setProducerListener(this.kafkaProducerListener());
        kafkaTemplate.setDefaultTopic(this.properties().getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }

    @Bean
    @Primary
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<Object, Object>();
    }

    @Bean
    @Primary
    public ConsumerFactory<?, ?> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<Object, Object>(
                this.properties().buildConsumerProperties());
    }

    @Bean
    @Primary
    public ProducerFactory<?, ?> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<Object, Object>(
                this.properties().buildProducerProperties());
    }

    @Bean("kafka1ListenerContainerFactory")
    @Primary
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory
                = new ConcurrentKafkaListenerContainerFactory<Object, Object>();

        factory.setConsumerFactory((ConsumerFactory<Object, Object>) this.kafkaConsumerFactory());
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