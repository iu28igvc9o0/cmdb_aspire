package com.aspire.cmdb.agent.collect.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.cmdb.agent.collect.CollectConst;
import com.aspire.cmdb.agent.collect.entity.CmdbSyncSuyanRelation;
import com.aspire.cmdb.agent.collect.service.CmdbSyncSuyanRelationService;
import com.aspire.cmdb.agent.util.StringUtils;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.dictMapper.payload.CmdbDictMapperEntity;
import com.aspire.ums.cmdb.v3.dictMapper.payload.CmdbSyncFiledMapperEntity;
import com.aspire.ums.cmdb.v3.dictMapper.service.ICmdbDictMapperService;
import com.aspire.ums.cmdb.v3.dictMapper.service.ICmdbSyncFiledMapperService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectFromSuyan
 * Author:   hangfang
 * Date:     2020/9/1
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
@Slf4j
public class CollectFromSuyan {

    @Autowired
    private ICmdbDictMapperService dictMapperService;
    @Autowired
    private ICmdbSyncFiledMapperService syncFiledMapperService;
    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ConfigDictService dictService;
    @Autowired
    private ICmdbConfigService configService;
    @Autowired
    private CmdbSyncSuyanRelationService relationService;
    /**
     * 字典信息
     */
    private Map<String, Map<String, CmdbDictMapperEntity>> dictMap = new HashMap<>();
    /**
     * 设备映射字段信息
     */
    private Map<String, Map<String, CmdbSyncFiledMapperEntity>> serverDictMap = new HashMap<>();
    /**
     * 资源池 Map
     */
    private Map<String, Map<String, String>> idcTypeMap = new HashMap<>();
    /**
     * 工期 Map
     */
    private Map<String, Map<String, String>> projectMap = new HashMap<>();
    /**
     * POD Map
     */
    private Map<String, Map<String, String>> podMap = new HashMap<>();
    /**
     * 模型id
     */
    private Map<String, String> moduleIdMap = new HashMap<>();
    /**
     * pod 资源池 工期 关系
     */
    private Map<String, CmdbSyncSuyanRelation> podRelationMap = new HashMap<>();

    /**
     * 物理机映射字段信息
     */
    private Map<String, ConfigDict> deviceTypeMap = new HashMap<>();


    @KafkaListener(id = "collect-suyan-0", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_collect_from_suyan:cmdbdata-deepwatch}", partitions = {"0","1","2","3","4","5","6","7","8","9","10","11","12"})})
    public void listenZbxPartition12(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-0 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resolveData(cr.value());
    }

    private void resolveData(String value) {
        initMap();
        if (StringUtils.isNotEmpty(value)) {
            JSONObject jsonData = JSON.parseObject(value);
            if (!jsonData.containsKey("data")) {
                log.error("苏研采集数据格式错误[未包含data数据]");
            }
            JSONObject sourceData = JSON.parseObject(jsonData.get("data").toString());
            if (null != sourceData.get("physicalServers")) {
                toHandleData("X86服务器", sourceData.get("physicalServers").toString());
            }
            if (null != sourceData.get("virtualServers")) {
                toHandleData("云主机", sourceData.get("virtualServers").toString());
            }
            if (null != sourceData.get("pods")) {
                toHandleData("pods", sourceData.get("pods").toString());
            }
        }
    }

