package com.aspire.cmdb.agent.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Listener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;

/**
 * 本地机房kafka自动配置 <br/>
 * Project Name:index-proxy File Name:LocalKafkaConfiguration.java Package Name:com.aspire.mirror.indexproxy.config ClassName:
 * LocalKafkaConfiguration <br/>
 * date: 2018年10月15日 下午5:32:46 <br/>
 * 
 * @author pengguihua
 * @version
 * @since JDK 1.6
 */
@Configuration
@ConditionalOnClass(KafkaTemplate.class)
@EnableConfigurationProperties(AgentKafkaProperties.class)
public class AgentKafkaConfiguration {

    @Autowired
    private AgentKafkaProperties properties;

    @Bean
    @Primary
    public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory,
            ProducerListener<Object, Object> kafkaProducerListener) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<Object, Object>(kafkaProducerFactory);
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
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
        return new DefaultKafkaConsumerFactory<Object, Object>(this.properties.buildConsumerProperties());
    }

    @Bean
    @Primary
    public ProducerFactory<?, ?> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<Object, Object>(this.properties.buildProducerProperties());
    }

    @Bean
    @Primary
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConsumerFactory<Object, Object> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();

        factory.setConsumerFactory(kafkaConsumerFactory);
        Listener container = this.properties.getListener();
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

    /**
     * 手工提交offset,防止消费者未处理消息宕机消息丢失.
     * 
     * @param
     * @return
     */
    @Bean("ackContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Object, Object> ackContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory();
        Map<String, Object> props = this.properties.buildConsumerProperties();
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory(props));
        Listener container = this.properties.getListener();
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
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    @EnableKafka
    @ConditionalOnMissingBean(name = KafkaListenerConfigUtils.KAFKA_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
    protected static class EnableKafkaConfiguration {

    }
}
