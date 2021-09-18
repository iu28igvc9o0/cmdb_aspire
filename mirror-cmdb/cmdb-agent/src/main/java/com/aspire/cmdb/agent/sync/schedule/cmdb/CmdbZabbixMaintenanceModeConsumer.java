package com.aspire.cmdb.agent.sync.schedule.cmdb;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;
import com.aspire.ums.cmdb.sync.entity.CmdbZabbixMaintenance;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.sync.service.CmdbSyncReceiveMessageLogService;
import com.aspire.ums.cmdb.sync.service.CmdbZabbixMaintenanceService;
import com.aspire.ums.cmdb.util.JsonMapper;

/**
 * 工程模式.
 *
 * @author jiangxuwen
 * @date 2020/5/18 18:50
 */
@Component
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class CmdbZabbixMaintenanceModeConsumer {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CmdbSyncReceiveMessageLogService receiveMessageLogService;

    @Autowired
    private CmdbZabbixMaintenanceService cmdbZabbixMaintenanceService;

    @KafkaListener(id = "cmdb-maintenance-mode-sync-kafka-0",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_maintenance_mode:osa_cmdb_maintenance_mode}",
                    partitions = { "0" }) })
    public void listenPartition0(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-maintenance-mode-sync-kafka-0 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-maintenance-mode-sync-kafka-1",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_maintenance_mode:osa_cmdb_maintenance_mode}",
                    partitions = { "1" }) })
    public void listenPartition1(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-maintenance-mode-sync-kafka-1 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-maintenance-mode-sync-kafka-2",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_maintenance_mode:osa_cmdb_maintenance_mode}",
                    partitions = { "2" }) })
    public void listenPartition2(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-maintenance-mode-sync-kafka-2 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-maintenance-mode-sync-kafka-3",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_maintenance_mode:osa_cmdb_maintenance_mode}",
                    partitions = { "3" }) })
    public void listenPartition3(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-maintenance-mode-sync-kafka-3 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-maintenance-mode-sync-kafka-4",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_maintenance_mode:osa_cmdb_maintenance_mode}",
                    partitions = { "4" }) })
    public void listenPartition4(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-maintenance-mode-sync-kafka-4 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-maintenance-mode-sync-kafka-5",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_maintenance_mode:osa_cmdb_maintenance_mode}",
                    partitions = { "5" }) })
    public void listenPartition5(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-maintenance-mode-sync-kafka-5 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-maintenance-mode-sync-kafka-6",
            containerFactory = "ackContainerFactory",
            topicPartitions = {
                    @TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_ConfigDict}", partitions = { "6" }) })
    public void listenPartition6(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-maintenance-mode-sync-kafka-6 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-maintenance-mode-sync-kafka-7",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_maintenance_mode:osa_cmdb_maintenance_mode}",
                    partitions = { "7" }) })
    public void listenPartition7(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-maintenance-mode-sync-kafka-7 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-maintenance-mode-sync-kafka-8",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_maintenance_mode:osa_cmdb_maintenance_mode}",
                    partitions = { "8" }) })
    public void listenPartition8(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-maintenance-mode-sync-kafka-8 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-maintenance-mode-sync-kafka-9",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_maintenance_mode:osa_cmdb_maintenance_mode}",
                    partitions = { "9" }) })
    public void listenPartition9(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-maintenance-mode-sync-kafka-9 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    private void onMessage(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        try {
            if (CollectionUtils.isEmpty(records)) {
                log.info("[工程模式配置同步] message is empty!!!");
                return;
            }
            log.info(">>>>>>>>>> [工程模式配置同步]收到消息，准备处理 >>>>>>");
            log.info("[工程模式配置同步]收到消息的数量:{}", records.size());
            for (ConsumerRecord<?, ?> record : records) {
                Optional<?> kafkaMessage = Optional.ofNullable(record.value());
                if (kafkaMessage.isPresent()) {
                    Object message = record.value();
                    String topic = record.topic();
                    Object key = record.key();
                    long offset = record.offset();
                    int partition = record.partition();
                    log.info("[工程模式配置同步] 参数：topic:[{}],key:[{}],offset:[{}],partition:[{}]", topic, key, offset, partition);
                    log.info("[工程模式配置同步] 消息内容为:{}", message);
                    handleMsg(message.toString());
                }
                log.info("<<<<<<<< [工程模式配置同步] 消息处理完成 <<<<<<<<<<<<<");
            }
        } catch (Exception e) {
            log.error("[工程模式配置同步]失败.", e);
        } finally {
            ack.acknowledge();
        }

    }

    private void handleMsg(String value) {
        try {
            CmdbSyncSendMessageLog messageLog = JsonMapper.getInstance().readValue(value, CmdbSyncSendMessageLog.class);
            receiveMessageLogService.saveReceiveLog(messageLog);
            String objectName = messageLog.getObjectName();
            String eventType = messageLog.getEventType();
            CmdbOptType optType = CmdbOptType.fromValue(eventType);
            log.info("[工程模式配置同步]开始入库,操作类型:[{}],操作表[{}]", optType.getLabel(), objectName);
            saveMaintenanceMode(messageLog);
            log.info("[工程模式配置同步] 入库[{}]成功!", objectName);
        } catch (Exception e) {
            log.error("[工程模式配置同步]入库失败!", e);
        }
    }

    private void saveMaintenanceMode(CmdbSyncSendMessageLog messageLog) throws Exception {
        String msgBody = messageLog.getMsgBody();
        CmdbZabbixMaintenance maintenance = JsonMapper.getInstanceWithSnakeCase().readValue(msgBody, CmdbZabbixMaintenance.class);
        String eventType = messageLog.getEventType();
        CmdbOptType optType = CmdbOptType.fromValue(eventType);
        if (CmdbOptType.OPT_ADD == optType) {
            cmdbZabbixMaintenanceService.add(maintenance);
        } else if (CmdbOptType.OPT_MODIFY == optType) {
            cmdbZabbixMaintenanceService.modify(maintenance);
        } else if (CmdbOptType.OPT_DEL == optType) {
            cmdbZabbixMaintenanceService.delete(maintenance.getId());
        }
    }

}
