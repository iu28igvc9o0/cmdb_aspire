package com.aspire.mirror.alert.server.kafka;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.server.util.MapUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.alert.server.biz.helper.CmdbHelper;
import com.aspire.mirror.alert.server.clientservice.ZabbixItemServiceClient;
import com.aspire.mirror.elasticsearch.api.dto.ItemDto;

import lombok.extern.slf4j.Slf4j;

/**
 * @author baiwp
 * @title: ZabbixItemConsumer
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/6/2515:23
 */
@Slf4j
@Component
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
public class ZabbixItemConsumer {
    @Autowired
    private ZabbixItemServiceClient zabbixItemServiceClient;
    @Autowired
    private CmdbHelper cmdbHelper;

    @KafkaListener(topics = "${kafka.topic.topic_zabbix_item:TOPIC_ZABBIX_ITEM}",group = "consumer_alert_ES")
    public void listen(ConsumerRecord<?, String> cr) throws Exception {
        Long time1 = System.currentTimeMillis();
        String value = cr.value();
        List<ItemDto> itemDtoList = JSON.parseArray(value, ItemDto.class);
        for (ItemDto itemDto:itemDtoList) {
            String idcName = cmdbHelper.getIdc(itemDto.getProxyName());
            itemDto.setIdcType(idcName);
            Map<String, Object> cmdbInstance = cmdbHelper.queryDeviceByRoomIdAndIP(idcName, itemDto.getIp());
            if (null != cmdbInstance) {
                itemDto.setIdcType(MapUtils.getString(cmdbInstance, "idcType"));
                itemDto.setBizSystem(MapUtils.getString(cmdbInstance, "bizSystem"));
                itemDto.setRoomId(MapUtils.getString(cmdbInstance, "roomId"));
                itemDto.setDeviceClass(MapUtils.getString(cmdbInstance, "device_class"));
                itemDto.setDeviceType(MapUtils.getString(cmdbInstance, "device_type"));
                itemDto.setResourceId(MapUtils.getString(cmdbInstance, "instance_id"));
            }
        }
        zabbixItemServiceClient.insert(itemDtoList);
        log.info("------------------consumer ITEM use : {} ms,partition:{}", (System.currentTimeMillis()-time1), cr.partition());
    }
}
