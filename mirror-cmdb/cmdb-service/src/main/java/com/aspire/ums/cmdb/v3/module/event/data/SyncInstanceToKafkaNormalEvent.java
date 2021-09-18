package com.aspire.ums.cmdb.v3.module.event.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.config.KafkaProducerFactory;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
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
public class SyncInstanceToKafkaNormalEvent extends AbstractModuleEvent{

    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbConfigService configService;
    @Override
    public void initSpringBeans() {
        if (this.instanceService == null) {
            this.instanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
        if (this.moduleService == null) {
            this.moduleService = SpringUtils.getBean(ModuleService.class);
        }
        if (this.configService == null) {
            this.configService = SpringUtils.getBean(ICmdbConfigService.class);
        }
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        String changeType = handleData.get("changeType").toString();
        Map instance = JSONObject.parseObject(JSON.toJSONString(handleData.get("instanceDetail")), Map.class);
        Map<String, Object> resultData = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
//        Map<String, Object> instance = new HashMap<>();
        if (!EventConst.EVENT_TYPE_DATA_DELETE.equals(changeType)) {
            instance = instanceService.getInstanceDetail(moduleId, instanceId);
        } else {
            // 删除
            instance.put("id", instanceId);
        }
        if (instance == null) {
            log.error("实例id：{}不存在", instanceId);
            return null;
        }
        Map<String, Map<String, String>> columnsMap = moduleService.getModuleColumns(moduleId);
        instance.put("columns", columnsMap);
        instance.put("changeType", ChangeTypeEnum.getCode(changeType));
        data.add(instance);
        //不同类型
        Map<String, Object> typeMap = new HashMap<>();
        String typeKey = String.format(EventConst.SUYAN_TYPE_KEY_PREFIX, instance.get("module_id"));
        CmdbConfig config = configService.getConfigByCode(typeKey);
        typeMap.put(config.getConfigValue(), data);
        resultData.put("data", typeMap);
        // 不同设备类型放到不同key下
        // 构建kafka
        String topic = handleData.get("topic").toString();
        Integer partition = handleData.containsKey("partition") ? (Integer) handleData.get("partition") : null;
        String kafkaAddress = handleData.get("kafka_address").toString();

        String kafkaKey = String.format(EventConst.KAFKA_KEY_FORMAT, kafkaAddress, topic, partition);
        if (!EventConst.KAFAK_MAP.containsKey(kafkaKey)) {
            // 新建生产者
            KafkaProducerFactory pf = new KafkaProducerFactory(kafkaAddress, topic, partition);
            pf.createProducer();
            EventConst.KAFAK_MAP.put(kafkaKey, pf);
        }
        KafkaProducerFactory producerFactory = EventConst.KAFAK_MAP.get(kafkaKey);
        // 发送数据
        Producer producer = producerFactory.getProducer();
        producerFactory.sendMessage(producer, topic , partition,null, JSON.toJSONString(resultData));
//        producer.close();
        return null;
    }
}
