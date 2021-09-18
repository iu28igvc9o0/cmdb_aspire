package com.aspire.ums.cmdb.openstack.service.impl;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.automate.enums.AutomateResCodeEnum;
import com.aspire.ums.cmdb.automate.exception.AutomateException;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRelationCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRelationCreateRequestExtInfo;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRelationDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRelationDeleteRequestExtInfo;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRequestData;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceUpdateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceUpdateRequestExtInfo;
import com.aspire.ums.cmdb.ipCollect.utils.VmwareInstanceQueryHelper;
import com.aspire.ums.cmdb.openstack.enums.OpenStackEnums;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackAdminDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackAllocationPoolDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackImageDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackNetworkDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackResult;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackServerDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackSubnetDTO;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackAdmin;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackAllocationPool;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackImage;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackImageAdminRel;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackImageServerRel;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackNetwork;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackServer;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackSubnet;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackSubnetAdminRel;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackSubnetNetworkRel;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackAdminService;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackAllocationPoolService;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackImageAdminRelService;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackImageServerRelService;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackImageService;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackNetworkService;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackServerService;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackSubnetAdminRelService;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackSubnetNetworkRelService;
import com.aspire.ums.cmdb.openstack.service.ICmdbOpenstackSubnetService;
import com.aspire.ums.cmdb.openstack.service.IOpenstackInstanceService;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 同步云管vmware Instance业务逻辑实现.
 *
 * @author jiangxuwen
 * @date 2020/3/11 21:07
 */
