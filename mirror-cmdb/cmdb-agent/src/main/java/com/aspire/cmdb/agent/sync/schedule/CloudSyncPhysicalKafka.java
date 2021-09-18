package com.aspire.cmdb.agent.sync.schedule;

import com.aspire.cmdb.agent.sync.service.ICloudSyncService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CloudSyncKafka
 * Author:   zhu.juwang
 * Date:     2019/11/28 18:44
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Component
@Slf4j
public class CloudSyncPhysicalKafka {
    @Autowired
    private ICloudSyncService cloudSyncService;

    @KafkaListener(id = "cloud-physical-sync-kafka-0", topicPartitions = {@TopicPartition(topic = "${kafka.topic.suyan_physical_sync:SUYAN_PHYSICAL_SYNC}", partitions = {"0"})})
    public void listenZbxPartition0(ConsumerRecord<?, String> cr) {
//        log.info("cloud-physical-sync-kafka-0 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        cloudSyncService.syncData(cr.value());
    }
    @KafkaListener(id = "cloud-physical-sync-kafka-1", topicPartitions = {@TopicPartition(topic = "${kafka.topic.suyan_physical_sync:SUYAN_PHYSICAL_SYNC}", partitions = {"1"})})
    public void listenZbxPartition1(ConsumerRecord<?, String> cr) {
//        log.info("cloud-physical-sync-kafka-1 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        cloudSyncService.syncData(cr.value());
    }
    @KafkaListener(id = "cloud-physical-sync-kafka-2", topicPartitions = {@TopicPartition(topic = "${kafka.topic.suyan_physical_sync:SUYAN_PHYSICAL_SYNC}", partitions = {"2"})})
    public void listenZbxPartition2(ConsumerRecord<?, String> cr) {
//        log.info("cloud-physical-sync-kafka-2 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        cloudSyncService.syncData(cr.value());
    }
    @KafkaListener(id = "cloud-physical-sync-kafka-3", topicPartitions = {@TopicPartition(topic = "${kafka.topic.suyan_physical_sync:SUYAN_PHYSICAL_SYNC}", partitions = {"3"})})
    public void listenZbxPartition3(ConsumerRecord<?, String> cr) {
//        log.info("cloud-physical-sync-kafka-3 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        cloudSyncService.syncData(cr.value());
    }
    @KafkaListener(id = "cloud-physical-sync-kafka-4", topicPartitions = {@TopicPartition(topic = "${kafka.topic.suyan_physical_sync:SUYAN_PHYSICAL_SYNC}", partitions = {"4"})})
    public void listenZbxPartition4(ConsumerRecord<?, String> cr) {
//        log.info("cloud-physical-sync-kafka-4 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        cloudSyncService.syncData(cr.value());
    }
    @KafkaListener(id = "cloud-physical-sync-kafka-5", topicPartitions = {@TopicPartition(topic = "${kafka.topic.suyan_physical_sync:SUYAN_PHYSICAL_SYNC}", partitions = {"5"})})
    public void listenZbxPartition5(ConsumerRecord<?, String> cr) {
//        log.info("cloud-physical-sync-kafka-5 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        cloudSyncService.syncData(cr.value());
    }
    @KafkaListener(id = "cloud-physical-sync-kafka-6", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Zbx}", partitions = {"6"})})
    public void listenZbxPartition6(ConsumerRecord<?, String> cr) {
//        log.info("cloud-physical-sync-kafka-6 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        cloudSyncService.syncData(cr.value());
    }
    @KafkaListener(id = "cloud-physical-sync-kafka-7", topicPartitions = {@TopicPartition(topic = "${kafka.topic.suyan_physical_sync:SUYAN_PHYSICAL_SYNC}", partitions = {"7"})})
    public void listenZbxPartition7(ConsumerRecord<?, String> cr) {
//        log.info("cloud-physical-sync-kafka-7 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        cloudSyncService.syncData(cr.value());
    }
    @KafkaListener(id = "cloud-physical-sync-kafka-8", topicPartitions = {@TopicPartition(topic = "${kafka.topic.suyan_physical_sync:SUYAN_PHYSICAL_SYNC}", partitions = {"8"})})
    public void listenZbxPartition8(ConsumerRecord<?, String> cr) {
//        log.info("cloud-physical-sync-kafka-8 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        cloudSyncService.syncData(cr.value());
    }
    @KafkaListener(id = "cloud-physical-sync-kafka-9", topicPartitions = {@TopicPartition(topic = "${kafka.topic.suyan_physical_sync:SUYAN_PHYSICAL_SYNC}", partitions = {"9"})})
    public void listenZbxPartition9(ConsumerRecord<?, String> cr) {
//        log.info("cloud-physical-sync-kafka-9 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        cloudSyncService.syncData(cr.value());
    }
}
