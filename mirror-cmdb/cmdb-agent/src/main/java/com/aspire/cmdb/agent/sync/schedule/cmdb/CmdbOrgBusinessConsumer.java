package com.aspire.cmdb.agent.sync.schedule.cmdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncFieldMapping;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.sync.payload.CmdbSyncModuleCondition;
import com.aspire.ums.cmdb.sync.payload.OrgBusinessDTO;
import com.aspire.ums.cmdb.sync.service.CmdbSyncReceiveMessageLogService;
import com.aspire.ums.cmdb.sync.service.ICmdbSyncFieldMappingService;
import com.aspire.ums.cmdb.systemManager.service.OrgManagerService;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import lombok.Data;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * 部门-财务业务线同步 (财务业务线修改为作为二级部门挂到一级部门下面).
 *
 * @author jiangxuwen
 * @date 2020/5/18 18:50
 */
@Data
@Component
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class CmdbOrgBusinessConsumer {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    private CmdbSyncReceiveMessageLogService receiveMessageLogService;

    @Autowired
    private ICmdbSyncFieldMappingService cmdbSyncFieldMappingService;

    @Autowired
    private ICmdbInstanceService cmdbInstanceService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private OrgManagerService orgManagerService;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public CmdbSyncReceiveMessageLogService getReceiveMessageLogService() {
        if (receiveMessageLogService == null) {
            receiveMessageLogService = SpringUtils.getBean(CmdbSyncReceiveMessageLogService.class);
        }
        return receiveMessageLogService;
    }

    @KafkaListener(id = "cmdb-finance-business-sync-kafka-0",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_finance_business:osa_cmdb_finance_business}",
                    partitions = { "0" }) })
    public void listenPartition0(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-finance-business-sync-kafka-0 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-finance-business-sync-kafka-1",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_finance_business:osa_cmdb_finance_business}",
                    partitions = { "1" }) })
    public void listenPartition1(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-finance-business-sync-kafka-1 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-finance-business-sync-kafka-2",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_finance_business:osa_cmdb_finance_business}",
                    partitions = { "2" }) })
    public void listenPartition2(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-finance-business-sync-kafka-2 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-finance-business-sync-kafka-3",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_finance_business:osa_cmdb_finance_business}",
                    partitions = { "3" }) })
    public void listenPartition3(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-finance-business-sync-kafka-3 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-finance-business-sync-kafka-4",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_finance_business:osa_cmdb_finance_business}",
                    partitions = { "4" }) })
    public void listenPartition4(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-finance-business-sync-kafka-4 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-finance-business-sync-kafka-5",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_finance_business:osa_cmdb_finance_business}",
                    partitions = { "5" }) })
    public void listenPartition5(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-finance-business-sync-kafka-5 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-finance-business-sync-kafka-6",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_ConfigDict}",
                    partitions = { "6" }) })
    public void listenPartition6(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-finance-business-sync-kafka-6 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-finance-business-sync-kafka-7",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_finance_business:osa_cmdb_finance_business}",
                    partitions = { "7" }) })
    public void listenPartition7(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-finance-business-sync-kafka-7 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-finance-business-sync-kafka-8",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_finance_business:osa_cmdb_finance_business}",
                    partitions = { "8" }) })
    public void listenPartition8(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-finance-business-sync-kafka-8 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    @KafkaListener(id = "cmdb-finance-business-sync-kafka-9",
            containerFactory = "ackContainerFactory",
            topicPartitions = { @TopicPartition(topic = "${kafka.osa_topic.osa_cmdb_finance_business:osa_cmdb_finance_business}",
                    partitions = { "9" }) })
    public void listenPartition9(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("cmdb-finance-business-sync-kafka-9 Listener, Thread ID:{}", Thread.currentThread().getId());
        onMessage(records, ack);
    }

    private void onMessage(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        try {
            if (CollectionUtils.isEmpty(records)) {
                log.info("[部门-财务业务线同步] message is empty!!!");
                return;
            }
            log.info(">>>>>>>>>> [部门-财务业务线同步]收到消息，准备处理 >>>>>>");
            log.info("[部门-财务业务线同步]收到消息的数量:{}", records.size());
            for (ConsumerRecord<?, ?> record : records) {
                Optional<?> kafkaMessage = Optional.ofNullable(record.value());
                if (kafkaMessage.isPresent()) {
                    Object message = record.value();
                    String topic = record.topic();
                    Object key = record.key();
                    long offset = record.offset();
                    int partition = record.partition();
                    log.info("[部门-财务业务线同步] 参数：topic:[{}],key:[{}],offset:[{}],partition:[{}]", topic, key, offset, partition);
                    log.info("[部门-财务业务线同步] 消息内容为:{}", message);
                    handleMsg(message.toString());
                }
                log.info("<<<<<<<< [部门-财务业务线同步] 消息处理完成 <<<<<<<<<<<<<");
            }
        } catch (Exception e) {
            log.error("[部门-财务业务线同步]失败.", e);
        } finally {
            ack.acknowledge();
        }

    }

    private void handleMsg(String value) {
        try {
            CmdbSyncSendMessageLog messageLog = JsonMapper.getInstance().readValue(value, CmdbSyncSendMessageLog.class);
            getReceiveMessageLogService().saveReceiveLog(messageLog);
            String objectName = messageLog.getObjectName();
            String eventType = messageLog.getEventType();
            CmdbOptType optType = CmdbOptType.fromValue(eventType);
            log.info("[部门-财务业务线同步]开始入库,操作类型:[{}],操作表[{}]", optType.getLabel(), objectName);
            saveSysOrg(messageLog);
            log.info("[部门-财务业务线同步] 入库[{}]成功!", objectName);
        } catch (Exception e) {
            log.error("[部门-财务业务线同步]入库失败!", e);
        }
    }

    private void saveSysOrg(CmdbSyncSendMessageLog messageLog) throws Exception {
        String msgBody = messageLog.getMsgBody();
        OrgBusinessDTO dto = JsonMapper.getInstanceWithSnakeCase().readValue(msgBody, OrgBusinessDTO.class);
        String eventType = messageLog.getEventType();
        CmdbOptType optType = CmdbOptType.fromValue(eventType);
        save2Module(dto, optType);
    }

    private void save2Module(OrgBusinessDTO dto, CmdbOptType optType) {
        Map<String, Object> instanceData = convert(dto);

        // TODO: 更新业务线，需要更新cmdb_business_system和cmdb_instance等关联业务线的数据
        Preconditions.checkNotNull(instanceData.get("module_id"), "module_id cannot be null");
        String instanceId = dto.getId();
        log.debug("instanceId=={}", instanceId);
        if (CmdbOptType.OPT_ADD == optType || CmdbOptType.OPT_MODIFY == optType) {
            Map<String, Object> instanceDetailMap = getCmdbInstanceService()
                    .getInstanceDetail(instanceData.get("module_id").toString(), instanceData.get("instance_id").toString());
            if (MapUtils.isNotEmpty(instanceDetailMap) && instanceDetailMap.get("id") != null) {
                getCmdbInstanceService().updateInstance(instanceData);
            } else {
                getCmdbInstanceService().addInstanceNoApprove("系统", instanceData, "新增");
            }
        } else if (CmdbOptType.OPT_DEL == optType) {
            if (StringUtils.isNotBlank(instanceId)) {
                List<Map<String, Object>> instanceList = new ArrayList<>();
                instanceList.add(instanceData);
                cmdbInstanceService.deleteInstancePysicalNoApprove("系统", instanceList, "删除");
            } else {
                log.info("跳过删除实例,instanceId不存在!");
            }
        }
    }

    private Map<String, Object> convert(OrgBusinessDTO dto) {
        Map<String, Object> mapValue = Arrays.stream(BeanUtils.getPropertyDescriptors(dto.getClass()))
                .filter(itm -> !"class".equals(itm.getName())).collect(HashMap::new,
                        (map, pd) -> map.put(pd.getName(), ReflectionUtils.invokeMethod(pd.getReadMethod(), dto)), HashMap::putAll);

        Map<String, Object> instanceMap = Maps.newLinkedHashMap();
        CmdbSyncModuleCondition condition = buildFieldCondition4Org();
        mapValue.put("parentId", dto.getDepartmentId());
        mapValue.put("orgType", "科室");
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

    protected CmdbSyncModuleCondition buildFieldCondition4Org() {
        return new CmdbSyncModuleCondition(KafkaConfigConstant.MAPPING_MODULE,
                KafkaConfigConstant.MAPPING_KEY_CMDB_FINANCE_BUSINESS, KafkaConfigConstant.MAPPING_TYPE_FIN_BUSINESS,
                KafkaConfigConstant.MAPPING_KEY_FIELD);
    }

    public static void main(String[] args) throws Exception {
        String json = "{\"id\":\"9b78f99ce18b402ab49b44d474f033df\",\"department_id\":\"3593a44ae2a642af93e8e2748cf3cbb8\",\"financial_name\":\"测试财务业务线001\",\"department_name\":\"云产品事业部\",\"cmdb_business_name\":null,\"cmdb_business_code\":null,\"cmdb_business_id\":null}";
        OrgBusinessDTO dto = JsonMapper.getInstanceWithSnakeCase().readValue(json, OrgBusinessDTO.class);
        System.out.println(dto);
    }
}
