package com.aspire.ums.cmdb.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: KafkaProducerFactory
 * Author:   hangfang
 * Date:     2020/9/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Data
public class KafkaProducerFactory {

    private String kafkaAddress;

    private String topic;

    private Integer partition;

    private Producer producer;

    public KafkaProducerFactory(String kafkaAddress, String topic, Integer partition) {
        this.kafkaAddress = kafkaAddress;
        this.topic = topic;
        this.partition = partition;
    }

    /**
     * 初始化producer对象
     * */
    public Producer createProducer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaAddress);
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.producer = new KafkaProducer<String, String>(props);
        return this.producer;
    }

    public void sendMessage(Producer producer, String topic, Integer partition, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition,key, value);
        try{
            List<PartitionInfo> partitionInfos = producer.partitionsFor(topic);
            if (partition != null && partitionInfos.size() < partition) {
                log.error("分区{}超出范围，topic:{}分区在[0,{})之间", partition, topic, partitionInfos.size());
                throw new RuntimeException("分区" + partition + "超出范围，topic:" + topic + "分区在[0," + partitionInfos.size() + ")之间");
            }
            producer.send(record).get();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
