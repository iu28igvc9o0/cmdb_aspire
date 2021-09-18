package com.aspire.cmdb.agent.sync.schedule.cmdb;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.aspire.cmdb.agent.util.StringUtils;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;
import com.aspire.ums.cmdb.sync.payload.PublicNetAndIntranetIPDTO;
import com.aspire.ums.cmdb.sync.service.CmdbSyncReceiveMessageLogService;
import com.aspire.ums.cmdb.sync.service.ICmdbNetPolicyManageService;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.fasterxml.jackson.databind.JavaType;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 公网、防火墙数据同步
 * 网管旧cmdb->新cmdb
 * @author cuizhijun
 *  cmdb-netpolicy-manage-sync
 */
@Data
@Component
@Slf4j
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class CmdbNetPolicyManageSyncConsumer{

    @Autowired
    private ICmdbNetPolicyManageService cmdbNetPolicyManageService;

    @Autowired
    private CmdbSyncReceiveMessageLogService receiveMessageLogService;

    @KafkaListener(id = "cmdb-netpolicy-manage-sync-kafka-0",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_netpolicy_manage:osa_cmdb_netpolicy_manage}",
                    partitions = { "0" }) })
    public void listenPartition0(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-netpolicy-manage-sync-kafka-0 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("cmdb-netpolicy-manage-sync-kafka-0 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-netpolicy-manage-sync-kafka-1",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_netpolicy_manage:osa_cmdb_netpolicy_manage}",
                    partitions = { "1" }) })
    public void listenPartition1(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-netpolicy-manage-sync-kafka-1 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("cmdb-netpolicy-manage-sync-kafka-1 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-netpolicy-manage-sync-kafka-2",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_netpolicy_manage:osa_cmdb_netpolicy_manage}",
                    partitions = { "2" }) })
    public void listenPartition2(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-netpolicy-manage-sync-kafka-2 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("cmdb-netpolicy-manage-sync-kafka-2 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-netpolicy-manage-sync-kafka-3",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_netpolicy_manage:osa_cmdb_netpolicy_manage}",
                    partitions = { "3" }) })
    public void listenPartition3(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-netpolicy-manage-sync-kafka-3 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("cmdb-netpolicy-manage-sync-kafka-3 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-netpolicy-manage-sync-kafka-4",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_netpolicy_manage:osa_cmdb_netpolicy_manage}",
                    partitions = { "4" }) })
    public void listenPartition4(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-netpolicy-manage-sync-kafka-4 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("cmdb-netpolicy-manage-sync-kafka-4 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-netpolicy-manage-sync-kafka-5",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_netpolicy_manage:osa_cmdb_netpolicy_manage}",
                    partitions = { "5" }) })
    public void listenPartition5(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-netpolicy-manage-sync-kafka-5 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("cmdb-netpolicy-manage-sync-kafka-5 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-netpolicy-manage-sync-kafka-6",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_netpolicy_manage:osa_cmdb_netpolicy_manage}",
                    partitions = { "6" }) })
    public void listenPartition6(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-netpolicy-manage-sync-kafka-6 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("cmdb-netpolicy-manage-sync-kafka-6 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-netpolicy-manage-sync-kafka-7",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_netpolicy_manage:osa_cmdb_netpolicy_manage}",
                    partitions = { "7" }) })
    public void listenPartition7(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-netpolicy-manage-sync-kafka-7 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("cmdb-netpolicy-manage-sync-kafka-7 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-netpolicy-manage-sync-kafka-8",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_netpolicy_manage:osa_cmdb_netpolicy_manage}",
                    partitions = { "8" }) })
    public void listenPartition8(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-netpolicy-manage-sync-kafka-8 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("cmdb-netpolicy-manage-sync-kafka-8 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "cmdb-netpolicy-manage-sync-kafka-9",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_netpolicy_manage:osa_cmdb_netpolicy_manage}",
                    partitions = { "9" }) })
    public void listenPartition9(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-netpolicy-manage-sync-kafka-9 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("cmdb-netpolicy-manage-sync-kafka-9 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    private void onMessage(List<ConsumerRecord<?, ?>> records) {
        if (CollectionUtils.isEmpty(records)) {
            log.info("message is empty!!!");
            return;
        }
        log.info(">>>>>>>>>> [公网、防火墙数据同步]收到消息，准备处理 >>>>>>");
        log.info("[公网、防火墙数据同步] 收到消息的数量:{}", records.size());
        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();
                Object key = record.key();
                long offset = record.offset();
                int partition = record.partition();
                log.info("参数：topic:[{}],key:[{}],offset:[{}],partition:[{}]", topic, key, offset, partition);
                log.info("消息内容为:{}", message);
                handleMsg(message.toString());
            }
            log.info("<<<<<<<< [公网、防火墙数据同步] 消息处理完成 <<<<<<<<<<<<<");
        }
    }

    private void handleMsg(String value) {
        try {
            CmdbSyncSendMessageLog messageLog = JsonMapper.getInstanceWithSnakeCase()
                    .readValue(value, CmdbSyncSendMessageLog.class);
            receiveMessageLogService.saveReceiveLog(messageLog);
            String objectName = messageLog.getObjectName();
            //TODO 暂时只处理公网内网数据同步
            if("cmdb_pubnet_mapped_ipinfo".equals(objectName)) {
            	log.info("[公网、防火墙数据同步] 开始处理表[{}]数据入库", objectName);
            	save(messageLog);
            	log.info("[公网、防火墙数据同步] 入库[{}]成功!", objectName);
            }
        } catch (Exception e) {
            log.error("[公网、防火墙数据同步] 入库失败!", e);
        }
    }

	private void save(CmdbSyncSendMessageLog messageLog) throws Exception {
		String msgBody = messageLog.getMsgBody();
		if (StringUtils.isEmpty(msgBody)) {
			log.info("[公网、防火墙数据同步] msgBody 为空!");
			return;
		}
		JsonMapper instance = JsonMapper.getInstance();
		JavaType collectionType = instance.createCollectionType(List.class, PublicNetAndIntranetIPDTO.class);
		List<PublicNetAndIntranetIPDTO> publicAndInnerIpList = instance.fromJson(msgBody, collectionType);
		cmdbNetPolicyManageService.batchInsertAndDelOldData(publicAndInnerIpList);
		cmdbNetPolicyManageService.updatePublicIPSurvivalStatus(publicAndInnerIpList);
		
	}
    
    
    
}
