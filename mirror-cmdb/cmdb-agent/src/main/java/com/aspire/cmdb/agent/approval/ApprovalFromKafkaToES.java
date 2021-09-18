package com.aspire.cmdb.agent.approval;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aspire.ums.cmdb.client.ICmdbESClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: MonitorFromKafka
 * Author:   hangfang 资源池各主机业务监控、带外监控状态上报任务类
 * Date:     2019/8/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Component
@Slf4j
public class ApprovalFromKafkaToES {

    private static final String INDEX = "cmdb_es";
    private static final String TYPE = "approval";

    @Autowired
    private ICmdbESClient esClient;
//    private ESClient esClient;

    @KafkaListener(id = "approval-0", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_approval:cmdb_approval_info}", partitions = {"0"})})
    public void listenZbxPartition0(ConsumerRecord<?, String> cr) {
        log.info("approval-0 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }

    @KafkaListener(id = "approval-1", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_approval:cmdb_approval_info}", partitions = {"1"})})
    public void listenZbxPartition1(ConsumerRecord<?, String> cr) {
        log.info("approval-1 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "approval-2", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_approval:cmdb_approval_info}", partitions = {"2"})})
    public void listenZbxPartition2(ConsumerRecord<?, String> cr) {
        log.info("approval-2 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "approval-3", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_approval:cmdb_approval_info}", partitions = {"3"})})
    public void listenZbxPartition3(ConsumerRecord<?, String> cr) {
        log.info("approval-3 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "approval-4", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_approval:cmdb_approval_info}", partitions = {"4"})})
    public void listenZbxPartition4(ConsumerRecord<?, String> cr) {
        log.info("approval-4 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "approval-5", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_approval:cmdb_approval_info}", partitions = {"5"})})
    public void listenZbxPartition5(ConsumerRecord<?, String> cr) {
        log.info("approval-5 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "approval-6", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_approval:cmdb_approval_info}", partitions = {"6"})})
    public void listenZbxPartition6(ConsumerRecord<?, String> cr) {
        log.info("approval-6 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "approval-7", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_approval:cmdb_approval_info}", partitions = {"7"})})
    public void listenZbxPartition7(ConsumerRecord<?, String> cr) {
        log.info("approval-7 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "approval-8", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_approval:cmdb_approval_info}", partitions = {"8"})})
    public void listenZbxPartition8(ConsumerRecord<?, String> cr) {
        log.info("approval-8 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "approval-9", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_approval:cmdb_approval_info}", partitions = {"9"})})
    public void listenZbxPartition9(ConsumerRecord<?, String> cr) {
        log.info("approval-9 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }

    protected void resovleData(String value) {
        if (StringUtils.isNotEmpty(value)) {
            try {
                log.info("--------- 开始发送审核数据到es --------- ");
                List<Map<String, Object>> approvals = JSON.parseObject(value, new TypeReference<List<Map<String, Object>>>() {});
//                List<CmdbCollectApproval> approvals = JSONArray.parseArray(value, CmdbCollectApproval.class);
//                esClient.insert(approvals);
                esClient.insert(approvals, INDEX, TYPE);
                log.info("--------- 发送审核数据到es成功 --------- ");
            } catch (Exception e) {
                log.info("--------- 发送审核数据到es失败 --------- ");
                log.info("错误信息：{}", e.getMessage());
            }
        }
    }
}
