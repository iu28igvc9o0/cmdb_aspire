package com.aspire.ums.cmdb.v3.module.event.data;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.config.KafkaProducerFactory;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceIpManagerService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import com.aspire.ums.cmdb.v3.module.event.EventConst;
import com.aspire.ums.cmdb.v3.module.event.enums.ChangeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SyncInstanceToKafkaNormalEvent
 * Author:   hangfang
 * Date:     2020/9/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class SyncInstanceToKafkaAlertEvent extends AbstractModuleEvent{

    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ICmdbInstanceIpManagerService ipManagerService;
    @Autowired
    private ModuleService moduleService;

    @Override
    public void initSpringBeans() {
        if (this.instanceService == null) {
            this.instanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
        if (this.ipManagerService == null) {
            this.ipManagerService = SpringUtils.getBean(ICmdbInstanceIpManagerService.class);
        }
        if (this.moduleService == null) {
            this.moduleService = SpringUtils.getBean(ModuleService.class);
        }
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        String changeType = handleData.get("changeType").toString();
        Map<String, Object> resultData = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> instance = new HashMap<>();
        if (!EventConst.EVENT_TYPE_DATA_DELETE.equals(changeType)) {
            instance = instanceService.getInstanceDetail(moduleId, instanceId);
        } else {
            instance.put("id", instanceId);
        }
        instance.put("changeType", ChangeTypeEnum.getCode(changeType));
        List<Map<String, Object>> ipManagerList = ipManagerService.getAllIpManagerList(instanceId);
        for (Map<String, Object> ipManager : ipManagerList) {
            if (StringUtils.isNotEmpty(ipManager.get("ip"))) {
                Map<String, Object> alertInstance = new HashMap<>();
                alertInstance.putAll(instance);
                alertInstance.put("ip", ipManager.get("ip").toString());
                data.add(alertInstance);
            }
        }
        if (!EventConst.EVENT_TYPE_DATA_DELETE.equals(changeType) && data.size() == 0) {
            data.add(instance);
        }
        String topic = handleData.get("topic").toString();
        Integer partition = handleData.containsKey("partition") ? (Integer) handleData.get("partition") : null;
        String kafkaAddress = handleData.get("kafka_address").toString();
        resultData.put("columns", moduleService.getModuleColumns(moduleId));
        resultData.put("data", data);
        String kafkaKey = String.format(EventConst.KAFKA_KEY_FORMAT, kafkaAddress, topic, partition);
        if (!EventConst.KAFAK_MAP.containsKey(kafkaKey)) {
            // 新建生产者
            KafkaProducerFactory pf = new KafkaProducerFactory(kafkaAddress, topic, partition);
            pf.createProducer();
            EventConst.KAFAK_MAP.put(kafkaKey, pf);
        }
        KafkaProducerFactory producerFactory = EventConst.KAFAK_MAP.get(kafkaKey);
        Producer producer = producerFactory.getProducer();
        // 发送数据
        producerFactory.sendMessage(producer, topic , partition,null, JSON.toJSONString(resultData));
        producer.close();
        return null;
    }


//    public static void main(String[] args) {
//        CmdbModuleKafkaEvent event = new CmdbModuleKafkaEvent("1", "1", "testwww",  "cmdb_sync", "10.12.70.40:9092", 1);
//        CmdbModuleKafkaEvent event1 = new CmdbModuleKafkaEvent("1", "1", "testwww",  "cmdb_change_sync", "10.12.70.40:9092", 1);
//        CmdbModuleKafkaEvent event2 = new CmdbModuleKafkaEvent("1", "1", "testwww", "cmdb_change_sync", "10.1.203.99:9092", 0);
//        Map<String, Object> handelData = new HashMap<>();
//        handelData.put("kafkaEvent", event);
//        handelData.put("instanceData", event);
//        Map<String, Object> handelData1 = new HashMap<>();
//        handelData1.put("kafkaEvent", event1);
//        handelData1.put("instanceData", event);
//        Map<String, Object> handelData2 = new HashMap<>();
//        handelData2.put("kafkaEvent", event2);
//        handelData2.put("instanceData", event);
//        SyncInstanceToKafkaAlertEvent.event("1", null, handelData);
//        SyncInstanceToKafkaAlertEvent.event("1", null, handelData1);
//        SyncInstanceToKafkaAlertEvent.event("1", null, handelData2);
//
//    }


}