    private void initMap() {
        List<CmdbDictMapperEntity> dictMapperList = dictMapperService.listByEntity(new CmdbDictMapperEntity("苏研"));
        // 将苏研映射数据配置按dictType分好类
        for (CmdbDictMapperEntity entity : dictMapperList) {
            if (!dictMap.containsKey(entity.getMapperDictType())) {
                dictMap.put(entity.getMapperDictType(), new HashMap<>());
            }
            dictMap.get(entity.getMapperDictType()).put(entity.getMapperDictCode(), entity);
        }
        // 根据设备类型归类映射字段
        List<CmdbSyncFiledMapperEntity> filedMapperList = syncFiledMapperService.listByEntity(new CmdbSyncFiledMapperEntity());
        for (CmdbSyncFiledMapperEntity entity : filedMapperList) {
            String mapperType = entity.getMapperType();
            if (!serverDictMap.containsKey(mapperType)) {
                serverDictMap.put(mapperType, new HashMap<>());
            }
            serverDictMap.get(mapperType).put(entity.getUmsFiledCode(), entity);
        }
        // 获取设备类型
        List<ConfigDict> deviceTypeDicts = dictService.selectDictsByType(CollectConst.COLLECT_DEVICE_TYPE, null, null, null);
        for (ConfigDict dict : deviceTypeDicts) {
            deviceTypeMap.put(dict.getValue(), dict);
        }
        // 获取云主机模型id
        CmdbConfig vmConfig = configService.getConfigByCode(CollectConst.VM_MODULE_ID);
        if (vmConfig == null) {
            log.error("未配置云主机模型id");
            throw new RuntimeException("未配置云主机模型id");
        }
        moduleIdMap.put("云主机", vmConfig.getConfigValue());
        // 获取物理机模型id
        CmdbConfig phyConfig = configService.getConfigByCode(CollectConst.PHY_MODULE_ID);
        if (phyConfig == null) {
            log.error("未配置物理机模型id");
            throw new RuntimeException("未配置物理机模型id");
        }
        moduleIdMap.put("X86服务器", phyConfig.getConfigValue());
        // 获取主机资源模型id
        CmdbConfig insConfig = configService.getConfigByCode(CollectConst.INS_MODULE_ID);
        if (insConfig == null) {
            log.error("未配置主机资源模型id");
            throw new RuntimeException("未配置主机资源模型id");
        }
        moduleIdMap.put("主机资源", insConfig.getConfigValue());
        initPodMap();

    }

    // 获取资源池 pod 工期关系
    private void initPodMap() {
        List<CmdbSyncSuyanRelation> relations = relationService.list(null);
        for (CmdbSyncSuyanRelation relation : relations) {
            podRelationMap.put(relation.getSuyanPodId(), relation);
        }
    }


    /**
     * 根据ip和资源池获取CI
     * @param instanceData instanceData
     * @return Map<String, Object>
     */
    private Map<String, Object> getInstanceByIpAndIdcType(String deviceType, Map<String, Object> instanceData) {
        if (!instanceData.containsKey("ip") || !instanceData.containsKey("idcType")) {
            log.error("苏研数据未上报ip和资源池信息");
            throw new RuntimeException("苏研数据未上报ip和资源池信息");
        }
        Map<String, Object> queryMap = new HashMap<>();
        String ip = instanceData.get("ip").toString();
        String idcType =  instanceData.get("idcType").toString();
        queryMap.put("ip", ip);
        queryMap.put("idcType", idcType);
        Map<String, Object> currInstance = instanceService.queryDeviceByIdcTypeAndIP(queryMap);
        // 如果查出来的设备模型和对应模型不一致
        String deviceModuleId = moduleIdMap.get(deviceType);
        if (!deviceModuleId.equals(instanceData.get("module_id").toString())) {
            throw new RuntimeException("苏研采集设备ip：" + ip + ",资源池: " + idcType + "模型为"
                    + deviceModuleId + ",而查询已存设备模型id为" + currInstance.get("module_id"));
        }
        return instanceService.queryDeviceByIdcTypeAndIP(queryMap);
    }

