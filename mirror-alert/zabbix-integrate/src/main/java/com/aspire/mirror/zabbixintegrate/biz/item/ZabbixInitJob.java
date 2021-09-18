package com.aspire.mirror.zabbixintegrate.biz.item;

import com.aspire.mirror.zabbixintegrate.dao.ZabbixDao;
import com.aspire.mirror.zabbixintegrate.domain.AlertModel;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixAlertScanIndex;
import com.aspire.mirror.zabbixintegrate.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author baiwp
 * @title: ZabbixItemInitJob
 * @projectName zabbix-integrate
 * @description: 用于每天一次清除es监控数据
 * @date 2019/6/2414:48
 */
@Slf4j
@Component
public class ZabbixInitJob {

    @Value("${zabbix.init_config.is_init_report: false}")
    private boolean isInitReport;
    @Value("${zabbix.init_config.topicName}")
    private String							topicName;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ZabbixDao dao;

    @PostConstruct
    private void init() {
        if(isInitReport) {  //如果设置立即处理，则重置es数据，重新导入
            kafkaTemplate.send(topicName,0,"{\"clean\":\"true\"}");
            log.info("发送数据到kafka，清除es中监控项和触发器数据");
            try {
                //kafka批量消费周期300s，所以设置310s，防止删除错误
                Thread.currentThread().sleep(310000);
            } catch (InterruptedException e) {
            }
            ZabbixAlertScanIndex itemData = new ZabbixAlertScanIndex();
            itemData.setId(ZabbixAlertScanIndex.ITEM_ID);
            itemData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            itemData.setScanIndex(0L);
            dao.updateScanIndex(itemData);
            ZabbixAlertScanIndex trendsData = new ZabbixAlertScanIndex();
            trendsData.setId(ZabbixAlertScanIndex.TRIGGER_ID);
            trendsData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            trendsData.setScanIndex(0L);
            dao.updateScanIndex(trendsData);
        } else {

        }
    }

    @Scheduled(cron = "${zabbix.init_config.cron: 0 0 1 * * ?}")
    public void run() {
        kafkaTemplate.send(topicName,0,"{\"clean\":\"true\"}");
        try {
            Thread.currentThread().sleep(310000);
        } catch (InterruptedException e) {
        }
        ZabbixAlertScanIndex itemData = new ZabbixAlertScanIndex();
        itemData.setId(ZabbixAlertScanIndex.ITEM_ID);
        itemData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        itemData.setScanIndex(0L);
        dao.updateScanIndex(itemData);
        ZabbixAlertScanIndex trendsData = new ZabbixAlertScanIndex();
        trendsData.setId(ZabbixAlertScanIndex.TRIGGER_ID);
        trendsData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        trendsData.setScanIndex(0L);
        dao.updateScanIndex(trendsData);
    }
}
