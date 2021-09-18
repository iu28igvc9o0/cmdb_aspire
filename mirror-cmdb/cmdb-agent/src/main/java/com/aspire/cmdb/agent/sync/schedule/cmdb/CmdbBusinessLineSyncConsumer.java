package com.aspire.cmdb.agent.sync.schedule.cmdb;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncFieldMapping;
import com.aspire.ums.cmdb.sync.payload.CmdbBusinessDomain;
import com.aspire.ums.cmdb.sync.payload.CmdbBusinessMessage;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.sync.payload.CmdbSyncModuleCondition;
import com.aspire.ums.cmdb.sync.payload.CmdbSyncType;
import com.aspire.ums.cmdb.sync.service.ICmdbSyncFieldMappingService;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

/**
 * cmdb业务线监听(通过RabbitMQ发送) 业务系统+独立业务线.
 *
 * @author jiangxuwen
 * @date 2020/5/20 11:37
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class CmdbBusinessLineSyncConsumer {

    private ICmdbInstanceService cmdbInstanceService;

    private ICmdbSyncFieldMappingService cmdbSyncFieldMappingService;

    private ModuleService moduleService;

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

    public ModuleService getModuleService() {
        if (moduleService == null) {
            moduleService = SpringUtils.getBean(ModuleService.class);
        }
        return moduleService;
    }

    @RabbitListener(queues = "${businessLine.mq.queue.name}")
    public void consumer(Channel channel, Message message) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String messageBody = new String(message.getBody(), Charset.forName("utf-8"));
        // messageBody = messageBody.replaceAll("(\r\n)|\n", "");
        log.info(">>>>>>>[独立业务线同步]收到rabbitMQ的消息:{}", messageBody);
        try {
            log.info("[独立业务线同步]处理消息，入库");
            CmdbBusinessMessage cmdbBusinessMessage = new JsonMapper().readValue(messageBody, CmdbBusinessMessage.class);
            saveCmdbBusiness(cmdbBusinessMessage);
            // 确认收到消息，false只确认当前consumer一个消息收到，true确认所有consumer获得的消息
            channel.basicAck(deliveryTag, false);
            log.info("<<<<<<<<<<<[独立业务线同步]处理完成!");
        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered()) {
                log.warn("消息已重复处理失败,拒绝再次接收");
                // 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                log.info("消息即将再次返回队列处理");
                // requeue为是否重新回到队列，true重新入队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
            log.error("[独立业务线同步]消息确认失败!", e);
            // channel.basicNack(deliveryTag, false, true);
        }
    }

    private void saveCmdbBusiness(CmdbBusinessMessage cmdbBusinessMessage) {
        try {
            List<CmdbBusinessDomain> addList = cmdbBusinessMessage.getData().getAddList();
            List<CmdbBusinessDomain> updateList = cmdbBusinessMessage.getData().getUpdateList();
            List<CmdbBusinessDomain> removeList = cmdbBusinessMessage.getData().getRemoveList();
            log.info("[独立业务线同步],待新增的数量:[{}]", addList.size());
            log.info("[独立业务线同步],待修改的数量:[{}]", updateList.size());
            log.info("[独立业务线同步],待删除的数量:[{}]", removeList.size());
            persist(addList, CmdbOptType.OPT_ADD);
            persist(updateList, CmdbOptType.OPT_MODIFY);
            persist(removeList, CmdbOptType.OPT_DEL);
            log.info("[独立业务线同步]处理完成");
        } catch (Exception e) {
            log.error("[独立业务线同步]入库失败.", e);
        }
    }

    /**
     * 构建父子节点.
     * 
     * @param
     * @return
     */
    private static List<CmdbBusinessDomain> buildTree(List<CmdbBusinessDomain> list) {
        List<CmdbBusinessDomain> businessDomainList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        Map<String, CmdbBusinessDomain> businessMap = Maps.newHashMap();

        for (CmdbBusinessDomain domain : list) {
            businessMap.put(domain.getBusinessCode(), domain);
        }
        for (CmdbBusinessDomain domain : list) {
            CmdbBusinessDomain child = domain;
            if (StringUtils.isBlank(child.getParentCode())) {
                businessDomainList.add(domain);
            } else {
                CmdbBusinessDomain parent = businessMap.get(child.getParentCode());
                // 容错处理，避免NPE
                if (parent == null) {
                    businessDomainList.add(domain);
                } else {
                    parent.getChildren().add(child);
                }
            }
        }

        return businessDomainList;
    }

    /**
     * 处理入库逻辑.
     * 
     * @param
     * @return
     */
    private void persist(List<CmdbBusinessDomain> list, CmdbOptType optType) {
        List<CmdbBusinessDomain> treeList = buildTree(list);
        for (CmdbBusinessDomain domain : treeList) {
            List<CmdbBusinessDomain> childs = domain.getChildren();
            // 插入模型
            saveToModule(domain, optType);
            if (CollectionUtils.isEmpty(childs)) {
                continue;
            }
            childs.forEach(e -> {
                // 插入模型
                saveToModule(domain, optType);
            });
        }
    }

    private void saveToModule(CmdbBusinessDomain domain, CmdbOptType optType) {
        if (StringUtils.isBlank(domain.getBusinessCode())) {
            return;
        }
        CmdbSyncType syncType = getSyncType();
        Map<String, Object> instanceData = convert(domain);

        // // 如果parentName为空，查询数据库
        // if (StringUtils.isNotBlank(domain.getParentCode()) && StringUtils.isBlank(domain.getParentName())) {
        // // 暂时写死
        // Map<String, Object> parentInstanceData = Maps.newHashMap();
        // parentInstanceData.putAll(instanceData);
        // parentInstanceData.put("business_code", domain.getParentCode());
        // Map<String, Object> parentData = getModuleService().getModuleDataByPrimarys(instanceData.get("module_id").toString(),
        // parentInstanceData);
        // if (MapUtils.isNotEmpty(parentData)) {
        // String pId = parentData.get("id").toString();
        // instanceData.put("parentBusiness", pId);
        // }
        // }

        // TODO: 更新业务线，需要更新cmdb_business_system和cmdb_instance等关联业务线的数据
        Preconditions.checkNotNull(instanceData.get("module_id"), "module_id cannot be null");
        String instanceId = domain.getBusinessId();
        log.debug("instanceId=={}", instanceId);
        // if (CmdbOptType.OPT_ADD == optType) {
        // instanceId = getInstanceId(instanceData);
        // boolean exists = BooleanUtils.toBoolean(instanceData.get("exists").toString());
        // if (exists) {
        // log.info("跳过新增,模型主键存在相同的数据:{},更新！", instanceId);
        // getCmdbInstanceService().updateInstance(instanceData);
        // } else {
        // getCmdbInstanceService().addInstanceNoApprove("系统", instanceData, syncType.getLabel() + "新增");
        // }
        // } else if (CmdbOptType.OPT_MODIFY == optType) {
        // if (StringUtils.isNotBlank(instanceId)) {
        // instanceData.put("instance_id", instanceId);
        // instanceData.put("id", instanceId);
        // getCmdbInstanceService().updateInstance(instanceData);
        // } else {
        // log.info("跳过更新实例,instanceId不存在!");
        // }
        // }
        if (CmdbOptType.OPT_ADD == optType || CmdbOptType.OPT_MODIFY == optType) {
            Map<String, Object> instanceDetailMap = getCmdbInstanceService()
                    .getInstanceDetail(instanceData.get("module_id").toString(), instanceData.get("instance_id").toString());
            if (MapUtils.isNotEmpty(instanceDetailMap) && instanceDetailMap.get("id") != null) {
                getCmdbInstanceService().updateInstance(instanceData);
            } else {
                getCmdbInstanceService().addInstanceNoApprove("系统", instanceData, syncType.getLabel() + "新增");
            }
        } else if (CmdbOptType.OPT_DEL == optType) {
            delBizSystemCascade(instanceId);
            if (StringUtils.isNotBlank(instanceId)) {
                List<Map<String, Object>> instanceList = new ArrayList<>();
                instanceList.add(instanceData);
                getCmdbInstanceService().deleteInstanceNoApprove("系统", instanceList, syncType.getLabel() + "删除");
            } else {
                log.info("跳过删除实例,instanceId不存在!");
            }
        }
    }

    private String getInstanceId(Map<String, Object> instanceData) {
        Map<String, Object> dbData = getModuleService().getModuleDataByPrimarys(instanceData.get("module_id").toString(),
                instanceData);
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

    private Map<String, Object> convert(CmdbBusinessDomain domain) {
        Map<String, Object> mapValue = Arrays.stream(BeanUtils.getPropertyDescriptors(domain.getClass()))
                .filter(itm -> !"class".equals(itm.getName())).collect(HashMap::new,
                        (map, pd) -> map.put(pd.getName(), ReflectionUtils.invokeMethod(pd.getReadMethod(), domain)),
                        HashMap::putAll);

        CmdbSyncModuleCondition condition = buildFieldCondition();
        Map<String, Object> instanceMap = Maps.newLinkedHashMap();
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
        instanceMap.put("instance_id", mapValue.get("businessId"));
        instanceMap.put("id", mapValue.get("businessId"));
        // 网管同步过来的不能 再同步到网管cmdb
        instanceMap.put("noSyncFlag", true);
        // instanceMap.put("department1", "中移互联网公司");
        instanceMap.put("business_type", "业务系统");
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

    protected CmdbSyncModuleCondition buildFieldCondition() {
        return new CmdbSyncModuleCondition(KafkaConfigConstant.MAPPING_MODULE, KafkaConfigConstant.MAPPING_KEY_CMDB__BUSINESS_LINE,
                KafkaConfigConstant.MAPPING_TYPE_CMDB_BUSINESS_LINE, KafkaConfigConstant.MAPPING_KEY_FIELD);
    }

    protected CmdbSyncType getSyncType() {
        return CmdbSyncType.CMDB_BUSINESS_LINE;
    }

    /**
     * 删除独立业务线的时候 要删除业务系统.
     * 
     * @param
     * @return
     */
    private void delBizSystemCascade(String bizSystemId) {
        try {
            CmdbSyncModuleCondition condition = new CmdbSyncModuleCondition(KafkaConfigConstant.MAPPING_MODULE,
                    KafkaConfigConstant.MAPPING_KEY_FINANCE_CMDB_BUSINESS, KafkaConfigConstant.MAPPING_TYPE_FINANCE_CMDB_BUSINESS,
                    KafkaConfigConstant.MAPPING_KEY_FIELD);
            Map<String, Object> instanceMap = Maps.newLinkedHashMap();
            String key = condition.getModuleKey();
            CmdbSyncFieldMapping query = new CmdbSyncFieldMapping();
            query.setMappingType(KafkaConfigConstant.MAPPING_MODULE);
            query.setMappingKey(key);
            CmdbSyncFieldMapping entity = getCmdbSyncFieldMappingService().getOne(query);
            if (entity == null || StringUtils.isBlank(entity.getMappingValue())) {
                throw new IllegalArgumentException("请配置[" + key + "]网管同步到cmdb的模型!!!");
            }
            instanceMap.put("module_id", entity.getMappingValue());
            instanceMap.put("bizSystem", bizSystemId);
            instanceMap.put("condicationCode", "business_list");

            Result<Map<String, Object>> mapResult = getCmdbInstanceService().getInstanceList(instanceMap, "");
            if (mapResult == null || CollectionUtils.isEmpty(mapResult.getData())) {
                log.info("没有此instanceId:[{}]对应的业务系统!", bizSystemId);
                return;
            }
            List<Map<String, Object>> instanceList = mapResult.getData();
            for (Map<String, Object> instance : instanceList) {
                instance.put("module_id", instanceMap.get("module_id"));
                instance.put("instance_id", instance.get("id"));
            }
            getCmdbInstanceService().deleteInstancePysicalNoApprove("系统", mapResult.getData(), "删除");
        } catch (Exception e) {
            log.error("删除独立业务线:[{}]对应的业务系统失败!", bizSystemId, e);
        }
    }

    public static void main(String[] args) throws Exception {
        // String json = FileUtils.readFileToString(new File("G:\\aspire\\IdeaProjects\\osa_project\\new_cmdb\\message.json"));
        // json = json.replaceAll("(\r\n|\n)", "");
        try {
            CmdbBusinessMessage cmdbBusinessMessage = JsonMapper.getInstance().readValue(
                    new File("G:\\aspire\\IdeaProjects\\osa_project\\new_cmdb\\message.json"),
                    new TypeReference<CmdbBusinessMessage>() {});
            // System.out.println(cmdbBusinessMessage);
            List<CmdbBusinessDomain> treeList = buildTree(cmdbBusinessMessage.getData().getAddList());
            System.out.println(treeList);
            CmdbBusinessLineSyncConsumer test = new CmdbBusinessLineSyncConsumer();
            for (CmdbBusinessDomain domain : treeList) {
                List<CmdbBusinessDomain> childs = domain.getChildren();
                Map<String, Object> dataMap = test.convert(domain);
                System.out.println("dataMap=" + dataMap);
                if (CollectionUtils.isEmpty(childs)) {
                    continue;
                }
                childs.forEach(e -> {
                    Map<String, Object> dataMap2 = test.convert(domain);
                    System.out.println("dataMap2=" + dataMap2);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
