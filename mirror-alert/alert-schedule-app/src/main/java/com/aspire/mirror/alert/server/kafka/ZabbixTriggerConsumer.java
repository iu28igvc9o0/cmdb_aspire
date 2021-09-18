package com.aspire.mirror.alert.server.kafka;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.alert.server.biz.helper.CmdbHelper;
import com.aspire.mirror.alert.server.clientservice.ZabbixTriggerServiceClient;
import com.aspire.mirror.elasticsearch.api.dto.TriggerDto;

import lombok.extern.slf4j.Slf4j;

/**
 * @author baiwp
 * @title: ZabbixTriggerConsumer
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/6/2515:24
 */
@Slf4j
@Component
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
public class ZabbixTriggerConsumer {
    @Autowired
    private ZabbixTriggerServiceClient zabbixTriggerServiceClient;
    @Autowired
    private CmdbHelper cmdbHelper;

//    @KafkaListener(topics = "${kafka.topic.topic_zabbix_trigger:TOPIC_ZABBIX_TRIGGER}",group = "consumer_alert_ES")
    public void listen(ConsumerRecord<?, String> cr) throws Exception {
        Long time1 = System.currentTimeMillis();
        String value = cr.value();
        List<TriggerDto> triggerDtoList = JSON.parseArray(value, TriggerDto.class);
        for (TriggerDto triggerDto:triggerDtoList) {
            triggerDto.setIdc(cmdbHelper.getIdc(triggerDto.getProxyName()));
        }
        zabbixTriggerServiceClient.insert(triggerDtoList);
        log.info("------------------consumer trigger use : {} ms,partition:{}", (System.currentTimeMillis()-time1), cr.partition());
    }
}
