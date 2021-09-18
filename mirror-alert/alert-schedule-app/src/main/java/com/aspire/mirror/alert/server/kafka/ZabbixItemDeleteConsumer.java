package com.aspire.mirror.alert.server.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.aspire.mirror.alert.server.clientservice.ZabbixItemServiceClient;
import com.aspire.mirror.alert.server.clientservice.ZabbixTriggerServiceClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @author baiwp
 * @title: ZabbixItemDeleteListener
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/6/2515:07
 */
@Slf4j
@Component
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
public class ZabbixItemDeleteConsumer {

    @Autowired
    private ZabbixItemServiceClient zabbixItemServiceClient;
    @Autowired
    private ZabbixTriggerServiceClient zabbixTriggerServiceClient;
    /**
     *
     * @param cr
     * @throws Exception
     */
    @KafkaListener(topics = "${kafka.topic.topic_zabbix_delete:TOPIC_ZABBIX_DELETE}",group = "consumer_alert_ES")
    public void listen(ConsumerRecord<?, String> cr) throws Exception {
        String value = cr.value();
        log.info("consumer message: {}", value);
        //接到消息不处理，直接清除监控项和触发器的数据
        try {
            zabbixItemServiceClient.deleteAll();
        } catch (Exception e) {
            Thread.sleep(20000);
            try {
                zabbixItemServiceClient.deleteAll();
            } catch (Exception e1) {

            }
        }
        // 休眠20s
        Thread.sleep(20000);

        try {
            zabbixTriggerServiceClient.deleteAll();
        } catch (Exception e) {
            Thread.sleep(20000);
            try {
                zabbixTriggerServiceClient.deleteAll();
            } catch (Exception e1) {

            }
        }
    }
}