@Service("openStackInstanceService")
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OpenStackInstanceServiceImpl implements IOpenstackInstanceService {

    private static String ALLOCATION_POOL_KEY = "allocationPools";

    @Autowired
    private ICmdbOpenstackSubnetService openstackSubnetService;

    @Autowired
    private ICmdbOpenstackServerService openstackServerService;

    @Autowired
    private ICmdbOpenstackNetworkService openstackNetworkService;

    @Autowired
    private ICmdbOpenstackAdminService openstackAdminService;

    @Autowired
    private ICmdbOpenstackImageService openstackImageService;

    @Autowired
    private ICmdbOpenstackAllocationPoolService allocationPoolService;

    @Autowired
    private ICmdbOpenstackSubnetAdminRelService subnetAdminRelService;

    @Autowired
    private ICmdbOpenstackSubnetNetworkRelService subnetNetworkRelService;

    @Autowired
    private ICmdbOpenstackImageAdminRelService imageAdminRelService;

    @Autowired
    private ICmdbOpenstackImageServerRelService imageServerRelService;

    @Override
    public void instanceCreate(InstanceCreateRequest createRequest) {
        String objectId = createRequest.getData().getExtInfo().getObjectId();
        String instanceId = createRequest.getData().getExtInfo().getInstanceId();
        String eventId = createRequest.getData().getEventId();
        String eventType = createRequest.getData().getEvent();
        log.info("[实例创建]处理中,参数:objectId=[{}],instanceId=[{}],eventId=[{}],eventType=[{}]", objectId, instanceId, eventId,
                eventType);
        OpenStackEnums moduleEnum = OpenStackEnums.fromKey(objectId);
        if (moduleEnum == null) {
            return;
        }
        // TODO:可以改为策略模式，避免太多if条件
        switch (moduleEnum) {
            case OPENSTACK_SUBNET:
                createOpenStackSubnet(eventId, instanceId);
                break;
            case OPENSTACK_IMAGE:
                createOpenStackImage(eventId, instanceId);
                break;
            case OPENSTACK_ADMIN:
                createOpenStackAdmin(eventId, instanceId);
                break;
            case OPENSTACK_NETWORK:
                createOpenStackNetwork(eventId, instanceId);
                break;
            case OPENSTACK_SERVERS:
                createOpenStackServer(eventId, instanceId);
                break;
            default:
                throw new AutomateException(AutomateResCodeEnum.INVALID_PARAM, "不支持的类型！");
        }
    }

    @Override
    public void instanceUpdate(InstanceUpdateRequest updateRequest) {
        InstanceUpdateRequestExtInfo extInfo = updateRequest.getData().getExtInfo();
        String objectId = extInfo.getObjectId();
        String instanceId = extInfo.getInstanceId();
        String eventType = updateRequest.getData().getEvent();
        String eventId = updateRequest.getData().getEventId();
        log.info("[实例修改]处理中,参数:objectId=[{}],instanceId=[{}],eventId=[{}],eventType=[{}]", objectId, instanceId, eventId,
                eventType);
        OpenStackEnums moduleEnum = OpenStackEnums.fromKey(objectId);
        if (moduleEnum == null) {
            return;
        }
        boolean ipFlag = false;
        List<OpenStackAllocationPoolDTO> allocationPoolDTOList = Lists.newArrayList();
        List<String> changeFieldList = extInfo.getChangeFields();
        if (CollectionUtils.isNotEmpty(changeFieldList)) {
            if (changeFieldList.contains(ALLOCATION_POOL_KEY)) {
                ipFlag = true;
            }
        }
        Map<String, Object> updateMap = extractUpdateField(updateRequest);
        // if (networkFlag) {
        // networkList = extractNetwork(updateRequest);
        // }
        // if (diskFlag) {
        // diskList = extractDisk(updateRequest);
        // }
        if (ipFlag) {
            allocationPoolDTOList = extractAllocationPool(updateRequest);
        }
        switch (moduleEnum) {
            case OPENSTACK_SUBNET:
                CmdbOpenstackSubnet query = new CmdbOpenstackSubnet();
                query.setInstanceId(instanceId);
                CmdbOpenstackSubnet openStackSubnet = openstackSubnetService.getByInstanceId(query);
                if (openStackSubnet == null) {
                    log.info("instanceId:[{}]对应的DataCenter不存在，新增!", instanceId);
                    createOpenStackSubnet(eventId, instanceId);
                } else {
                    if (ipFlag) {
                        deleteAllocation(instanceId);
                        saveAllcationPool(allocationPoolDTOList, instanceId);
                    }
                    openstackSubnetService.updateByInstanceId(updateMap);
                }
                break;
            case OPENSTACK_IMAGE:
                CmdbOpenstackImage query2 = new CmdbOpenstackImage();
                query2.setInstanceId(instanceId);
                CmdbOpenstackImage image = openstackImageService.getByInstanceId(query2);
                if (image == null) {
                    log.info("instanceId:[{}]对应的image不存在，新增!", instanceId);
                    createOpenStackImage(eventId, instanceId);
                } else {
                    openstackImageService.updateByInstanceId(updateMap);
                }
                break;
            case OPENSTACK_NETWORK:
                CmdbOpenstackNetwork network = openstackNetworkService.findByInstanceId(instanceId);
                if (network == null) {
                    log.info("instanceId:[{}]对应的network不存在，新增!", instanceId);
                    createOpenStackNetwork(eventId, instanceId);
                } else {
                    openstackNetworkService.updateByInstanceId(updateMap);
                }
                break;
            case OPENSTACK_SERVERS:
                CmdbOpenstackServer server = openstackServerService.findByInstanceId(instanceId);
                if (server == null) {
                    log.info("instanceId:[{}]对应的server不存在，新增!", instanceId);
                    createOpenStackServer(eventId, instanceId);
                } else {
                    openstackServerService.updateByInstanceId(updateMap);
                }
                break;
            case OPENSTACK_ADMIN:
                CmdbOpenstackAdmin admin = openstackAdminService.findByInstanceId(instanceId);
                if (admin == null) {
                    log.info("instanceId:[{}]对应的admin不存在，新增!", instanceId);
                    createOpenStackAdmin(eventId, instanceId);
                } else {
                    openstackAdminService.updateByInstanceId(updateMap);
                }
                break;
            default:
                throw new AutomateException(AutomateResCodeEnum.INVALID_PARAM, "不支持的类型！");
        }

    }

    @Override
    public void instanceDelete(InstanceDeleteRequest deleteRequest) {
        String objectId = deleteRequest.getData().getExtInfo().getObjectId();
        String instanceId = deleteRequest.getData().getExtInfo().getInstanceId();
        String eventType = deleteRequest.getData().getEvent();
        String eventId = deleteRequest.getData().getEventId();
        log.info("[实例删除]处理中,参数:objectId=[{}],instanceId=[{}],eventId=[{}],eventType=[{}]", objectId, instanceId, eventId,
                eventType);
        OpenStackEnums moduleEnum = OpenStackEnums.fromKey(objectId);
        switch (moduleEnum) {
            case OPENSTACK_SUBNET:
                openstackSubnetService.deleteByInstanceId(instanceId);
                break;
            case OPENSTACK_IMAGE:
                openstackImageService.deleteByInstanceId(instanceId);
                break;

        }
    }

    @Override
    public void instanceRelationCreate(InstanceRelationCreateRequest createRequest) {
        InstanceRelationCreateRequestExtInfo extInfo = createRequest.getData().getExtInfo();
        String dstObjectId = extInfo.getDstObjectId();
        String objectId = extInfo.getObjectId();
        String dstInstanceId = extInfo.getDstInstanceId();
        String instanceId = extInfo.getInstanceId();
        OpenStackEnums dstModel = OpenStackEnums.fromKey(dstObjectId);
        OpenStackEnums model = OpenStackEnums.fromKey(objectId);
        String eventType = createRequest.getData().getEvent();
        String eventId = createRequest.getData().getEventId();
        log.info("[创建实例关联关系]处理中,参数:dstObjectId=[{}],dstInstanceId=[{}],objectId=[{}],instanceId=[{}],eventId=[{}],eventType=[{}]",
                dstObjectId, dstInstanceId, objectId, instanceId, eventId, eventType);
        createOpenstackSubnetAdminRel(dstModel, model, dstInstanceId, instanceId, eventId);
        createOpenstackSubnetNetworkRel(dstModel, model, dstInstanceId, instanceId, eventId);
        createOpenstackImageAdminRel(dstModel, model, dstInstanceId, instanceId, eventId);
        createOpenstackImageServerRel(dstModel, model, dstInstanceId, instanceId, eventId);
    }

    @Override
    public void instanceRelationDelete(InstanceRelationDeleteRequest deleteRequest) {
        InstanceRelationDeleteRequestExtInfo extInfo = deleteRequest.getData().getExtInfo();
        String dstObjectId = extInfo.getDstObjectId();
        String objectId = extInfo.getObjectId();
        String dstInstanceId = extInfo.getDstInstanceId();
        String instanceId = extInfo.getInstanceId();
        OpenStackEnums dstModel = OpenStackEnums.fromKey(dstObjectId);
        OpenStackEnums model = OpenStackEnums.fromKey(objectId);
        String eventType = deleteRequest.getData().getEvent();
        String eventId = deleteRequest.getData().getEventId();
        log.info("[删除实例关联关系]处理中,参数:dstObjectId=[{}],dstInstanceId=[{}],objectId=[{}],instanceId=[{}],eventId=[{}],eventType=[{}]",
                dstObjectId, dstInstanceId, objectId, instanceId, eventId, eventType);
        delOpenStackSubnetAdminRel(dstModel, model, dstInstanceId, instanceId);
        delOpenStackSubnetNetworkRel(dstModel, model, dstInstanceId, instanceId);
        delOpenStackImageAdminRel(dstModel, model, dstInstanceId, instanceId);
        delOpenStackImageServerRel(dstModel, model, dstInstanceId, instanceId);
    }

    /**
     * 创建openStack子网.
     *
     * @param instanceId
     *            实例id
     * @return
     */
    private void createOpenStackSubnet(String eventId, String instanceId) {
        List<CmdbOpenstackSubnet> entityList = Lists.newArrayList();
        OpenStackResult<OpenStackSubnetDTO> result = VmwareInstanceQueryHelper.queryOpenStackSubnet(Arrays.asList(instanceId));
        List<OpenStackSubnetDTO> dataList = result.getData().getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (OpenStackSubnetDTO dto : dataList) {
            CmdbOpenstackSubnet entity = VmwareInstanceQueryHelper.fillOpenStackSubnet(eventId, dto);
            List<OpenStackAllocationPoolDTO> allocationPoolList = dto.getAllocationPoolList();
            saveAllcationPool(allocationPoolList, entity.getInstanceId());
            entityList.add(entity);
        }
        if (CollectionUtils.isNotEmpty(entityList)) {
            openstackSubnetService.batchInsert(entityList);
        }
    }

    private void saveAllcationPool(List<OpenStackAllocationPoolDTO> allocationPools, String subnetId) {
        if (CollectionUtils.isEmpty(allocationPools)) {
            return;
        }
        List<CmdbOpenstackAllocationPool> allcationPoolEntityList = Lists.newArrayList();
        for (OpenStackAllocationPoolDTO dto : allocationPools) {
            CmdbOpenstackAllocationPool entity = new CmdbOpenstackAllocationPool();
            entity.setId(UUIDUtil.getUUID());
            entity.setSubnetId(subnetId);
            entity.setStart(dto.getStart());
            entity.setEnd(dto.getEnd());
            allcationPoolEntityList.add(entity);
        }
        if (CollectionUtils.isNotEmpty(allcationPoolEntityList)) {
            allocationPoolService.batchInsert(allcationPoolEntityList);
        }
    }

    private void deleteAllocation(String subnetId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(subnetId), "subnetId cannot be null!");
        CmdbOpenstackAllocationPool entity = new CmdbOpenstackAllocationPool();
        entity.setSubnetId(subnetId);
        allocationPoolService.deleteBySubnetId(entity);
    }

    /**
     * 更新实例,抽取更新字段中的网络信息(数组).
     *
     * @param request
     *            实例更新请求
     * @return
     */
    private List<OpenStackAllocationPoolDTO> extractAllocationPool(InstanceUpdateRequest request) {
        List<OpenStackAllocationPoolDTO> allocationPoolList = Lists.newArrayList();
        InstanceRequestData<InstanceUpdateRequestExtInfo> data = request.getData();
        InstanceUpdateRequestExtInfo extInfo = data.getExtInfo();
        LinkedHashMap<String, Object> linkedHashMap = extInfo.getChangeData();
        for (Map.Entry<String, Object> entry : linkedHashMap.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (ALLOCATION_POOL_KEY.equals(key)) {
                List<OpenStackAllocationPoolDTO> dtoList = VmwareInstanceQueryHelper.array2List(value,
                        OpenStackAllocationPoolDTO.class);
                if (CollectionUtils.isNotEmpty(dtoList)) {
                    allocationPoolList.addAll(dtoList);
                }
            }
        }
        return allocationPoolList;
    }

    private void createOpenStackAdmin(String eventId, String instanceId) {
        List<CmdbOpenstackAdmin> entityList = Lists.newArrayList();
        OpenStackResult<OpenStackAdminDTO> result = VmwareInstanceQueryHelper.queryOpenStackAdmin(Arrays.asList(instanceId));
        List<OpenStackAdminDTO> dataList = result.getData().getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (OpenStackAdminDTO dto : dataList) {
            CmdbOpenstackAdmin entity = VmwareInstanceQueryHelper.fillOpenStackAdmin(eventId, dto);
            entityList.add(entity);
        }
        if (CollectionUtils.isNotEmpty(entityList)) {
            openstackAdminService.batchInsert(entityList);
        }
    }

    private void createOpenStackServer(String eventId, String instanceId) {
        List<CmdbOpenstackServer> entityList = Lists.newArrayList();
        OpenStackResult<OpenStackServerDTO> result = VmwareInstanceQueryHelper.queryOpenStackServer(Arrays.asList(instanceId));
        List<OpenStackServerDTO> dataList = result.getData().getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (OpenStackServerDTO dto : dataList) {
            CmdbOpenstackServer entity = VmwareInstanceQueryHelper.fillOpenStackServer(eventId, dto);
            entityList.add(entity);
        }
        if (CollectionUtils.isNotEmpty(entityList)) {
            openstackServerService.batchInsert(entityList);
        }
    }

    private void createOpenStackNetwork(String eventId, String instanceId) {
        List<CmdbOpenstackNetwork> entityList = Lists.newArrayList();
        OpenStackResult<OpenStackNetworkDTO> result = VmwareInstanceQueryHelper.queryOpenStackNetwork(Arrays.asList(instanceId));
        List<OpenStackNetworkDTO> dataList = result.getData().getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (OpenStackNetworkDTO dto : dataList) {
            CmdbOpenstackNetwork entity = VmwareInstanceQueryHelper.fillOpenStackNetwork(eventId, dto);
            entityList.add(entity);
        }
        if (CollectionUtils.isNotEmpty(entityList)) {
            openstackNetworkService.batchInsert(entityList);
        }
    }

    /**
     * 创建openStack Image.
     *
     * @param instanceId
     *            实例id
     * @return
     */
    private void createOpenStackImage(String eventId, String instanceId) {
        List<CmdbOpenstackImage> entityList = Lists.newArrayList();
        OpenStackResult<OpenStackImageDTO> result = VmwareInstanceQueryHelper.queryOpenStackImage(Arrays.asList(instanceId));
        List<OpenStackImageDTO> dataList = result.getData().getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (OpenStackImageDTO dto : dataList) {
            CmdbOpenstackImage entity = VmwareInstanceQueryHelper.fillOpenStackImage(eventId, dto);
            entityList.add(entity);
        }
        if (CollectionUtils.isNotEmpty(entityList)) {
            openstackImageService.batchInsert(entityList);
        }
    }

    /**
     * 抽取json串中的更新字段和值.
     *
     * @param request
     *            更新请求内容
     * @return 更新字段和value Pair
     */
    private Map<String, Object> extractUpdateField(InstanceUpdateRequest request) {
        InstanceRequestData<InstanceUpdateRequestExtInfo> data = request.getData();
        InstanceUpdateRequestExtInfo extInfo = data.getExtInfo();
        String instanceId = extInfo.getInstanceId();
        Map<String, Object> params = Maps.newHashMap();
        params.put("instanceId", instanceId);
        params.put("version", extInfo.getVersion());
        params.put("preTimestamp", extInfo.getPreTimestamp());
        params.put("timestamp", extInfo.getTimestamp());
        LinkedHashMap<String, Object> fieldValueHashMap = extInfo.getChangeData();
        for (Map.Entry<String, Object> entry : fieldValueHashMap.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            // TODO:修改为通用的，不写死.
            if (!Arrays.asList(ALLOCATION_POOL_KEY).contains(key)) {
                List<String> updateValueList = VmwareInstanceQueryHelper.array2List(value, String.class);
                String joinStr = "";
                if (CollectionUtils.isNotEmpty(updateValueList)) {
                    joinStr = Joiner.on(",").join(updateValueList);
                }
                // 已经有version版本字段了
                if ("version".equals(key)) {
                    key = "os_version";
                }
                params.put(key, joinStr);
            }
        }
        return params;
    }

    private void createOpenstackSubnetAdminRel(OpenStackEnums dstModel, OpenStackEnums model, String dstInstanceId,
            String instanceId, String eventId) {
        // 数据中心-集群 1:N
        if (OpenStackEnums.OPENSTACK_SUBNET == dstModel && OpenStackEnums.OPENSTACK_ADMIN == model) {
            CmdbOpenstackSubnet subnet = openstackSubnetService.findByInstanceId(dstInstanceId);
            CmdbOpenstackAdmin admin = openstackAdminService.findByInstanceId(instanceId);
            if (subnet == null) {
                log.info("dstInstanceId:[{}]对应的CmdbOpenstackSubnet不存在，新增!", dstInstanceId);
                createOpenStackSubnet(eventId, dstInstanceId);
            }
            if (admin == null) {
                log.info("instanceId:[{}]对应的CmdbOpenstackAdmin不存在，新增!", instanceId);
                createOpenStackAdmin(eventId, instanceId);
            }

            CmdbOpenstackSubnetAdminRel entity = new CmdbOpenstackSubnetAdminRel();
            entity.setId(UUIDUtil.getUUID());
            entity.setSubnetId(dstInstanceId);
            entity.setAdminId(instanceId);
            subnetAdminRelService.insert(entity);
        }
        if (OpenStackEnums.OPENSTACK_SUBNET == model && OpenStackEnums.OPENSTACK_ADMIN == dstModel) {
            CmdbOpenstackSubnet subnet = openstackSubnetService.findByInstanceId(instanceId);
            CmdbOpenstackAdmin admin = openstackAdminService.findByInstanceId(dstInstanceId);
            if (subnet == null) {
                log.info("instanceId:[{}]对应的CmdbOpenstackSubnet不存在，新增!", instanceId);
                createOpenStackSubnet(eventId, instanceId);
            }
            if (admin == null) {
                log.info("dstInstanceId:[{}]对应的CmdbOpenstackAdmin不存在，新增!", dstInstanceId);
                createOpenStackAdmin(eventId, dstInstanceId);
            }
            CmdbOpenstackSubnetAdminRel entity = new CmdbOpenstackSubnetAdminRel();
            entity.setId(UUIDUtil.getUUID());
            entity.setSubnetId(instanceId);
            entity.setAdminId(dstInstanceId);
            subnetAdminRelService.insert(entity);
        }
    }

    private void createOpenstackSubnetNetworkRel(OpenStackEnums dstModel, OpenStackEnums model, String dstInstanceId,
            String instanceId, String eventId) {
        // 数据中心-集群 1:N
        if (OpenStackEnums.OPENSTACK_SUBNET == dstModel && OpenStackEnums.OPENSTACK_NETWORK == model) {
            CmdbOpenstackSubnet subnet = openstackSubnetService.findByInstanceId(dstInstanceId);
            CmdbOpenstackNetwork network = openstackNetworkService.findByInstanceId(instanceId);
            if (subnet == null) {
                log.info("dstInstanceId:[{}]对应的CmdbOpenstackSubnet不存在，新增!", dstInstanceId);
                createOpenStackSubnet(eventId, dstInstanceId);
            }
            if (network == null) {
                log.info("instanceId:[{}]对应的CmdbOpenstackNetwork不存在，新增!", instanceId);
                createOpenStackNetwork(eventId, instanceId);
            }

            CmdbOpenstackSubnetNetworkRel entity = new CmdbOpenstackSubnetNetworkRel();
            entity.setId(UUIDUtil.getUUID());
            entity.setSubnetId(dstInstanceId);
            entity.setNetworkId(instanceId);
            subnetNetworkRelService.insert(entity);
        }
        if (OpenStackEnums.OPENSTACK_SUBNET == model && OpenStackEnums.OPENSTACK_NETWORK == dstModel) {
            CmdbOpenstackSubnet subnet = openstackSubnetService.findByInstanceId(instanceId);
            CmdbOpenstackNetwork network = openstackNetworkService.findByInstanceId(dstInstanceId);
            if (subnet == null) {
                log.info("instanceId:[{}]对应的CmdbOpenstackSubnet不存在，新增!", instanceId);
                createOpenStackSubnet(eventId, instanceId);
            }
            if (network == null) {
                log.info("instanceId:[{}]对应的CmdbOpenstackNetwork不存在，新增!", instanceId);
                createOpenStackNetwork(eventId, instanceId);
            }
            CmdbOpenstackSubnetNetworkRel entity = new CmdbOpenstackSubnetNetworkRel();
            entity.setId(UUIDUtil.getUUID());
            entity.setSubnetId(instanceId);
            entity.setNetworkId(dstInstanceId);
            subnetNetworkRelService.insert(entity);
        }
    }

    private void createOpenstackImageAdminRel(OpenStackEnums dstModel, OpenStackEnums model, String dstInstanceId,
            String instanceId, String eventId) {
        // 数据中心-集群 1:N
        if (OpenStackEnums.OPENSTACK_IMAGE == dstModel && OpenStackEnums.OPENSTACK_ADMIN == model) {
            CmdbOpenstackImage image = openstackImageService.findByInstanceId(dstInstanceId);
            CmdbOpenstackAdmin admin = openstackAdminService.findByInstanceId(instanceId);
            if (image == null) {
                log.info("dstInstanceId:[{}]对应的CmdbOpenstackImage不存在，新增!", dstInstanceId);
                createOpenStackImage(eventId, dstInstanceId);
            }
            if (admin == null) {
                log.info("instanceId:[{}]对应的CmdbOpenstackAdmin不存在，新增!", instanceId);
                createOpenStackAdmin(eventId, instanceId);
            }

            CmdbOpenstackImageAdminRel entity = new CmdbOpenstackImageAdminRel();
            entity.setId(UUIDUtil.getUUID());
            entity.setImageId(dstInstanceId);
            entity.setAdminId(instanceId);
            imageAdminRelService.insert(entity);
        }
        if (OpenStackEnums.OPENSTACK_IMAGE == model && OpenStackEnums.OPENSTACK_ADMIN == dstModel) {
            CmdbOpenstackImage image = openstackImageService.findByInstanceId(instanceId);
            CmdbOpenstackAdmin admin = openstackAdminService.findByInstanceId(dstInstanceId);
            if (image == null) {
                log.info("dstInstanceId:[{}]对应的CmdbOpenstackImage不存在，新增!", dstInstanceId);
                createOpenStackImage(eventId, dstInstanceId);
            }
            if (admin == null) {
                log.info("dstInstanceId:[{}]对应的CmdbOpenstackAdmin不存在，新增!", dstInstanceId);
                createOpenStackAdmin(eventId, dstInstanceId);
            }
            CmdbOpenstackImageAdminRel entity = new CmdbOpenstackImageAdminRel();
            entity.setId(UUIDUtil.getUUID());
            entity.setImageId(instanceId);
            entity.setAdminId(dstInstanceId);
            imageAdminRelService.insert(entity);
        }
    }

    private void createOpenstackImageServerRel(OpenStackEnums dstModel, OpenStackEnums model, String dstInstanceId,
            String instanceId, String eventId) {
        // 数据中心-集群 1:N
        if (OpenStackEnums.OPENSTACK_IMAGE == dstModel && OpenStackEnums.OPENSTACK_SERVERS == model) {
            CmdbOpenstackImage image = openstackImageService.findByInstanceId(dstInstanceId);
            CmdbOpenstackServer server = openstackServerService.findByInstanceId(instanceId);
            if (image == null) {
                log.info("dstInstanceId:[{}]对应的CmdbOpenstackImage不存在，新增!", dstInstanceId);
                createOpenStackImage(eventId, dstInstanceId);
            }
            if (server == null) {
                log.info("instanceId:[{}]对应的CmdbOpenstackServer不存在，新增!", instanceId);
                createOpenStackServer(eventId, instanceId);
            }

            CmdbOpenstackImageServerRel entity = new CmdbOpenstackImageServerRel();
            entity.setId(UUIDUtil.getUUID());
            entity.setImageId(dstInstanceId);
            entity.setServerId(instanceId);
            imageServerRelService.insert(entity);
        }
        if (OpenStackEnums.OPENSTACK_IMAGE == model && OpenStackEnums.OPENSTACK_SERVERS == dstModel) {
            CmdbOpenstackImage image = openstackImageService.findByInstanceId(instanceId);
            CmdbOpenstackServer server = openstackServerService.findByInstanceId(dstInstanceId);
            if (image == null) {
                log.info("dstInstanceId:[{}]对应的CmdbOpenstackImage不存在，新增!", dstInstanceId);
                createOpenStackImage(eventId, dstInstanceId);
            }
            if (server == null) {
                log.info("instanceId:[{}]对应的CmdbOpenstackServer不存在，新增!", instanceId);
                createOpenStackServer(eventId, instanceId);
            }
            CmdbOpenstackImageServerRel entity = new CmdbOpenstackImageServerRel();
            entity.setId(UUIDUtil.getUUID());
            entity.setImageId(instanceId);
            entity.setServerId(dstInstanceId);
            imageServerRelService.insert(entity);
        }
    }

    private void delOpenStackSubnetAdminRel(OpenStackEnums dstModel, OpenStackEnums model, String dstInstanceId,
            String instanceId) {
        if (OpenStackEnums.OPENSTACK_SUBNET == dstModel && OpenStackEnums.OPENSTACK_ADMIN == model) {
            CmdbOpenstackSubnetAdminRel entity = new CmdbOpenstackSubnetAdminRel();
            entity.setSubnetId(dstInstanceId);
            entity.setAdminId(instanceId);
            subnetAdminRelService.deleteByInstanceId(entity);
        }
        if (OpenStackEnums.OPENSTACK_SUBNET == model && OpenStackEnums.OPENSTACK_ADMIN == dstModel) {
            CmdbOpenstackSubnetAdminRel entity = new CmdbOpenstackSubnetAdminRel();
            entity.setSubnetId(instanceId);
            entity.setAdminId(dstInstanceId);
            subnetAdminRelService.deleteByInstanceId(entity);
        }
    }

    private void delOpenStackSubnetNetworkRel(OpenStackEnums dstModel, OpenStackEnums model, String dstInstanceId,
            String instanceId) {
        if (OpenStackEnums.OPENSTACK_SUBNET == dstModel && OpenStackEnums.OPENSTACK_NETWORK == model) {
            CmdbOpenstackSubnetNetworkRel entity = new CmdbOpenstackSubnetNetworkRel();
            entity.setSubnetId(dstInstanceId);
            entity.setNetworkId(instanceId);
            subnetNetworkRelService.deleteByInstanceId(entity);
        }
        if (OpenStackEnums.OPENSTACK_SUBNET == model && OpenStackEnums.OPENSTACK_NETWORK == dstModel) {
            CmdbOpenstackSubnetNetworkRel entity = new CmdbOpenstackSubnetNetworkRel();
            entity.setSubnetId(instanceId);
            entity.setNetworkId(dstInstanceId);
            subnetNetworkRelService.deleteByInstanceId(entity);
        }
    }

    private void delOpenStackImageAdminRel(OpenStackEnums dstModel, OpenStackEnums model, String dstInstanceId, String instanceId) {
        if (OpenStackEnums.OPENSTACK_IMAGE == dstModel && OpenStackEnums.OPENSTACK_ADMIN == model) {
            CmdbOpenstackImageAdminRel entity = new CmdbOpenstackImageAdminRel();
            entity.setImageId(dstInstanceId);
            entity.setAdminId(instanceId);
            imageAdminRelService.deleteByInstanceId(entity);
        }
        if (OpenStackEnums.OPENSTACK_IMAGE == model && OpenStackEnums.OPENSTACK_ADMIN == dstModel) {
            CmdbOpenstackImageAdminRel entity = new CmdbOpenstackImageAdminRel();
            entity.setImageId(instanceId);
            entity.setAdminId(dstInstanceId);
            imageAdminRelService.deleteByInstanceId(entity);
        }
    }

    private void delOpenStackImageServerRel(OpenStackEnums dstModel, OpenStackEnums model, String dstInstanceId,
            String instanceId) {
        if (OpenStackEnums.OPENSTACK_IMAGE == dstModel && OpenStackEnums.OPENSTACK_SERVERS == model) {
            CmdbOpenstackImageServerRel entity = new CmdbOpenstackImageServerRel();
            entity.setImageId(dstInstanceId);
            entity.setServerId(instanceId);
            imageServerRelService.deleteByInstanceId(entity);
        }
        if (OpenStackEnums.OPENSTACK_IMAGE == model && OpenStackEnums.OPENSTACK_SERVERS == dstModel) {
            CmdbOpenstackImageServerRel entity = new CmdbOpenstackImageServerRel();
            entity.setImageId(instanceId);
            entity.setServerId(dstInstanceId);
            imageServerRelService.deleteByInstanceId(entity);
        }
    }
}
