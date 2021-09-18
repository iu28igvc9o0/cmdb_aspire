package com.aspire.ums.cmdb.ipCollect.service.impl;

import com.aspire.ums.cmdb.ipCollect.enums.EventInstanceModuleEnum;
import com.aspire.ums.cmdb.ipCollect.payload.entity.*;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.*;
import com.aspire.ums.cmdb.ipCollect.service.*;
import com.aspire.ums.cmdb.ipCollect.utils.VmwareInstanceQueryHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自动化推送数据处理
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 14:42
 */
@Service("CmdbCollectApiService")
@Slf4j
public class CmdbCollectApiServiceImpl implements CmdbCollectApiService {

    @Autowired
    private CmdbIpCollectService collectService;
    @Autowired
    private CmdbVipCollectService vipConllectService;
    @Autowired
    private CmdbVmwareNetworkPortGroupService networkPortGroupService;
    @Autowired
    private CmdbVmwareInstanceLogService instanceLogService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public boolean instanceCreate(InstanceCreateRequest request) {
        String objectId = request.getData().getExtInfo().getObjectId();
        String instanceId = request.getData().getExtInfo().getInstanceId();
        String eventId = request.getData().getEventId();
        String eventType = request.getData().getEvent();
        log.info("[实例创建]处理中,参数:objectId=[{}],instanceId=[{}],eventId=[{}],eventType=[{}]", objectId, instanceId, eventId,
                eventType);
        EventInstanceModuleEnum moduleEnum = EventInstanceModuleEnum.fromKey(objectId);
        if (moduleEnum == null){
            return false;
        }
        this.saveLog(request);
        switch (moduleEnum) {
            case IP_ADDRESS_POOL:
                //存活ip采集-网络设备
                createIpAddressPool(eventId, instanceId);
                break;
            case IP_CONF_POOL:
                //存活ip采集-网络设备配置文件解析
                createIpConfPool(eventId, instanceId);
                break;
            case IP_ARP_POOL:
                //存活ip采集-arp扫描网段地址
                createIpArpPool(eventId, instanceId);
                break;
            case VIP_ADDRESS_POOL:
                //虚拟ip采集
                createVipAddressPool(eventId, instanceId);
                break;
            case VMWARE_NETWORK_PORTGROUP:
                // 网段-端口组
                networkPortGroupService.buildAndCreateInstance(eventId, instanceId);
                break;
            default:
                throw new RuntimeException("不支持的类型！");
        }
        return true;
    }

    @Override
    public boolean instanceUpdate(InstanceUpdateRequest request) {
        InstanceUpdateRequestExtInfo extInfo = request.getData().getExtInfo();
        String objectId = extInfo.getObjectId();
        String instanceId = extInfo.getInstanceId();
        String eventType = request.getData().getEvent();
        String eventId = request.getData().getEventId();
        log.info("[实例修改]处理中,参数:objectId=[{}],instanceId=[{}],eventId=[{}],eventType=[{}]", objectId, instanceId, eventId,
                eventType);
        EventInstanceModuleEnum moduleEnum = EventInstanceModuleEnum.fromKey(objectId);
        if (moduleEnum == null){
            return false;
        }
        this.saveLog(request);
        Map<String, Object> updateMap = extractUpdateField(request);
        switch (moduleEnum) {
            case IP_ADDRESS_POOL:
                // 存活ip更新
                createIpAddressPool(eventId, instanceId);
//                List<CmdbIpAddressPoolEntity> entitys1 = collectService.findEntityByInstanceId(Arrays.asList(instanceId), CmdbIpAddressPoolEntity.class);
//                if (CollectionUtils.isEmpty(entitys1)) {
//                    createIpAddressPool(eventId, instanceId);
//                } else {
//                    CmdbIpAddressPoolEntity cmdbIp = entitys1.get(0);
//                    Object versionObj = updateMap.get("version");
//                    if (null != versionObj) {
//                        int version = (int) versionObj;
//                        if (version != cmdbIp.getVersion()) { // 如果版本不同则表示需要更新，保留历史版本
//                            createIpAddressPool(eventId, instanceId);
//                        }
//                    }
////                    collectService.modifyForMap(updateMap, CmdbIpAddressPoolEntity.class);
//                }
                break;
            case IP_CONF_POOL:
                //存活ip采集-网络设备配置文件解析
                createIpConfPool(eventId, instanceId);
//                List<CmdbIpConfPoolEntity> entitys2 = collectService.findEntityByInstanceId(Arrays.asList(instanceId), CmdbIpConfPoolEntity.class);
//                if (CollectionUtils.isEmpty(entitys2)) {
//                    createIpConfPool(eventId, instanceId);
//                } else {
//                    CmdbIpConfPoolEntity cmdbIp = entitys2.get(0);
//                    Object versionObj = updateMap.get("version");
//                    if (null != versionObj) {
//                        int version = (int) versionObj;
//                        if (version != cmdbIp.getVersion()) { // 如果版本不同则表示需要更新，保留历史版本
//                            createIpAddressPool(eventId, instanceId);
//                        }
//                    }
////                    collectService.modifyForMap(updateMap, CmdbIpConfPoolEntity.class);
//                }
                break;
            case IP_ARP_POOL:
                //存活ip采集-arp扫描网段地址
                createIpArpPool(eventId, instanceId);
//                List<CmdbIpArpPoolEntity> entitys3 = collectService.findEntityByInstanceId(Arrays.asList(instanceId), CmdbIpArpPoolEntity.class);
//                if (CollectionUtils.isEmpty(entitys3)) {
//                    createIpArpPool(eventId, instanceId);
//                } else {
//                    CmdbIpArpPoolEntity cmdbIp = entitys3.get(0);
//                    Object versionObj = updateMap.get("version");
//                    if (null != versionObj) {
//                        int version = (int) versionObj;
//                        if (version != cmdbIp.getVersion()) { // 如果版本不同则表示需要更新，保留历史版本
//                            createIpAddressPool(eventId, instanceId);
//                        }
//                    }
////                    collectService.modifyForMap(updateMap, CmdbIpArpPoolEntity.class);
//                }
                break;
            case VIP_ADDRESS_POOL:
                // 虚拟ip更新
                CmdbVipCollectEntity entity2 = vipConllectService.findDataByInstanceId(instanceId);
                if (entity2 == null) {
                    createIpAddressPool(eventId, instanceId);
                } else {
                    vipConllectService.updateByInstanceId(updateMap);
                }
                break;
            case VMWARE_NETWORK_PORTGROUP:
                // 网段-端口组
                CmdbVmwareNetworkPortGroup networkPortGroup = networkPortGroupService.findByInstanceId(instanceId);
                if (networkPortGroup == null) {
                    log.info("instanceId:[{}]对应的VmwareNetworkPortGroup不存在，新增!", instanceId);
                    networkPortGroupService.buildAndCreateInstance(eventId, instanceId);
                } else {
                    networkPortGroupService.updateByInstanceId(updateMap);
                }
                break;
            default:
                throw new RuntimeException("不支持的类型！");
        }
        return true;
    }

