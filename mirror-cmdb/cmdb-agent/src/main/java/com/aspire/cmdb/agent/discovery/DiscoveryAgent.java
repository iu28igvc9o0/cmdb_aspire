package com.aspire.cmdb.agent.discovery;

import com.aspire.cmdb.agent.config.Const;
import com.aspire.cmdb.agent.discovery.icmp.PingAgent;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DiscoverAgent
 * Author:   zhu.juwang
 * Date:     2019/4/24 17:48
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
//@Component
@Slf4j
//@EnableScheduling
public class DiscoveryAgent {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 监控topic=cmdb_discovery_info_request partition = 0请求信息
     * @param consumer 消费信息
     */
    @KafkaListener(id = "discovery-0", topicPartitions = {@TopicPartition(topic = "cmdb_discovery_info_request", partitions = {"0"})})
    public void listenPartition0(String consumer) {
        log.info("Discovery-0 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), consumer);
        execute("Discovery-0", consumer);
    }

    @KafkaListener(id = "discovery-1", topicPartitions = {@TopicPartition(topic = "cmdb_discovery_info_request", partitions = {"1"})})
    public void listenPartition1(String consumer) {
        log.info("Discovery-1 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), consumer);
        execute("Discovery-1", consumer);
    }

    @KafkaListener(id = "discovery-2", topicPartitions = {@TopicPartition(topic = "cmdb_discovery_info_request", partitions = {"2"})})
    public void listenPartition2(String consumer) {
        log.info("Discovery-2 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), consumer);
        execute("Discovery-2", consumer);
    }

    @KafkaListener(id = "discovery-3", topicPartitions = {@TopicPartition(topic = "cmdb_discovery_info_request", partitions = {"3"})})
    public void listenPartition3(String consumer) {
        log.info("Discovery-3 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), consumer);
        execute("Discovery-3", consumer);
    }

    @KafkaListener(id = "discovery-4", topicPartitions = {@TopicPartition(topic = "cmdb_discovery_info_request", partitions = {"4"})})
    public void listenPartition4(String consumer) {
        log.info("Discovery-4 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), consumer);
        execute("Discovery-4", consumer);
    }

    @KafkaListener(id = "discovery-5", topicPartitions = {@TopicPartition(topic = "cmdb_discovery_info_request", partitions = {"5"})})
    public void listenPartition5(String consumer) {
        log.info("Discovery-5 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), consumer);
        execute("Discovery-5", consumer);
    }

    @KafkaListener(id = "discovery-6", topicPartitions = {@TopicPartition(topic = "cmdb_discovery_info_request", partitions = {"6"})})
    public void listenPartition6(String consumer) {
        log.info("Discovery-6 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), consumer);
        execute("Discovery-6", consumer);
    }

    @KafkaListener(id = "discovery-7", topicPartitions = {@TopicPartition(topic = "cmdb_discovery_info_request", partitions = {"7"})})
    public void listenPartition7(String consumer) {
        log.info("Discovery-7 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), consumer);
        execute("Discovery-7", consumer);
    }

    @KafkaListener(id = "discovery-8", topicPartitions = {@TopicPartition(topic = "cmdb_discovery_info_request", partitions = {"8"})})
    public void listenPartition8(String consumer) {
        log.info("Discovery-8 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), consumer);
        execute("Discovery-8", consumer);
    }

    @KafkaListener(id = "discovery-9", topicPartitions = {@TopicPartition(topic = "cmdb_discovery_info_request", partitions = {"9"})})
    public void listenPartition9(String consumer) {
        log.info("Discovery-9 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), consumer);
        execute("Discovery-9", consumer);
    }

    /**
     * 处理kafka信息
     * @param consumer kafka信息
     */
    private void execute(String id ,String consumer) {
        log.info("{} Received: {}", id, consumer);
        if (StringUtils.isNotEmpty(consumer)) {
            JSONObject jsonData = JSONObject.fromObject(consumer);
            String ruleId = jsonData.getString("rule_id");
            String room = jsonData.getString("room");
            String discoveryType = jsonData.getString("discovery_type");
            JSONObject returnJson = new JSONObject();
            returnJson.put("rule_id", ruleId);
            AbstractAgent agent = null;
            if (Const.DISCOVERY_TYPE_PING.equals(discoveryType)) {
                agent = new PingAgent(jsonData);
                agent.start();
            } else if (Const.DISCOVERY_TYPE_SNMP.equals(discoveryType)) {
                //todo 待处理
            } else {
                returnJson.put("flag", false);
                returnJson.put("message", "不支持[" + discoveryType+ "]发现方式!");
            }
            if (agent != null) {
                List<String> coveryIpList = agent.getDiscoveryIpList();
                JSONArray dataList = new JSONArray();
                for (String ip : coveryIpList) {
                    JSONObject data = new JSONObject();
                    data.put("ip", ip);
                    data.put("room", room);
                    dataList.add(data);
                }
                returnJson.put("data_list", dataList);
            }
            //发送消息给Kafka
            log.info("Return rule id -> {} discovery data -> {}", ruleId, returnJson.toString());
            kafkaTemplate.send("cmdb_discovery_info_response", returnJson.toString());
        }
    }
}
