package com.aspire.ums.cmdb.sync.service.producer;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.config.SyncOsaConfig;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncFieldMapping;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.sync.service.CmdbSyncSendMessageLogService;
import com.aspire.ums.cmdb.sync.service.ICmdbSyncFieldMappingService;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Data;

/**
 * 同步模型数据到网管
 *
 * @author jiangxuwen
 * @date 2020/9/15 11:02
 */
@Service("cmdbModuleProducerServiceImpl")
public class CmdbModuleProducerServiceImpl {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ICmdbInstanceService instanceService;

    @Autowired
    private ICmdbSyncFieldMappingService cmdbSyncFieldMappingService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private KafkaProducerServer kafkaProducerServer;

    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private CmdbSyncSendMessageLogService cmdbSyncSendMessageLogService;

    @Autowired
    private SyncOsaConfig syncOsaConfig;

    private ThreadPoolTaskExecutor getTaskExecutor() {
        if (this.taskExecutor == null) {
            this.taskExecutor = SpringUtils.getBean("taskExecutor", ThreadPoolTaskExecutor.class);
        }
        return this.taskExecutor;
    }

    /**
     * 保存发送的消息日志.
     *
     * @param eventType
     *            操作类型
     * @param topic
     *            kafka Topic
     * @param objectId
     *            操作表id
     * @param partitionId
     *            kafka分区id
     * @param objectName
     *            操作的表名
     * @return
     */
    private CmdbSyncSendMessageLog saveLog(CmdbOptType eventType, String topic, String moduleId, String objectId,
            String partitionId, String objectName) {
        CmdbSyncSendMessageLog messageLog = new CmdbSyncSendMessageLog();
        messageLog.setEventType(eventType.getValue());
        messageLog.setModuleId(moduleId);
        messageLog.setInstanceId("new_cmdb");
        messageLog.setMsgId(UUIDUtil.getUUID());
        messageLog.setTopic(topic);
        messageLog.setMsgStatus("0");
        messageLog.setObjectId(objectId);
        // if (!"delete".equals(eventType)) {
        // Map<String, Object> resultMap = sqlSessionTemplate.selectOne(
        // "com.aspire.birp.modules.osa.dao.config.dict.getDictConfig", objectId);
        // messageLog.setData(resultMap);
        // messageLog.setMsgBody(JSON.toJSONString(resultMap));
        // }
        Object data = null;
        if (CmdbOptType.OPT_ADD == eventType || CmdbOptType.OPT_MODIFY == eventType) {
            data = getObjectData(moduleId, objectId);
        } else {
            Map<String, String> map = Maps.newHashMap();
            map.put("id", objectId);
            data = map;
        }
        if (data == null) {
            logger.error("同步的data为空!!!!");
            throw new IllegalArgumentException("同步数据不能为空!!!");
        }
        messageLog.setData(data);
        // logger.info("data==={}", data);
        messageLog.setMsgBody(JsonMapper.getInstance().toJson(data));
        messageLog.setObjectName(objectName);
        messageLog.setObjectVersion(1);
        messageLog.setOperator("system");
        messageLog.setOptTime(new Date());
        messageLog.setOptDesc(eventType + "了" + objectName + "表的值");
        messageLog.setObjectKey(partitionId);
        cmdbSyncSendMessageLogService.add(messageLog);
        return messageLog;
    }

    /**
     * 发送kafka消息.
     *
     * @param eventType
     *            操作类型
     * @param objectId
     *            操作表id
     * @return
     */
    public void saveEventLogAndSendMsg(CmdbOptType eventType, String moduleId, String objectId) {
        boolean syncOsaFlag = syncOsaConfig.isSyncOsaFlag();
        // TODO:从网管模型同步数据表里面获取模型后，同步，其他模型 不同步。如业务系统模型 IP模型等不需要同步
        if (syncOsaFlag) {
            getTaskExecutor().execute(() -> sendMsg(eventType, moduleId, objectId));
        }
    }

