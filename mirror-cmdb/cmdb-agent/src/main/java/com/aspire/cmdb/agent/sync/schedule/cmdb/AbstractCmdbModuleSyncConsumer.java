package com.aspire.cmdb.agent.sync.schedule.cmdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;

import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncFieldMapping;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.sync.payload.CmdbSyncModuleCondition;
import com.aspire.ums.cmdb.sync.payload.CmdbSyncType;
import com.aspire.ums.cmdb.sync.service.CmdbSyncReceiveMessageLogService;
import com.aspire.ums.cmdb.sync.service.ICmdbSyncFieldMappingService;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.google.common.collect.Maps;

/**
 * 网管同步cmdb资产数据模型配置处理抽象类.
 *
 * @author jiangxuwen
 * @date 2020/6/12 15:06
 */
public abstract class AbstractCmdbModuleSyncConsumer {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    private static final boolean IS_DELETE_PHYSICAL = true;

    private ICmdbInstanceService cmdbInstanceService;

    private ICmdbSyncFieldMappingService cmdbSyncFieldMappingService;

    private CmdbSyncReceiveMessageLogService receiveMessageLogService;

    private ConfigDictService configDictService;

    public ConfigDictService getConfigDictService() {
        if (configDictService == null) {
            configDictService = SpringUtils.getBean(ConfigDictService.class);
        }
        return configDictService;
    }