    @Override
    public boolean instanceDelete(InstanceDeleteRequest request) {
        String objectId = request.getData().getExtInfo().getObjectId();
        String instanceId = request.getData().getExtInfo().getInstanceId();
        String eventType = request.getData().getEvent();
        String eventId = request.getData().getEventId();
        log.info("[实例删除]处理中,参数:objectId=[{}],instanceId=[{}],eventId=[{}],eventType=[{}]", objectId, instanceId, eventId,
                eventType);
        EventInstanceModuleEnum moduleEnum = EventInstanceModuleEnum.fromKey(objectId);
        if (moduleEnum == null){
            return false;
        }
        this.saveLog(request);
        switch (moduleEnum) {
            case IP_ADDRESS_POOL:
                //存活ip采集-网络设备
                collectService.batchDeleteByInstanceId(Arrays.asList(instanceId), CmdbIpAddressPoolEntity.class);
                break;
            case IP_CONF_POOL:
                //存活ip采集-网络设备配置文件解析
                collectService.batchDeleteByInstanceId(Arrays.asList(instanceId), CmdbIpConfPoolEntity.class);
                break;
            case IP_ARP_POOL:
                //存活ip采集-arp扫描网段地址
                collectService.batchDeleteByInstanceId(Arrays.asList(instanceId), CmdbIpArpPoolEntity.class);
                break;
            case VIP_ADDRESS_POOL:
                // 虚拟ip采集
                vipConllectService.deleteByInstanceId(instanceId);
                break;
            case VMWARE_NETWORK_PORTGROUP:
                // 网段-端口组
                networkPortGroupService.deleteByInstanceId(instanceId);
                break;
            default:
                throw new RuntimeException("不支持的类型！");
        }
        return true;
    }


    /**
     * 新增存活ip-网络设备
     *
     * @param eventId
     * @param instanceId 实例id
     */
    private void createIpAddressPool(String eventId, String instanceId) {
        List<CmdbIpAddressPoolEntity> entityList = Lists.newArrayList();
        EasyOpsResult<CmdbIpAddressPoolDTO> result = VmwareInstanceQueryHelper.queryIpAddressPool(Arrays.asList(instanceId));
        List<CmdbIpAddressPoolDTO> dataList = result.getData().getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (CmdbIpAddressPoolDTO dto : dataList) {
            CmdbIpAddressPoolEntity entity = new CmdbIpAddressPoolEntity();
            VmwareInstanceQueryHelper.fillIpAddressPool(eventId, dto, entity);
            entityList.add(entity);
        }
        if (CollectionUtils.isNotEmpty(entityList)) {
            collectService.batchAdd(entityList, CmdbIpAddressPoolEntity.class,"1","","");
        }
    }

