package com.aspire.mirror.zabbixintegrate.biz.item;

import com.aspire.mirror.zabbixintegrate.dao.ZabbixDao;
import com.aspire.mirror.zabbixintegrate.dao.ZabbixItemDao;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixAlertScanIndex;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixTrends;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixTrigger;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: ZabbixTrendsJob
 * @projectName zabbix-integrate
 * @description: TODO
 * @date 2019/6/2416:10
 */
@Slf4j
//@Component
public class ZabbixTrendsJob {
    @Value("${local.alertIndex}")
    private String				alertIndex;
    @Value("${zabbix.trends_config.topicName}")
    private String topicName;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ZabbixDao dao;
    @Autowired
    private ZabbixItemDao zabbixItemDao;

//    @PostConstruct
    private void init () {
        ZabbixAlertScanIndex fetchScanIndex = dao.fetchScanIndex(ZabbixAlertScanIndex.TRENDS_ID);
        if (fetchScanIndex == null) {
            ZabbixAlertScanIndex initModel = new ZabbixAlertScanIndex();
            initModel.setId(ZabbixAlertScanIndex.TRENDS_ID);
            initModel.setScanIndex(0L);
            initModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            dao.insertScanIndex(initModel);
        }
    }

//    @Scheduled(fixedDelayString = "${zabbix.trends_config.fixedDelay}")
    public void run() {
        Long time1 = System.currentTimeMillis();
        ZabbixAlertScanIndex fetchScanIndex = dao.fetchScanIndex(ZabbixAlertScanIndex.TRENDS_ID);
        Long maxScanIndex = fetchScanIndex.getScanIndex();
        Map<String, Object> params = new HashMap<>();
        params.put("startScanIndex", maxScanIndex);
        Long clock = zabbixItemDao.fetchMinClock(params);
        //如果没有可处理的数据，则返回
        if (clock == null || clock == 0L || clock.longValue() == maxScanIndex.longValue()) {
            return;
        }
        params.put("startScanIndex", clock);
        //查询zabbix item
        List<ZabbixTrends> trendsList = zabbixItemDao.fetchTrendList(params);
        if (CollectionUtils.isEmpty(trendsList)) {
            return;
        }
        List<ZabbixTrends> trendsList1 = new ArrayList<>();
        for (int i = 0; i < trendsList.size(); i++) {
            ZabbixTrends trends = trendsList.get(i);
            trends.setPrefix(alertIndex);
            trendsList1.add(trends);
            //批量发送kafka，每200条发送一次
            if ((i+1)%200 == 0) {
                kafkaTemplate.send(topicName, JSONArray.fromObject(trendsList1).toString());
                trendsList1.clear();
            }
        }
        if (trendsList1.size() > 0) {
            ListenableFuture f = kafkaTemplate.send(topicName, JSONArray.fromObject(trendsList1).toString());
        }
        fetchScanIndex.setScanIndex(clock);
        fetchScanIndex.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        dao.updateScanIndex(fetchScanIndex);
        log.info("------------------producer trends use : {} ms,size: {}", (System.currentTimeMillis()-time1), trendsList.size());
    }
}
