package com.aspire.cmdb.agent.sync.schedule.cmdb;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.sync.payload.CmdbSyncModuleCondition;
import com.aspire.ums.cmdb.sync.payload.CmdbSyncType;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务线移动接口人同步
 *
 * @author jiangxuwen
 * @date 2020/5/20 15:19
 */
@Data
@Component
@Slf4j
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class CmdbBusinessContactConsumer extends AbstractCmdbModuleSyncConsumer {

    @KafkaListener(id = "cmdb-business-line-contact-sync-kafka-0",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_business_line_contact:osa_cmdb_business_line_contact}",
                    partitions = { "0" }) })
    public void listenPartition0(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-business-line-contact-sync-kafka-0 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            super.onMessage(records, ack);
        } catch (Exception e) {
            log.error("cmdb-business-line-contact-sync-kafka-0 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb_business_line_contact-sync-kafka-1",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_business_line_contact:osa_cmdb_business_line_contact}",
                    partitions = { "1" }) })
    public void listenPartition1(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-business-line-contact-sync-kafka-1 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            super.onMessage(records, ack);
        } catch (Exception e) {
            log.error("cmdb-business-line-contact-sync-kafka-1 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-business-line-contact-sync-kafka-2",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_business_line_contact:osa_cmdb_business_line_contact}",
                    partitions = { "2" }) })
    public void listenPartition2(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-business-line-contact-sync-kafka-2 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            super.onMessage(records, ack);
        } catch (Exception e) {
            log.error("cmdb-business-line-contact-sync-kafka-2 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-business-line-contact-sync-kafka-3",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_business_line_contact:osa_cmdb_business_line_contact}",
                    partitions = { "3" }) })
    public void listenPartition3(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-business-line-contact-sync-kafka-3 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            super.onMessage(records, ack);
        } catch (Exception e) {
            log.error("cmdb-business-line-contact-sync-kafka-3 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-business-line-contact-sync-kafka-4",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_business_line_contact:osa_cmdb_business_line_contact}",
                    partitions = { "4" }) })
    public void listenPartition4(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-business-line-contact-sync-kafka-4 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            super.onMessage(records, ack);
        } catch (Exception e) {
            log.error("cmdb-business-line-contact-sync-kafka-4 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-business-line-contact-sync-kafka-5",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_business_line_contact:osa_cmdb_business_line_contact}",
                    partitions = { "5" }) })
    public void listenPartition5(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-business-line-contact-sync-kafka-5 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            super.onMessage(records, ack);
        } catch (Exception e) {
            log.error("cmdb-business-line-contact-sync-kafka-5 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-business-line-contact-sync-kafka-6",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_ConfigDict}",
                    partitions = { "6" }) })
    public void listenPartition6(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-business-line-contact-sync-kafka-6 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            super.onMessage(records, ack);
        } catch (Exception e) {
            log.error("cmdb-business-line-contact-sync-kafka-6 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-business-line-contact-sync-kafka-7",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_business_line_contact:osa_cmdb_business_line_contact}",
                    partitions = { "7" }) })
    public void listenPartition7(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-business-line-contact-sync-kafka-7 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            super.onMessage(records, ack);
        } catch (Exception e) {
            log.error("cmdb-business-line-contact-sync-kafka-7 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-business-line-contact-sync-kafka-8",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_business_line_contact:osa_cmdb_business_line_contact}",
                    partitions = { "8" }) })
    public void listenPartition8(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-business-line-contact-sync-kafka-8 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            super.onMessage(records, ack);
        } catch (Exception e) {
            log.error("cmdb-business-line-contact-sync-kafka-8 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-business-line-contact-sync-kafka-9",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_business_line_contact:osa_cmdb_business_line_contact}",
                    partitions = { "9" }) })
    public void listenPartition9(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-business-line-contact-sync-kafka-9 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            super.onMessage(records, ack);
        } catch (Exception e) {
            log.error("cmdb-business-line-contact-sync-kafka-9 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @Override
    protected CmdbSyncModuleCondition buildFieldCondition() {
        return new CmdbSyncModuleCondition(KafkaConfigConstant.MAPPING_MODULE,
                KafkaConfigConstant.MAPPING_KEY_CMDB__BUSINESS_CONTACT, KafkaConfigConstant.MAPPING_TYPE_CMDB_BUSINESS_CONTACT,
                KafkaConfigConstant.MAPPING_KEY_FIELD);
    }

    @Override
    protected CmdbSyncType getSyncType() {
        return CmdbSyncType.BUSINESS_CONTACT;
    }
}