    /**
     * 新增存活ip-网络设备配置文件解析
     *
     * @param eventId
     * @param instanceId
     */
    private void createIpConfPool(String eventId, String instanceId) {
        List<CmdbIpConfPoolEntity> entityList = Lists.newArrayList();
        EasyOpsResult<CmdbIpConfPoolDTO> result = VmwareInstanceQueryHelper.queryIpConfPool(Arrays.asList(instanceId));
        List<CmdbIpConfPoolDTO> dataList = result.getData().getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (CmdbIpConfPoolDTO dto : dataList) {
            CmdbIpConfPoolEntity entity = new CmdbIpConfPoolEntity();
            VmwareInstanceQueryHelper.fillIpConfPool(eventId, dto, entity);
            entityList.add(entity);
        }
        if (CollectionUtils.isNotEmpty(entityList)) {
            collectService.batchAdd(entityList, CmdbIpConfPoolEntity.class,"1","","");
        }
    }

    /**
     * 新增存活ip-arp扫描网段地址
     *
     * @param eventId
     * @param instanceId
     */
    private void createIpArpPool(String eventId, String instanceId) {
        List<CmdbIpArpPoolEntity> entityList = Lists.newArrayList();
        EasyOpsResult<CmdbIpArpPoolDTO> result = VmwareInstanceQueryHelper.queryIpArpPool(Arrays.asList(instanceId));
        List<CmdbIpArpPoolDTO> dataList = result.getData().getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (CmdbIpArpPoolDTO dto : dataList) {
            CmdbIpArpPoolEntity entity = new CmdbIpArpPoolEntity();
            VmwareInstanceQueryHelper.fillIpArpPool(eventId, dto, entity);
            entityList.add(entity);
        }
        if (CollectionUtils.isNotEmpty(entityList)) {
            collectService.batchAdd(entityList, CmdbIpArpPoolEntity.class,"1","","");
        }
    }

    /**
     * 新增虚拟ip
     *
     * @param eventId
     * @param instanceId 实例id
     */
    private void createVipAddressPool(String eventId, String instanceId) {
        List<CmdbVipCollectEntity> entityList = Lists.newArrayList();
        EasyOpsResult<CmdbVipAddressPoolDTO> result = VmwareInstanceQueryHelper.queryVipAddressPool(Arrays.asList(instanceId));
        List<CmdbVipAddressPoolDTO> dataList = result.getData().getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (CmdbVipAddressPoolDTO dto : dataList) {
            CmdbVipCollectEntity entity = new CmdbVipCollectEntity();
            VmwareInstanceQueryHelper.fillVipAddressPool(eventId, dto, entity);
            entityList.add(entity);
        }
        if (CollectionUtils.isNotEmpty(entityList)) {
            vipConllectService.batchAdd(entityList);
        }
    }

    /**
     * 抽取json串中的更新字段和值.
     *
     * @param request 更新请求内容
     * @return 更新字段和value Pair
     */
    private Map<String, Object> extractUpdateField(InstanceUpdateRequest request) {
        InstanceRequestData<InstanceUpdateRequestExtInfo> data = request.getData();
        InstanceUpdateRequestExtInfo extInfo = data.getExtInfo();
        String instanceId = extInfo.getInstanceId();
        Map<String, Object> params = Maps.newHashMap();
        params.put("operator", data.getOperator());
        params.put("instanceId", instanceId);
        params.put("version", extInfo.getVersion());
        params.put("preTimestamp", extInfo.getPreTimestamp());
        params.put("timestamp", extInfo.getTimestamp());
        LinkedHashMap<String, Object> fieldValueHashMap = extInfo.getChangeData();
        for (Map.Entry<String, Object> entry : fieldValueHashMap.entrySet()) {
            Object value = entry.getValue();
            // hangfang 2020.07.30 硬编码加密密钥 key->version
            String version = entry.getKey();
            // TODO:修改为通用的，不写死.
            if (!Arrays.asList("network", "ip", "disk").contains(version)) {
                List<String> updateValueList = VmwareInstanceQueryHelper.array2List(value, String.class);
                String joinStr = "";
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(updateValueList)) {
                    joinStr = Joiner.on(",").join(updateValueList);
                }
                // 已经有version版本字段了
                if ("version".equals(version)) {
                    version = "os_version";
                }
                params.put(version, joinStr);
            }
        }
        return params;
    }

    /**
     * 保存请求日志
     *
     * @param request
     */
    private void saveLog(BaseInstanceRequest request) {
        executorService.execute(() -> instanceLogService.saveInstanceLog(request));
    }

}
