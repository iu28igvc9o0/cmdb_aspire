package com.aspire.cmdb.agent.sync.schedule.cmdb;

import com.aspire.ums.cmdb.automate.service.AutomateService;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author fanwenhui
 * @date 2020-08-27 16:59
 * @description 自动化模型同步
 */
@Component
@Slf4j
public class CmdbAutomateSynConsumer {

    @Autowired
    private AutomateService automateService;

    @KafkaListener(id = "automate-host-syn-kafka-0",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.topic.automate_host_syn:automate_host_syn}",
                    partitions = { "0" }) })
    public void listenPartition0(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("automate-host-syn-kafka-0 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("automate-host-syn-kafka-0 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "automate-host-syn-kafka-1",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.topic.automate_host_syn:automate_host_syn}",
                    partitions = { "1" }) })
    public void listenPartition1(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("automate-host-syn-kafka-1 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("automate-host-syn-kafka-1 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "automate-host-syn-kafka-2",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.topic.automate_host_syn:automate_host_syn}",
                    partitions = { "2" }) })
    public void listenPartition2(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("automate-host-syn-kafka-2 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("automate-host-syn-kafka-2 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }


    private void onMessage(List<ConsumerRecord<?, ?>> records) throws Exception {
        if (CollectionUtils.isEmpty(records)) {
            log.info("message is empty!!!");
            return;
        }
        log.info(">>>>>>>>>> [主机配置模型]收到消息，准备处理，数量为:{} >>>>>>",records.size());
        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                Map<String,String> synMap = (Map<String,String>)new JsonMapper().readValue(message.toString(), Map.class);
                String content = synMap.get("requestContent");
                String synLogId = synMap.get("synLogId");
                String type = synMap.get("type");
                String topic = record.topic();
                Object key = record.key();
                long offset = record.offset();
                int partition = record.partition();
                log.info("参数：topic:[{}],key:[{}],offset:[{}],partition:[{}]", topic, key, offset, partition);
                handleMsg(content,type,synLogId);
            }
            log.info("<<<<<<<< [自动化主机模型] 消息处理完成 <<<<<<<<<<<<<");
        }
    }

    private void handleMsg(String value,String type,String synLogId){
        try {
            if (StringUtils.isNotEmpty(value)) {
                if ("create".equals(type)) {
                    automateService.instanceCreateByJson(value,synLogId);
                } else if ("update".equals(type)) {
                    automateService.instanceUpdateByJson(value,synLogId);
                } else if ("delete".equals(type)) {
                    automateService.instanceDeleteByJson(value,synLogId);
                }
            }
        } catch (Exception e) {
            log.error("[主机配置模型] 入库失败!", e);
        }
    }
}
