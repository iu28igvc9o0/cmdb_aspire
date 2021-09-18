package com.aspire.ums.cmdb.sync.service.producer;

import java.util.Arrays;
import java.util.List;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;
import com.aspire.ums.cmdb.sync.service.CmdbSyncSendMessageLogService;
import com.aspire.ums.cmdb.util.JsonMapper;

/**
 * kafkaProducer监听器，在producer配置文件中开启.
 *
 * @author jiangxuwen
 * @date 2020/5/12 13:43
 */
@Component
public class KafkaSendResultHandler implements ProducerListener {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSendResultHandler.class);

    @Autowired
    private CmdbSyncSendMessageLogService syncSendMessageLogService;

    @Override
    public void onSuccess(String topic, Integer partition, Object key, Object value, RecordMetadata recordMetadata) {
        logger.info("==========kafka发送数据成功（日志开始）==========");
        logger.info("----------topic:" + topic);
        logger.info("----------partition:" + partition);
        logger.info("----------key:" + key);
        logger.info("----------value:" + value);
        logger.info("----------RecordMetadata:" + recordMetadata);
        try {
            if (isUpdateStatus(topic)) {
                updateMsgStatus(value, "1");
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("~~~~~~~~~~kafka发送数据成功（日志结束）~~~~~~~~~~");
    }

    @Override
    public void onError(String topic, Integer partition, Object key, Object value, Exception exception) {
        logger.error("==========kafka发送数据错误（日志开始）==========");
        logger.error("----------topic:" + topic);
        logger.error("----------partition:" + partition);
        logger.error("----------key:" + key);
        logger.error("----------value:" + value);
        logger.error("----------Exception:" + exception);
        try {
            if (isUpdateStatus(topic)) {
                updateMsgStatus(value, "2");
            }
        } catch (Exception e) {
            logger.error(",e");
        }
        logger.error("~~~~~~~~~~kafka发送数据错误（日志结束）~~~~~~~~~~");
        exception.printStackTrace();
    }

    private void updateMsgStatus(Object value, String msgStatus) throws Exception {
        String msgContent = "1".equals(msgStatus) ? "成功" : "失败";

        logger.info("消息发送到kafka[{}],开始修改消息的状态...", msgContent);
        CmdbSyncSendMessageLog messageLog = new JsonMapper().readValue(value.toString(), CmdbSyncSendMessageLog.class);
        CmdbSyncSendMessageLog entity = new CmdbSyncSendMessageLog();
        entity.setId(messageLog.getId());
        entity.setMsgStatus(msgStatus);
        syncSendMessageLogService.updateStatusAndRetryCount(entity);
    }

    /**
     * 方法返回值代表是否启动kafkaProducer监听器
     */
    @Override
    public boolean isInterestedInSuccess() {
        logger.info("kafkaProducer监听器启动......");
        return true;
    }

    private boolean isUpdateStatus(String topic) {
        List<String> topicList = Arrays.asList(KafkaConfigConstant.TOPIC_CONFIG_DICT, KafkaConfigConstant.TOPIC_CMDB_DEVICE_ASSET,
                KafkaConfigConstant.TOPIC_CMDB_BUSINESS, KafkaConfigConstant.TOPIC_CMDB_BUSINESS_LINE_CONTACT,
                KafkaConfigConstant.TOPIC_CMDB_DEVICE_OTHER_IP, KafkaConfigConstant.TOPIC_CMDB_DEVICE_STORAGE);
        return topicList.contains(topic);
    }
}