    private void toHandleData(String deviceType, String dataString) {
        List<Map> deviceList= JSONObject.parseArray(dataString, Map.class);
        if (deviceList.size() > 0 ) {
            Map<String, CmdbSyncFiledMapperEntity> deviceTypeDictMap = serverDictMap.get(deviceType);
            if ( null == deviceTypeDictMap || deviceTypeDictMap.size() == 0) {
                log.error("未配置苏研字段映射");
                return;
            }
            // 开始处理数据
            for (Map<String, Object> device : deviceList) {
                CollectConst.threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        // 检验变更类型
                        if (!device.containsKey("changeType")) {
                            log.error("采集数据中未包含变更类型");
                            throw new RuntimeException("采集数据中未包含变更类型");
                        }
                        int changeType = (Integer) device.get("changeType");
                        if (!(CollectConst.COLLECT_CHANGETYPE_LIST).contains(changeType)) {
                            log.error("当前变更类型[{}]不能被识别，合法变更类型有{}", changeType, CollectConst.COLLECT_CHANGETYPE_LIST);
                            throw new RuntimeException("非法的变更类型");
                        }
                        // 处理操作数据
                        Map<String, Object> instanceData = new HashMap<>();
                        instanceData.put("module_id", moduleIdMap.get(deviceType));
                        // 设备分类
                        instanceData.put(CollectConst.COLLECT_DEVICE_CLASS, deviceTypeMap.get(deviceType).getPid());
                        // 设备类型
                        instanceData.put(CollectConst.COLLECT_DEVICE_TYPE, deviceTypeMap.get(deviceType).getId());
                        for (String umsKey : deviceTypeDictMap.keySet()) {
                            //判断苏研字段是否有被映射管理
                            String suyanKey1 = deviceTypeDictMap.get(umsKey).getOtherFiledCode();
                            // 判断苏研设备中是否包含我们所需要的key
                            if (device.containsKey(suyanKey1)) {
                                Object suyanValue = device.get(suyanKey1);
                                if (!StringUtils.isNotEmpty(suyanValue)) {
                                    log.info("采集最新值为空，进行过滤");
                                    return;
                                }
                                // 如果有相关的字典值映射则取映射值（如业务系统）
                                if (dictMap.containsKey(umsKey)) {
                                    CmdbDictMapperEntity valueMapper = dictMap.get(umsKey).get(suyanValue);
                                    if (valueMapper == null || StringUtils.isEmpty(valueMapper.getUmsDictCode())) {
                                        log.error("苏研字段{} UMS字段{} 的值[{}]未被管理", suyanKey1, umsKey, suyanValue);
                                        continue;
//                                        throw new RuntimeException("苏研字段" + suyanKey1 + " UMS字段" + umsKey + " 的值[" + suyanValue + "]未被管理");
                                    }
                                    instanceData.put(umsKey, valueMapper.getUmsDictCode());
                                } else {
                                    if ("date".equals(deviceTypeDictMap.get(umsKey).getFiledType())) {
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        long value = Long.valueOf(suyanValue.toString()).longValue();
                                        instanceData.put(umsKey, df.format(new Date(value)));
                                    } else {
                                        instanceData.put(umsKey, suyanValue);
                                    }

                                }
                            } else {
                                log.info("第三方未提供采集字段苏研字段：[{}], UMS:[{}]",suyanKey1, umsKey);
                            }
                        }
                        toHandlePod(deviceType, device, instanceData);
                        if (changeType == CollectConst.COLLECT_SUYAN_CHANGTYPE_DELETE) {
//                            // 删除
//                            Map<String, Object> currInstance = getInstanceByIpAndIdcType(deviceType, instanceData);
                            if (instanceData.containsKey("id")) {
                                List<Map<String, Object>> instanceList = new ArrayList<>();
                                instanceList.add(instanceData);
//                            if (currInstance != null) {
                                instanceService.deleteInstance("系统管理员", instanceList, "苏研数据");
//                            }
                            }
                        } else {
                            if (instanceData.containsKey("id")) {
                                // 更新
                                instanceService.updateInstance(instanceData.get("id").toString(), "系统管理员", instanceData, "苏研数据");

                            } else {
                                // 新增
                                instanceService.addInstance("系统管理员", instanceData, "苏研数据");
                            }
                        }
//                        // 新增
//                        if (changeType == CollectConst.COLLECT_SUYAN_CHANGTYPE_ADD) {
//                            instanceService.addInstance("系统管理员", instanceData, "苏研数据");
//                        } else if (changeType == CollectConst.COLLECT_SUYAN_CHANGTYPE_UPDATE) {
//                            // 更新
//                            Map<String, Object> currInstance = getInstanceByIpAndIdcType(deviceType, instanceData);
//                            // 设备分类
//                            instanceData.put(CollectConst.COLLECT_DEVICE_CLASS, deviceTypeMap.get(deviceType).getPid());
//                            // 设备类型
//                            instanceData.put(CollectConst.COLLECT_DEVICE_TYPE, deviceTypeMap.get(deviceType).getId());
//                            if (currInstance == null) {
//                                // 如果未查询设备则新增
//                                instanceService.addInstance("系统管理员", instanceData, "苏研数据");
//                            } else {
//                                instanceService.updateInstance(currInstance.get("id").toString(), "系统管理员", instanceData, "苏研数据");
//                            }
//                        } else if (changeType == CollectConst.COLLECT_SUYAN_CHANGTYPE_DELETE) {
//                            // 删除
//                            Map<String, Object> currInstance = getInstanceByIpAndIdcType(deviceType, instanceData);
//                            List<Map<String, Object>> instanceList = new ArrayList<>();
//                            instanceList.add(currInstance);
//                            if (currInstance != null) {
//                                instanceService.deleteInstance("系统管理员", instanceList, "苏研数据");
//                            }
//                        }
                        log.info(">>>设备数据{}入审核成功<<<", device);
                    }
                });
            }

        }
    }

    private void toHandlePod(String deviceType, Map<String, Object> device, Map<String, Object> instanceData) {
        if ("云主机".equals(deviceType)) {
            if (!device.containsKey("physicalServer") || !StringUtils.isNotEmpty(device.get("physicalServer"))) {
                log.error("苏研数据未上报云主机的宿主机信息");
                throw new RuntimeException("苏研数据未上报云主机的宿主机信息");
            }
            Map<String, Object> params = new HashMap<>();
            // 根据resourceId获取instanceId
            String suyanUuid = device.get("resourceId").toString();
            params = new HashMap<>();
            params.put("suyan_uuid", suyanUuid);
            Map<String, Object> vmData = instanceService.getByParams(moduleIdMap.get("主机资源"), params);
            if (vmData != null && vmData.size() > 0) {
                instanceData.put("id", vmData.get("id"));
                instanceData.put("idcType", vmData.get("idcType"));
                instanceData.put("project_name", vmData.get("project_name"));
                instanceData.put("pod_name", vmData.get("pod_name"));
                instanceData.put("exsi_ip", vmData.get("exsi_ip"));
            } else {
                log.info("云主机设备不存在，开始查询宿主机信息");
                // 根据苏研UUID获取宿主机信息
                String existSyUuid = device.get("physicalServer").toString();
                params.put("suyan_uuid", existSyUuid);
                Map<String, Object> phyData = instanceService.getByParams(moduleIdMap.get("主机资源"), params);
                if (phyData == null || phyData.size() == 0) {
                    log.error("未查询到苏研UUID为[{}]宿主机设备", existSyUuid);
                    throw new RuntimeException("未查询到苏研UUID为[" + existSyUuid + "]宿主机设备");
                }
                instanceData.put("ip", instanceData.get("ServiceIP"));
                instanceData.put("exsi_ip", phyData.get("ip"));
                instanceData.put("idcType", phyData.get("idcType"));
                instanceData.put("project_name", phyData.get("project_name"));
                instanceData.put("pod_name", phyData.get("pod_name"));
            }
        } else {
            if (!device.containsKey("pod") || device.get("pod") == null) {
                log.error("苏研数据未上报POD池信息");
                throw new RuntimeException("苏研数据未上报POD池信息");
            }
            String podName = instanceData.get("pod_name").toString();
            if (!podRelationMap.containsKey(podName)) {
                log.error("pod池[{}]未被管理",podName);
                throw new RuntimeException("pod池["+ podName +"]未被管理");
            }
            CmdbSyncSuyanRelation relation = podRelationMap.get(podName);
            instanceData.put("idcType", relation.getUmsIdcId());
            instanceData.put("project_name", relation.getUmsProjectId());
            instanceData.put("pod_name", relation.getUmsPodId());
            // 更新
            Map<String, Object> currInstance = getInstanceByIpAndIdcType(deviceType, instanceData);
            if (currInstance != null && currInstance.size() > 0) {
                instanceData.put("id", currInstance.get("id"));
            }
//                 if (currInstance == null) {
//                // 如果未查询设备则新增
//                instanceService.addInstance("系统管理员", instanceData, "苏研数据");
//            } else {
//                instanceService.updateInstance(currInstance.get("id").toString(), "系统管理员", instanceData, "苏研数据");
//            }
        }
    }
}