    private void sendMsg(CmdbOptType eventType, String moduleId, String objectId) {
        boolean syncFlag = true;
        logger.info(">>>>>>> 进入发送同步消息到kafka逻辑 >>>>>>>>>>");
        if (StringUtils.isNotBlank(moduleId)) {
            List<CmdbSyncFieldMapping> moduleList = getSyncOsaModuleList();
            List<String> moduleIdList = moduleList.stream().map(e -> e.getMappingValue()).collect(Collectors.toList());
            if (moduleIdList.contains(moduleId)) {
                syncFlag = true;
            } else {
                syncFlag = false;
                logger.info("未找到该模型ID:[" + moduleId + "]对应的同步配置数据,请在[cmdb_sync_field_mapping]表内配置!!!");
            }
        }
        if (syncFlag) {
            // getTaskExecutor().execute(() -> {
            CmdbModuleField field = getFieldMapping(moduleId);
            // 主机资源要同步硬件维保到网管
            if (field.isDeviceAssetFlag()) {
                CmdbSyncSendMessageLog messageLog = saveLog(eventType, KafkaConfigConstant.TOPIC_CMDB_DEVICE_ASSET, moduleId,
                        objectId, objectId, KafkaConfigConstant.TABLE_CMDB_DEVICE_ASSET);
                kafkaProducerServer.sendMsgForTemplate(messageLog);

                // CmdbSyncSendMessageLog messageLog2 = saveLog(eventType, KafkaConfigConstant.TOPIC_CMDB_DEVICE_ASSET, moduleId,
                // objectId, objectId, KafkaConfigConstant.TABLE_CMDB_DEVICE_ASSET_MAINTENCE);
                // kafkaProducerServer.sendMsgForTemplate(messageLog2);
            } else {
                CmdbSyncSendMessageLog log = populate(field);
                CmdbSyncSendMessageLog messageLog = saveLog(eventType, log.getTopic(), moduleId, objectId, objectId,
                        log.getObjectName());
                kafkaProducerServer.sendMsgForTemplate(messageLog);
            }

            // });
        }
    }

    private CmdbSyncSendMessageLog populate(CmdbModuleField field) {
        CmdbSyncSendMessageLog log = new CmdbSyncSendMessageLog();
        if (CollectionUtils.isEmpty(field.getFieldMappingList())) {
            throw new RuntimeException("fieldMapping is empty!");
        }
        CmdbSyncFieldMapping fieldMapping = field.getFieldMappingList().get(0);
        log.setTopic(KafkaConfigConstant.TOPIC_CMDB_DEVICE_ASSET);
        log.setObjectName(fieldMapping.getMappingType());
        return log;
    }

    private List<CmdbSyncFieldMapping> getSyncOsaModuleList() {
        CmdbSyncFieldMapping query = new CmdbSyncFieldMapping();
        query.setMappingType(KafkaConfigConstant.MAPPING_MODULE);
        query.setSyncOsaFlag("1");
        return cmdbSyncFieldMappingService.list();
    }

    private void convert(Map<String, Object> instanceMap, String moduleId) {
        CmdbModuleField fieldMapping = getFieldMapping(moduleId);

        Map<String, Object> keyValuePairs = Maps.newHashMap();
        fieldMapping.getFieldMappingList().forEach(e -> {
            String dtoField = e.getSrcField();
            String mapField = e.getDestField();
            if (instanceMap.containsKey(mapField)) {
                // 字段名 下划线转为驼峰风格
                // String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, dtoField);
                String fieldName = dtoField;
                // Field field = ReflectionUtils.findField(dto.getClass(), fieldName);
                Object value = instanceMap.get(mapField);
                keyValuePairs.put(fieldName, value);
            }
        });
        instanceMap.putAll(keyValuePairs);
    }

    private CmdbModuleField getFieldMapping(String moduleId) {

        String configModuleId = moduleId;
        boolean isDeviceAsset = false;
        Module module = moduleService.getModuleDetail(moduleId);
        List<Module> refModules = module.getRefModules();
        if (CollectionUtils.isNotEmpty(refModules)) {
            for (int i = 0; i < refModules.size(); i++) {
                Module ref = refModules.get(i);
                CmdbV3ModuleCatalog catalog = ref.getModuleCatalog();
                String catalogCode = catalog.getCatalogCode();
                if ("cmdb_instance".equals(catalogCode)) {
                    configModuleId = ref.getId();
                    isDeviceAsset = true;
                    break;
                }
            }
        }
        // mapping_value对应moduleId,资产模型对应主机资源的模型ID.
        CmdbSyncFieldMapping query = new CmdbSyncFieldMapping();
        query.setMappingKey(KafkaConfigConstant.MAPPING_KEY_FIELD);
        query.setMappingValue(configModuleId);
        List<CmdbSyncFieldMapping> fieldMappingList = cmdbSyncFieldMappingService.list(query);
        CmdbModuleField field = new CmdbModuleField();
        field.setDeviceAssetFlag(isDeviceAsset);
        field.setFieldMappingList(fieldMappingList);
        return field;
    }

    private Map<String, Object> getObjectData(String moduleId, String objectId) {
        Map<String, Object> dataMap = instanceService.getInstanceDetail(moduleId, objectId);
        convert(dataMap, moduleId);
        return dataMap;
    }

    @Data
    private static class CmdbModuleField {

        private boolean deviceAssetFlag;

        private List<CmdbSyncFieldMapping> fieldMappingList = Lists.newArrayList();
    }
}
