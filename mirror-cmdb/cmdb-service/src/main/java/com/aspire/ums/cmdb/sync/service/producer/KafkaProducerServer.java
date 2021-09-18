package com.aspire.ums.cmdb.sync.service.producer;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.config.KafkaMesConstant;
import com.aspire.ums.cmdb.config.SyncOsaConfig;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.google.common.collect.Maps;

/**
 * kafkaProducer 生产者 模板 使用此模板发送消息
 *
 * @author jiangxuwen
 * @date 2020/5/12 14:23
 */
@Component
@Slf4j
public class KafkaProducerServer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private KafkaSendResultHandler producerListener;

    @Autowired
    private SyncOsaConfig syncOsaConfig;

    public Map<String, Object> sendMsgForTemplate(CmdbSyncSendMessageLog cmdbSyncSendMessageLog) {
        kafkaTemplate.setProducerListener(producerListener);
        String valueString = JsonMapper.getInstance().toJson(cmdbSyncSendMessageLog);
        // 表示使用分区
        int partitionIndex = getPartitionIndex(cmdbSyncSendMessageLog.getObjectKey(), syncOsaConfig.getPartitionNum());
        ListenableFuture<SendResult<String, Object>> result = kafkaTemplate.send(cmdbSyncSendMessageLog.getTopic(), partitionIndex,
                cmdbSyncSendMessageLog.getObjectId(), valueString);
        Map<String, Object> res = checkProRecord(result);
        log.info("发送消息结果：{}", res);
        return res;
    }

    /**
     * kafka发送消息模板
     * 
     * @param topic
     *            主题
     * @param value
     *            messageValue
     * @param ifPartition
     *            是否使用分区 0是\1不是
     * @param partitionNum
     *            分区数 如果是否使用分区为0,分区数必须大于0
     */
    public Map<String, Object> sndMesForTemplate(String topic, Object value, String ifPartition, Integer partitionNum, String key) {
        // String key = role + "-" + value.hashCode();
        kafkaTemplate.setProducerListener(producerListener);
        String valueString = JSON.toJSONString(value);
        if (ifPartition.equals("0")) {
            // 表示使用分区
            int partitionIndex = getPartitionIndex(key, partitionNum);
            ListenableFuture<SendResult<String, Object>> result = kafkaTemplate.send(topic, partitionIndex, key, valueString);
            Map<String, Object> res = checkProRecord(result);
            return res;
        } else {
            ListenableFuture<SendResult<String, Object>> result = kafkaTemplate.send(topic, key, valueString);
            Map<String, Object> res = checkProRecord(result);
            return res;
        }
    }

    /**
     * 根据key值获取分区索引
     * 
     * @param key
     * @param partitionNum
     * @return
     */
    private int getPartitionIndex(String key, int partitionNum) {
        if (key == null) {
            SecureRandom random = null;
            try {
                random = SecureRandom.getInstance("SHA1PRNG");
//                Random random = new Random();
                return random.nextInt(partitionNum);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return 1;
        } else {
            int result = Math.abs(key.hashCode()) % partitionNum;
            return result;
        }
    }

    /**
     * 检查发送返回结果record
     * 
     * @param res
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Map<String, Object> checkProRecord(ListenableFuture<SendResult<String, Object>> res) {
        Map<String, Object> m = Maps.newHashMap();
        if (res != null) {
            try {
                SendResult r = res.get();// 检查result结果集
                /* 检查recordMetadata的offset数据，不检查producerRecord */
                Long offsetIndex = r.getRecordMetadata().offset();
                if (offsetIndex != null && offsetIndex >= 0) {
                    m.put("code", KafkaMesConstant.SUCCESS_CODE);
                    m.put("message", KafkaMesConstant.SUCCESS_MES);
                    return m;
                } else {
                    m.put("code", KafkaMesConstant.KAFKA_NO_OFFSET_CODE);
                    m.put("message", KafkaMesConstant.KAFKA_NO_OFFSET_MES);
                    return m;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                m.put("code", KafkaMesConstant.KAFKA_SEND_ERROR_CODE);
                m.put("message", KafkaMesConstant.KAFKA_SEND_ERROR_MES);
                return m;
            } catch (ExecutionException e) {
                e.printStackTrace();
                m.put("code", KafkaMesConstant.KAFKA_SEND_ERROR_CODE);
                m.put("message", KafkaMesConstant.KAFKA_SEND_ERROR_MES);
                return m;
            }
        } else {
            m.put("code", KafkaMesConstant.KAFKA_NO_RESULT_CODE);
            m.put("message", KafkaMesConstant.KAFKA_NO_RESULT_MES);
            return m;
        }
    }
}
