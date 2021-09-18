package com.aspire.mirror.zabbixintegrate.biz.item;

import com.aspire.mirror.zabbixintegrate.dao.ZabbixDao;
import com.aspire.mirror.zabbixintegrate.dao.ZabbixItemDao;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixAlertScanIndex;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixItem;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author baiwp
 * @title: ZabbixItemJob
 * @projectName zabbix-integrate
 * @description: TODO
 * @date 2019/6/2416:09
 */
@Slf4j
@Component
public class ZabbixItemJob {
    @Value("${local.alertIndex}")
    private String alertIndex;
    @Value("${zabbix.item_config.topicName}")
    private String topicName;
    @Value("${zabbix.item_config.batchCount: 200}")
    private Integer batchCount;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ZabbixDao dao;

    @Autowired
    private ZabbixItemDao zabbixItemDao;

    @PostConstruct
    private void init () {
        ZabbixAlertScanIndex fetchScanIndex = dao.fetchScanIndex(ZabbixAlertScanIndex.ITEM_ID);
        if (fetchScanIndex == null) {
            ZabbixAlertScanIndex initModel = new ZabbixAlertScanIndex();
            initModel.setId(ZabbixAlertScanIndex.ITEM_ID);
            initModel.setScanIndex(0L);
            initModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            dao.insertScanIndex(initModel);
        }
    }

    @Scheduled(fixedDelayString = "${zabbix.item_config.fixedDelay}")
    public void run() {
        Long time1 = System.currentTimeMillis();
        ZabbixAlertScanIndex fetchScanIndex = dao.fetchScanIndex(ZabbixAlertScanIndex.ITEM_ID);
        Long maxScanIndex = fetchScanIndex.getScanIndex();
        Map<String, Object> params = new HashMap<>();
        params.put("startScanIndex", maxScanIndex);
        params.put("batchCount", batchCount);
        //查询zabbix item
        List<ZabbixItem> itemList = zabbixItemDao.fetchItemList(params);
        if (CollectionUtils.isEmpty(itemList)) {
            return;
        }
        for (ZabbixItem item:itemList) {
            item.setPrefix(alertIndex);
            Long itemId = Long.parseLong(item.getItemId());
            if (itemId > maxScanIndex) {
                maxScanIndex = itemId;
            }
        }

        kafkaTemplate.send(topicName, JSONArray.fromObject(itemList).toString());
        fetchScanIndex.setScanIndex(maxScanIndex);
        fetchScanIndex.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        dao.updateScanIndex(fetchScanIndex);
        itemList.clear();
        log.info("------------------producer item use : {} ms,size: {}", (System.currentTimeMillis()-time1), itemList.size());
    }
}
