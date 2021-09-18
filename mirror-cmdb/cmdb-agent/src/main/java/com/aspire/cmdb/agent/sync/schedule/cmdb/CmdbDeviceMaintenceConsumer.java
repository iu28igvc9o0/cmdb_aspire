package com.aspire.cmdb.agent.sync.schedule.cmdb;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.sync.payload.CmdbSyncModuleCondition;
import com.aspire.ums.cmdb.sync.payload.CmdbSyncType;
import com.aspire.ums.cmdb.sync.service.CmdbSyncReceiveMessageLogService;
import com.aspire.ums.cmdb.sync.service.ICmdbSyncFieldMappingService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;

import lombok.Data;

/**
 * cmdb 硬件维保同步.
 *
 * @author jiangxuwen
 * @date 2020/5/18 18:50
 */
@Data
@Component
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class CmdbDeviceMaintenceConsumer extends AbstractCmdbModuleSyncConsumer {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ICmdbInstanceService cmdbInstanceService;

    @Autowired
    private ICmdbSyncFieldMappingService cmdbSyncFieldMappingService;

    @Autowired
    private CmdbSyncReceiveMessageLogService receiveMessageLogService;

    @KafkaListener(id = "cmdb-device-maintence-sync-kafka-0",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_device_maintence:osa_cmdb_device_maintence}",
                    partitions = { "0" }) })
    public void listenPartition0(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-device-maintence-sync-kafka-0 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-device-maintence-sync-kafka-1",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_device_maintence:osa_cmdb_device_maintence}",
                    partitions = { "1" }) })
    public void listenPartition1(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-device-maintence-sync-kafka-1 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-device-maintence-sync-kafka-2",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_device_maintence:osa_cmdb_device_maintence}",
                    partitions = { "2" }) })
    public void listenPartition2(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-device-maintence-sync-kafka-2 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-device-maintence-sync-kafka-3",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_device_maintence:osa_cmdb_device_maintence}",
                    partitions = { "3" }) })
    public void listenPartition3(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-device-maintence-sync-kafka-3 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-device-maintence-sync-kafka-4",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_device_maintence:osa_cmdb_device_maintence}",
                    partitions = { "4" }) })
    public void listenPartition4(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-device-maintence-sync-kafka-4 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-device-maintence-sync-kafka-5",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_device_maintence:osa_cmdb_device_maintence}",
                    partitions = { "5" }) })
    public void listenPartition5(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-device-maintence-sync-kafka-5 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-device-maintence-sync-kafka-6",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_ConfigDict}",
                    partitions = { "6" }) })
    public void listenPartition6(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-device-maintence-sync-kafka-6 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-device-maintence-sync-kafka-7",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_device_maintence:osa_cmdb_device_maintence}",
                    partitions = { "7" }) })
    public void listenPartition7(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-device-maintence-sync-kafka-7 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-device-maintence-sync-kafka-8",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_device_maintence:osa_cmdb_device_maintence}",
                    partitions = { "8" }) })
    public void listenPartition8(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-device-maintence-sync-kafka-8 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-device-maintence-sync-kafka-9",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_device_maintence:osa_cmdb_device_maintence}",
                    partitions = { "9" }) })
    public void listenPartition9(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-device-maintence-sync-kafka-9 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @Override
    protected CmdbSyncModuleCondition buildFieldCondition() {
        return new CmdbSyncModuleCondition(KafkaConfigConstant.MAPPING_MODULE, KafkaConfigConstant.MAPPING_KEY_MAINTENCE,
                KafkaConfigConstant.MAPPING_TYPE_MAINTENCE, KafkaConfigConstant.MAPPING_KEY_FIELD);
    }

    @Override
    protected CmdbSyncType getSyncType() {
        return CmdbSyncType.CMDB_DEVICE_MAINTENCE;
    }
}
