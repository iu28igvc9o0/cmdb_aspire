package com.aspire.mirror.zabbixintegrate.biz.item;

import com.aspire.mirror.zabbixintegrate.dao.ZabbixDao;
import com.aspire.mirror.zabbixintegrate.dao.ZabbixItemDao;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixAlertScanIndex;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixTrigger;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: ZabbixTriggerJob
 * @projectName zabbix-integrate
 * @description: TODO
 * @date 2019/6/2416:10
 */
@Slf4j
@Component
public class ZabbixTriggerJob {
    @Value("${local.alertIndex}")
    private String				alertIndex;
    @Value("${zabbix.trigger_config.topicName}")
    private String topicName;
    @Value("${zabbix.trigger_config.batchCount: 200}")
    private Integer batchCount;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ZabbixDao dao;
    @Autowired
    private ZabbixItemDao zabbixItemDao;

    @PostConstruct
    private void init () {
        ZabbixAlertScanIndex fetchScanIndex = dao.fetchScanIndex(ZabbixAlertScanIndex.TRIGGER_ID);
        if (fetchScanIndex == null) {
            ZabbixAlertScanIndex initModel = new ZabbixAlertScanIndex();
            initModel.setId(ZabbixAlertScanIndex.TRIGGER_ID);
            initModel.setScanIndex(0L);
            initModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            dao.insertScanIndex(initModel);
        }
    }

    @Scheduled(fixedDelayString = "${zabbix.trigger_config.fixedDelay}")
    public void run() {
        Long time1 = System.currentTimeMillis();
        ZabbixAlertScanIndex fetchScanIndex = dao.fetchScanIndex(ZabbixAlertScanIndex.TRIGGER_ID);
        Long maxScanIndex = fetchScanIndex.getScanIndex();
        Map<String, Object> params = new HashMap<>();
        params.put("startScanIndex", maxScanIndex);
        params.put("batchCount", batchCount);
        //查询zabbix item
        List<ZabbixTrigger> triggerList = zabbixItemDao.fetchTriggerList(params);
        if (CollectionUtils.isEmpty(triggerList)) {
            return;
        }
        for (ZabbixTrigger trigger:triggerList) {
            trigger.setPrefix(alertIndex);
            Long triggerId = Long.parseLong(trigger.getTriggerId());
            if (triggerId > maxScanIndex) {
                maxScanIndex = triggerId;
            }
        }
        kafkaTemplate.send(topicName, JSONArray.fromObject(triggerList).toString());
        fetchScanIndex.setScanIndex(maxScanIndex);
        fetchScanIndex.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        dao.updateScanIndex(fetchScanIndex);
        triggerList.clear();
        log.info("------------------producer trigger use : {} ms,size: {}", (System.currentTimeMillis()-time1), triggerList.size());
    }
}
