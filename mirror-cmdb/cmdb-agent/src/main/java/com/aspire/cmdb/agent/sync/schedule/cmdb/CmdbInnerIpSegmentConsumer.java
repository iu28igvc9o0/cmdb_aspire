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
 * cmdb 内网IP网段地址库同步.
 *
 * @author jiangxuwen
 * @date 2020/5/18 18:50
 */
@Data
@Component
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class CmdbInnerIpSegmentConsumer extends AbstractCmdbModuleSyncConsumer {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ICmdbInstanceService cmdbInstanceService;

    @Autowired
    private ICmdbSyncFieldMappingService cmdbSyncFieldMappingService;

    @Autowired
    private CmdbSyncReceiveMessageLogService receiveMessageLogService;

    @KafkaListener(id = "cmdb-inner-ip-segment-sync-kafka-0",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_inner_ip_segment:osa_cmdb_inner_ip_segment}",
                    partitions = { "0" }) })
    public void listenPartition0(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-inner-ip-segment-sync-kafka-0 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-inner-ip-segment-sync-kafka-1",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_inner_ip_segment:osa_cmdb_inner_ip_segment}",
                    partitions = { "1" }) })
    public void listenPartition1(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-inner-ip-segment-sync-kafka-1 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-inner-ip-segment-sync-kafka-2",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_inner_ip_segment:osa_cmdb_inner_ip_segment}",
                    partitions = { "2" }) })
    public void listenPartition2(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-inner-ip-segment-sync-kafka-2 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-inner-ip-segment-sync-kafka-3",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_inner_ip_segment:osa_cmdb_inner_ip_segment}",
                    partitions = { "3" }) })
    public void listenPartition3(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-inner-ip-segment-sync-kafka-3 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-inner-ip-segment-sync-kafka-4",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_inner_ip_segment:osa_cmdb_inner_ip_segment}",
                    partitions = { "4" }) })
    public void listenPartition4(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-inner-ip-segment-sync-kafka-4 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-inner-ip-segment-sync-kafka-5",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_inner_ip_segment:osa_cmdb_inner_ip_segment}",
                    partitions = { "5" }) })
    public void listenPartition5(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-inner-ip-segment-sync-kafka-5 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-inner-ip-segment-sync-kafka-6",
            containerFactory = "ackContainerFactory",
            topicPartitions = {
                    @TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_ConfigDict}", partitions = { "6" }) })
    public void listenPartition6(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-inner-ip-segment-sync-kafka-6 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-inner-ip-segment-sync-kafka-7",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_inner_ip_segment:osa_cmdb_inner_ip_segment}",
                    partitions = { "7" }) })
    public void listenPartition7(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-inner-ip-segment-sync-kafka-7 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-inner-ip-segment-sync-kafka-8",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_inner_ip_segment:osa_cmdb_inner_ip_segment}",
                    partitions = { "8" }) })
    public void listenPartition8(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-inner-ip-segment-sync-kafka-8 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-inner-ip-segment-sync-kafka-9",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_inner_ip_segment:osa_cmdb_inner_ip_segment}",
                    partitions = { "9" }) })
    public void listenPartition9(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-inner-ip-segment-sync-kafka-9 Listener, Thread ID:{}", Thread.currentThread().getId());
        super.onMessage(records, ack);
    }

    @Override
    protected CmdbSyncModuleCondition buildFieldCondition() {
        return new CmdbSyncModuleCondition(KafkaConfigConstant.MAPPING_MODULE,
                KafkaConfigConstant.MAPPING_KEY_CMDB_INNER_IP_SEGMENT, KafkaConfigConstant.MAPPING_TYPE_CMDB_INNER_IP_SEGMENT,
                KafkaConfigConstant.MAPPING_KEY_FIELD);
    }

    @Override
    protected CmdbSyncType getSyncType() {
        return CmdbSyncType.CMDB_INNER_IP_SEGMENT;
    }
}
