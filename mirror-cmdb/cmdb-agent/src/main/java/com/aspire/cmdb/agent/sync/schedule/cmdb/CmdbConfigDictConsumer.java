package com.aspire.cmdb.agent.sync.schedule.cmdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncFieldMapping;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.sync.payload.CmdbSyncModuleCondition;
import com.aspire.ums.cmdb.sync.payload.CmdbSyncType;
import com.aspire.ums.cmdb.sync.payload.ConfigDictDTO;
import com.aspire.ums.cmdb.sync.service.CmdbSyncReceiveMessageLogService;
import com.aspire.ums.cmdb.sync.service.ICmdbSyncFieldMappingService;
import com.aspire.ums.cmdb.systemManager.service.OrgManagerService;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.v2.idc.entity.CmdbIdcManager;
import com.aspire.ums.cmdb.v2.idc.service.ICmdbIdcManagerService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * 字典表同步消费端.
 *
 * @author jiangxuwen
 * @date 2020/5/18 18:50
 */
@Data
@Component
@Slf4j
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class CmdbConfigDictConsumer {

    @Autowired
    private ConfigDictService configDictService;

    @Autowired
    private OrgManagerService orgManagerService;

    @Autowired
    private ICmdbIdcManagerService cmdbIdcManagerService;

    @Autowired
    private ICmdbSyncFieldMappingService cmdbSyncFieldMappingService;

    @Autowired
    private CmdbSyncReceiveMessageLogService receiveMessageLogService;

    @Autowired
    private ICmdbInstanceService cmdbInstanceService;

    @Autowired
    private ModuleService moduleService;

    private static final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    static {
        mapperFactory.classMap(ConfigDictDTO.class, CmdbIdcManager.class).field("id", "id").field("value", "idcCode")
                .field("label", "idcName").field("deleteFlag", "isDelete").byDefault().register();
    }

    @KafkaListener(id = "config-dict-sync-kafka-0",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_config_dict:osa_config_dict}", partitions = { "0" }) })
    public void listenPartition0(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("config-dict-sync-kafka-0 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("config-dict-sync-kafka-0 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "config-dict-sync-kafka-1",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_config_dict:osa_config_dict}", partitions = { "1" }) })
    public void listenPartition1(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("config-dict-sync-kafka-1 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("config-dict-sync-kafka-1 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "config-dict-sync-kafka-2",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_config_dict:osa_config_dict}", partitions = { "2" }) })
    public void listenPartition2(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("config-dict-sync-kafka-2 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("config-dict-sync-kafka-2 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "config-dict-sync-kafka-3",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_config_dict:osa_config_dict}", partitions = { "3" }) })
    public void listenPartition3(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("config-dict-sync-kafka-3 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("config-dict-sync-kafka-3 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "config-dict-sync-kafka-4",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_config_dict:osa_config_dict}", partitions = { "4" }) })
    public void listenPartition4(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("config-dict-sync-kafka-4 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("config-dict-sync-kafka-4 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "config-dict-sync-kafka-5",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_config_dict:osa_config_dict}", partitions = { "5" }) })
    public void listenPartition5(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("config-dict-sync-kafka-5 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("config-dict-sync-kafka-5 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "config-dict-sync-kafka-6",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_ConfigDict}",
                    partitions = { "6" }) })
    public void listenPartition6(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("config-dict-sync-kafka-6 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("config-dict-sync-kafka-6 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "config-dict-sync-kafka-7",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_config_dict:osa_config_dict}", partitions = { "7" }) })
    public void listenPartition7(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("config-dict-sync-kafka-7 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("config-dict-sync-kafka-7 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "config-dict-sync-kafka-8",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_config_dict:osa_config_dict}", partitions = { "8" }) })
    public void listenPartition8(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("config-dict-sync-kafka-8 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("config-dict-sync-kafka-8 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "config-dict-sync-kafka-9",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_config_dict:osa_config_dict}", partitions = { "9" }) })
    public void listenPartition9(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("config-dict-sync-kafka-9 Listener, Thread ID:{}", Thread.currentThread().getId());
        try {
            onMessage(records);
        } catch (Exception e) {
            log.error("config-dict-sync-kafka-9 process message error.", e);
        } finally {
            ack.acknowledge();
        }
    }

    private void onMessage(List<ConsumerRecord<?, ?>> records) {
        if (CollectionUtils.isEmpty(records)) {
            log.info("[cmdb字典表同步] message is empty!!!");
            return;
        }
        log.info(">>>>>>>>>> [cmdb字典表同步]收到消息，准备处理 >>>>>>");
        log.info("[cmdb字典表同步]收到消息的数量:{}", records.size());
        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();
                Object key = record.key();
                long offset = record.offset();
                int partition = record.partition();
                log.info("[cmdb字典表同步] 参数：topic:[{}],key:[{}],offset:[{}],partition:[{}]", topic, key, offset, partition);
                log.info("[cmdb字典表同步] 消息内容为:{}", message);
                handleMsg(message.toString());
            }
            log.info("<<<<<<<< [cmdb字典表同步] 消息处理完成 <<<<<<<<<<<<<");
        }
    }

    private void handleMsg(String value) {
        try {
            CmdbSyncSendMessageLog messageLog = JsonMapper.getInstance().readValue(value, CmdbSyncSendMessageLog.class);
            receiveMessageLogService.saveReceiveLog(messageLog);
            String objectName = messageLog.getObjectName();
            String eventType = messageLog.getEventType();
            CmdbOptType optType = CmdbOptType.fromValue(eventType);
            log.info("[cmdb字典表同步]开始入库,操作类型:[{}],操作表[{}]", optType.getLabel(), objectName);
            if (KafkaConfigConstant.TABLE_CONFIG_DICT.equals(objectName)) {
                saveDict(messageLog);
            } else if (KafkaConfigConstant.TABLE_SYS_ORG.equals(objectName)) {
                saveSysOrg(messageLog);
            }
            // TODO:资源池字典表 idcType同步到表cmdb_idc_manage
            log.info("[cmdb字典表同步] 入库[{}]成功!", objectName);
        } catch (Exception e) {
            log.error("[cmdb字典表同步]入库失败!", e);
        }
    }

    private void saveDict(CmdbSyncSendMessageLog messageLog) throws Exception {
        String msgBody = messageLog.getMsgBody();
        ConfigDictDTO configDictDTO = JsonMapper.getInstanceWithSnakeCase().readValue(msgBody, ConfigDictDTO.class);
        String eventType = messageLog.getEventType();
        CmdbOptType optType = CmdbOptType.fromValue(eventType);

        // 同步到字典表，同时同步到资源池的表
        // TODO:资源池不做双向同步
        if ("idcType".equals(configDictDTO.getType())) {
            CmdbIdcManager idcManager = convertIdcManager(configDictDTO);
            if ("1".equals(configDictDTO.getDeleteFlag())) {
                idcManager.setIsDelete("1");
            } else {
                idcManager.setIsDelete("0");
            }
            if (CmdbOptType.OPT_ADD == optType) {
                cmdbIdcManagerService.insert(idcManager);
            } else if (CmdbOptType.OPT_MODIFY == optType) {
                cmdbIdcManagerService.update(idcManager);
            } else if (CmdbOptType.OPT_DEL == optType) {
                cmdbIdcManagerService.delete(idcManager);
            }
        }
        // 网管的idcType对应新cmdb的idcType2
        String colName = configDictDTO.getType();
        if ("idcType".equals(colName)) {
            colName = "idcType2";
        }
        if ("deviceClass".equals(colName)) {
            colName = "device_class";
        }
        if ("deviceType".equals(colName)) {
            colName = "device_type";
        }
        if ("deviceModel".equals(colName)) {
            colName = "device_model";
        }
        configDictDTO.setType(colName);

        if (CmdbOptType.OPT_ADD == optType) {
            save2Module(configDictDTO, CmdbSyncType.SYS_DICT, optType);
        } else if (CmdbOptType.OPT_MODIFY == optType) {
            save2Module(configDictDTO, CmdbSyncType.SYS_DICT, optType);
        } else if (CmdbOptType.OPT_DEL == optType) {
            CmdbIdcManager idcManager = new CmdbIdcManager();
            idcManager.setId(configDictDTO.getId());
            cmdbIdcManagerService.delete(idcManager);
            save2Module(configDictDTO, CmdbSyncType.SYS_DICT, optType);
        }
    }

    private CmdbIdcManager convertIdcManager(ConfigDictDTO dto) {
        BoundMapperFacade<ConfigDictDTO, CmdbIdcManager> mapper = mapperFactory.getMapperFacade(ConfigDictDTO.class,
                CmdbIdcManager.class);
        return mapper.map(dto);
    }

    private void saveSysOrg(CmdbSyncSendMessageLog messageLog) throws Exception {
        String msgBody = messageLog.getMsgBody();
        ConfigDictDTO configDictDTO = JsonMapper.getInstanceWithSnakeCase().readValue(msgBody, ConfigDictDTO.class);
        String eventType = messageLog.getEventType();
        CmdbOptType optType = CmdbOptType.fromValue(eventType);
        save2Module(configDictDTO, CmdbSyncType.SYS_ORG, optType);
    }

    private void save2Module(ConfigDictDTO dict, CmdbSyncType syncType, CmdbOptType optType) {
        Map<String, Object> instanceData = convert(dict, syncType);

        // TODO: 更新业务线，需要更新cmdb_business_system和cmdb_instance等关联业务线的数据
        Preconditions.checkNotNull(instanceData.get("module_id"), "module_id cannot be null");
        String instanceId = dict.getId();
        log.debug("instanceId=={}", instanceId);
        if (CmdbOptType.OPT_ADD == optType || CmdbOptType.OPT_MODIFY == optType) {
            Map<String, Object> instanceDetailMap = getCmdbInstanceService()
                    .getInstanceDetail(instanceData.get("module_id").toString(), instanceData.get("instance_id").toString());
            if (MapUtils.isNotEmpty(instanceDetailMap) && instanceDetailMap.get("id") != null) {
                getCmdbInstanceService().updateInstance(instanceData);
            } else {
                getCmdbInstanceService().addInstanceNoApprove("系统", instanceData, syncType.getLabel() + "新增");
            }
        } else if (CmdbOptType.OPT_DEL == optType) {
            if (StringUtils.isNotBlank(instanceId)) {
                List<Map<String, Object>> instanceList = new ArrayList<>();
                instanceList.add(instanceData);
                cmdbInstanceService.deleteInstancePysicalNoApprove("系统", instanceList, syncType.getLabel() + "删除");
            } else {
                log.info("跳过删除实例,instanceId不存在!");
            }
        }
    }

    private String getInstanceId(Map<String, Object> instanceData) {
        Map<String, Object> dbData = moduleService.getModuleDataByPrimarys(instanceData.get("module_id").toString(), instanceData);
        boolean exists = MapUtils.isNotEmpty(dbData);
        String instanceId = "";
        log.debug("instanceData=={}", instanceData);
        if (instanceData.get("instance_id") != null && StringUtils.isNotBlank(instanceData.get("instance_id").toString())) {
            instanceId = instanceData.get("instance_id").toString();
        }
        if (exists) {
            instanceId = dbData.get("id").toString();
        }
        instanceData.put("exists", exists);
        return instanceId;
    }

    private Map<String, Object> convert(ConfigDictDTO dict, CmdbSyncType type) {
        Map<String, Object> mapValue = Arrays.stream(BeanUtils.getPropertyDescriptors(dict.getClass()))
                .filter(itm -> !"class".equals(itm.getName())).collect(HashMap::new,
                        (map, pd) -> map.put(pd.getName(), ReflectionUtils.invokeMethod(pd.getReadMethod(), dict)),
                        HashMap::putAll);

        Map<String, Object> instanceMap = Maps.newLinkedHashMap();
        CmdbSyncModuleCondition condition = null;
        if (CmdbSyncType.SYS_DICT == type) {
            condition = buildFieldCondition4Dict();
        } else if (CmdbSyncType.SYS_ORG == type) {
            condition = buildFieldCondition4Org();
            mapValue.put("parentId", "0");
            mapValue.put("orgType", "部门");
        }
        String key = condition.getModuleKey();
        // 将设备分类和设备类型作为key，moduleId作为value.映射
        CmdbSyncFieldMapping query = new CmdbSyncFieldMapping();
        query.setMappingType(KafkaConfigConstant.MAPPING_MODULE);
        query.setMappingKey(key);
        CmdbSyncFieldMapping entity = getCmdbSyncFieldMappingService().getOne(query);
        if (entity == null || StringUtils.isBlank(entity.getMappingValue())) {
            throw new IllegalArgumentException("请配置[" + key + "]网管同步到cmdb的模型!!!");
        }
        instanceMap.put("module_id", entity.getMappingValue());
        instanceMap.put("instance_id", mapValue.get("id"));
        instanceMap.put("id", mapValue.get("id"));
        // 网管同步过来的不能 再同步到网管cmdb
        instanceMap.put("noSyncFlag", true);
        instanceMap.put("remark", "网管同步");
        instanceMap.put("dict_type", "数据字典");
        query.setMappingType(condition.getModuleFieldType());
        query.setMappingKey(condition.getModuleFiledKey());
        List<CmdbSyncFieldMapping> list = getCmdbSyncFieldMappingService().list(query);
        instanceMap.putAll(mapValue);
        list.forEach(e -> {
            if (mapValue.containsKey(e.getSrcField())) {
                instanceMap.put(e.getDestField(), mapValue.get(e.getSrcField()));
            }
        });
        return instanceMap;
    }

    protected CmdbSyncModuleCondition buildFieldCondition4Dict() {
        return new CmdbSyncModuleCondition(KafkaConfigConstant.MAPPING_MODULE, KafkaConfigConstant.MAPPING_KEY_CONFIG_DICT,
                KafkaConfigConstant.MAPPING_TYPE_CONFIG_DICT, KafkaConfigConstant.MAPPING_KEY_FIELD);
    }

    protected CmdbSyncModuleCondition buildFieldCondition4Org() {
        return new CmdbSyncModuleCondition(KafkaConfigConstant.MAPPING_MODULE, KafkaConfigConstant.MAPPING_KEY_ORG_SYSTEM,
                KafkaConfigConstant.MAPPING_TYPE_ORG_SYSTEM, KafkaConfigConstant.MAPPING_KEY_FIELD);
    }
}