    public ICmdbInstanceService getCmdbInstanceService() {
        if (cmdbInstanceService == null) {
            cmdbInstanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
        return cmdbInstanceService;
    }

    public ICmdbSyncFieldMappingService getCmdbSyncFieldMappingService() {
        if (cmdbSyncFieldMappingService == null) {
            cmdbSyncFieldMappingService = SpringUtils.getBean(ICmdbSyncFieldMappingService.class);
        }
        return cmdbSyncFieldMappingService;
    }

    public CmdbSyncReceiveMessageLogService getReceiveMessageLogService() {
        if (receiveMessageLogService == null) {
            receiveMessageLogService = SpringUtils.getBean(CmdbSyncReceiveMessageLogService.class);
        }
        return receiveMessageLogService;
    }

    protected void onMessage(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        if (CollectionUtils.isEmpty(records)) {
            log.info("message is empty!!!");
            return;
        }
        CmdbSyncType syncType = getSyncType();
        log.info(">>>>>>>>>> [{}]收到消息，准备处理 >>>>>>", syncType.getLabel());
        log.info("[{}] 收到消息的数量:{}", syncType.getLabel(), records.size());
        try {
            for (ConsumerRecord<?, ?> record : records) {
                Optional<?> kafkaMessage = Optional.ofNullable(record.value());
                if (kafkaMessage.isPresent()) {
                    Object message = record.value();
                    String topic = record.topic();
                    Object key = record.key();
                    long offset = record.offset();
                    int partition = record.partition();
                    log.info("[{}] 参数：topic:[{}],key:[{}],offset:[{}],partition:[{}]", syncType.getLabel(), topic, key, offset,
                            partition);
                    log.info("[{}] 消息内容为:{}", syncType.getLabel(), message);
                    handleMsg(message.toString());
                }
            }
            log.info("<<<<<<<< [{}] 消息处理完成 <<<<<<<<<<<<<", syncType.getLabel());
        } catch (Exception e) {
            log.error("[{}]处理失败.", syncType.getLabel(), e);
        } finally {
            ack.acknowledge();
        }
    }

    private void handleMsg(String value) {
        CmdbSyncType syncType = getSyncType();
        try {
            CmdbSyncSendMessageLog messageLog = JsonMapper.getInstanceWithSnakeCase().readValue(value,
                    CmdbSyncSendMessageLog.class);
            getReceiveMessageLogService().saveReceiveLog(messageLog);
            String objectName = messageLog.getObjectName();
            String eventType = messageLog.getEventType();
            CmdbOptType optType = CmdbOptType.fromValue(eventType);
            log.info("[{}]开始入库,操作类型:[{}],操作表[{}]", syncType.getLabel(), optType.getLabel(), objectName);
            saveInstanceRecord(messageLog);
            log.info("[{}]入库[{}]成功!", syncType.getLabel(), objectName);
        } catch (Exception e) {
            log.error("[{}]入库失败!", syncType.getLabel(), e);
        }
    }

    private void saveInstanceRecord(CmdbSyncSendMessageLog messageLog) throws Exception {
        CmdbSyncType syncType = getSyncType();
        String msgBody = messageLog.getMsgBody();
        Map<String, Object> map = new JsonMapper().readValue(msgBody, HashMap.class);
        String eventType = messageLog.getEventType();
        CmdbOptType optType = CmdbOptType.fromValue(eventType);
        Map<String, Object> instanceMap = convert(map);
        convertDictLabel2Id(instanceMap, "host_backup", "hostBackup");

        if (CmdbOptType.OPT_ADD == optType || CmdbOptType.OPT_MODIFY == optType) {
            Map<String, Object> instanceDetailMap = getCmdbInstanceService()
                    .getInstanceDetail(instanceMap.get("module_id").toString(), instanceMap.get("instance_id").toString());
            if (MapUtils.isNotEmpty(instanceDetailMap) && instanceDetailMap.get("id") != null) {
                getCmdbInstanceService().updateInstance(instanceMap);
            } else {
                getCmdbInstanceService().addInstanceNoApprove("系统", instanceMap, syncType.getLabel() + "新增");
            }
        } /*
           * else if (CmdbOptType.OPT_MODIFY == optType) { getCmdbInstanceService().updateInstance(instanceMap); }
           */
        else if (CmdbOptType.OPT_DEL == optType) {
            // TODO:是否删除关联的数据？？？
            List<Map<String, Object>> instanceList = new ArrayList<>();
            instanceList.add(instanceMap);
            if (IS_DELETE_PHYSICAL) {
                getCmdbInstanceService().deleteInstancePysicalNoApprove("系统", instanceList, syncType.getLabel() + "删除");
            } else {
                getCmdbInstanceService().deleteInstanceNoApprove("系统", instanceList, syncType.getLabel() + "删除");
            }
        }
    }

    private void convertDictLabel2Id(Map<String, Object> map, String key, String dictType) {
        if (map.containsKey(key) && map.get(key) != null) {
            String keyValue = map.get(key).toString();
            if (StringUtils.isNotBlank(keyValue)) {
                String id = getConfigDictService().getIdByNoteAndCol(keyValue, dictType);
                if (StringUtils.isNotBlank(id)) {
                    map.put(key, id);
                }
            }
        }
    }

    private Map<String, Object> convert(Map<String, Object> map) {
        CmdbSyncType syncType = getSyncType();
        CmdbSyncModuleCondition condition = buildFieldCondition();
        Map<String, Object> instanceMap = Maps.newLinkedHashMap();
        String key = condition.getModuleKey();
        if (CmdbSyncType.CMDB_DEVICE == syncType) {
            key = map.get("device_class_name") + "-" + map.get("device_type_name");
        }
        // 将设备分类和设备类型作为key，moduleId作为value.映射
        CmdbSyncFieldMapping query = new CmdbSyncFieldMapping();
        query.setMappingType(KafkaConfigConstant.MAPPING_MODULE);
        query.setMappingKey(key);
        CmdbSyncFieldMapping entity = getCmdbSyncFieldMappingService().getOne(query);
        if (entity == null || StringUtils.isBlank(entity.getMappingValue())) {
            throw new IllegalArgumentException("请配置[" + key + "]网管同步到cmdb的模型!!!");
        }
        instanceMap.put("module_id", entity.getMappingValue());
        instanceMap.put("instance_id", map.get("id"));
        if (CmdbSyncType.CMDB_DEVICE_MAINTENCE == syncType) {
            instanceMap.put("instance_id", map.get("device_id"));
        }
        // 网管同步过来的不能 再同步到网管cmdb
        instanceMap.put("noSyncFlag", true);
        // instanceMap.put("department1", "中移互联网公司");
        if (CmdbSyncType.FINANCE_CMDB_BUSINESS == syncType) {
            instanceMap.put("business_type", "业务系统");
        }
        query.setMappingType(condition.getModuleFieldType());
        query.setMappingKey(condition.getModuleFiledKey());
        List<CmdbSyncFieldMapping> list = getCmdbSyncFieldMappingService().list(query);
        instanceMap.putAll(map);
        list.forEach(e -> {
            if (map.containsKey(e.getSrcField())) {
                instanceMap.put(e.getDestField(), map.get(e.getSrcField()));
            }
        });
        return instanceMap;
    }

    protected abstract CmdbSyncModuleCondition buildFieldCondition();

    protected abstract CmdbSyncType getSyncType();
}
