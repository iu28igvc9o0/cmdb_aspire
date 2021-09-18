package com.aspire.ums.cmdb.ipCollect.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.aspire.ums.cmdb.ipCollect.payload.entity.*;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.*;
import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.automate.exception.AutomateException;
import com.aspire.ums.cmdb.ipCollect.enums.EventInstanceModuleEnum;
import com.aspire.ums.cmdb.ipCollect.exception.CollectException;
import com.aspire.ums.cmdb.openstack.enums.OpenStackEnums;
import com.aspire.ums.cmdb.openstack.payload.dto.BaseOpenStackDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackAdminDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackImageDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackNetworkDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackResult;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackServerDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackSubnetDTO;
import com.aspire.ums.cmdb.openstack.payload.entity.BaseOpenStackEntity;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackAdmin;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackImage;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackNetwork;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackServer;
import com.aspire.ums.cmdb.openstack.payload.entity.CmdbOpenstackSubnet;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.util.JsonUtil;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * 查询云管接口.
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 15:42
 */
@Slf4j
public class VmwareInstanceQueryHelper {

    private static final MapperFactory subnetMapperFactory = new DefaultMapperFactory.Builder().build();

    private static final MapperFactory adminMapperFactory = new DefaultMapperFactory.Builder().build();

    private static final MapperFactory serverMapperFactory = new DefaultMapperFactory.Builder().build();

    private static final MapperFactory networkMapperFactory = new DefaultMapperFactory.Builder().build();

    private static final MapperFactory imageMapperFactory = new DefaultMapperFactory.Builder().build();

    static {
        subnetMapperFactory.classMap(CmdbOpenstackSubnet.class, OpenStackSubnetDTO.class).field("subnetId", "id")
                .field("instanceId", "instanceId").field("name", "name").field("srcCreateTime", "creatTime")
                .field("srcModifyTime", "modifyTime").field("srcCreator", "creator").field("srcModifier", "modifier")
                .field("hname", "hname").field("ipVersion", "ipVersion").field("cidr", "cidr").field("org", "org")
                .field("sname", "sname").field("dhcpEnable", "dhcpEnable").field("gatewayIp", "gatewayIp").byDefault().register();

        imageMapperFactory.classMap(CmdbOpenstackImage.class, OpenStackImageDTO.class).field("imageId", "id")
                .field("instanceId", "instanceId").field("name", "name").field("srcCreateTime", "creatTime")
                .field("srcModifyTime", "modifyTime").field("srcCreator", "creator").field("srcModifier", "modifier")
                .field("hname", "hname").field("sname", "sname").byDefault().register();

        adminMapperFactory.classMap(CmdbOpenstackAdmin.class, OpenStackAdminDTO.class).field("srcCreateTime", "creatTime")
                .field("srcCreator", "creator").field("srcModifyTime", "modifyTime").field("orderIpController", "orderIpController")
                .byDefault().register();

        serverMapperFactory.classMap(CmdbOpenstackServer.class, OpenStackServerDTO.class).field("serverId", "id")
                .field("instanceId", "instanceId").field("name", "name").field("srcCreateTime", "creatTime")
                .field("srcModifyTime", "modifyTime").field("srcCreator", "creator").field("srcModifier", "modifier")
                .field("hname", "hname").field("launchedAt", "launchedAt").field("powerState", "powerState").field("org", "org")
                .field("sname", "sname").field("rootDeviceName", "rootDeviceName").field("status", "status").byDefault().register();

        networkMapperFactory.classMap(CmdbOpenstackNetwork.class, OpenStackNetworkDTO.class).field("networkId", "id")
                .field("instanceId", "instanceId").field("name", "name").field("srcCreateTime", "creatTime")
                .field("srcModifyTime", "modifyTime").field("srcCreator", "creator").field("srcModifier", "modifier").byDefault()
                .register();

    }

    /**
     * 根据instanceId查询存活ip-网络设备.
     *
     * @param instanceIdList
     * @return
     * @throws CollectException
     */
    public static EasyOpsResult<CmdbIpAddressPoolDTO> queryIpAddressPool(List<String> instanceIdList) throws CollectException {
        try {
            String response = getInstanceResponse(EventInstanceModuleEnum.IP_ADDRESS_POOL.getKey(), instanceIdList);
            return JsonUtil.jacksonConvert(response, new TypeReference<EasyOpsResult<CmdbIpAddressPoolDTO>>() {});
        } catch (Exception e) {
            log.error("", e);
            throw new CollectException(e);
        }
    }

    /**
     * 根据instanceId查询存活ip-网络设备配置文件解析.
     *
     * @param instanceIdList
     * @return
     * @throws CollectException
     */
    public static EasyOpsResult<CmdbIpConfPoolDTO> queryIpConfPool(List<String> instanceIdList) throws CollectException {
        try {
            String response = getInstanceResponse(EventInstanceModuleEnum.IP_CONF_POOL.getKey(), instanceIdList);
            return JsonUtil.jacksonConvert(response, new TypeReference<EasyOpsResult<CmdbIpConfPoolDTO>>() {});
        } catch (Exception e) {
            log.error("", e);
            throw new CollectException(e);
        }
    }

    /**
     * 根据instanceId查询存活ip-arp扫描网段地址.
     *
     * @param instanceIdList
     * @return
     * @throws CollectException
     */
    public static EasyOpsResult<CmdbIpArpPoolDTO> queryIpArpPool(List<String> instanceIdList) throws CollectException {
        try {
            String response = getInstanceResponse(EventInstanceModuleEnum.IP_ARP_POOL.getKey(), instanceIdList);
            return JsonUtil.jacksonConvert(response, new TypeReference<EasyOpsResult<CmdbIpArpPoolDTO>>() {});
        } catch (Exception e) {
            log.error("", e);
            throw new CollectException(e);
        }
    }

    /**
     * 根据instanceId查询虚拟ip.
     *
     * @param instanceIdList
     * @return
     * @throws CollectException
     */
    public static EasyOpsResult<CmdbVipAddressPoolDTO> queryVipAddressPool(List<String> instanceIdList) throws CollectException {
        try {
            String response = getInstanceResponse(EventInstanceModuleEnum.IP_ADDRESS_POOL.getKey(), instanceIdList);
            return JsonUtil.jacksonConvert(response, new TypeReference<EasyOpsResult<CmdbVipAddressPoolDTO>>() {});
        } catch (Exception e) {
            log.error("", e);
            throw new CollectException(e);
        }
    }

    /**
     * 根据instanceId查询.
     *
     * @param
     * @return
     */
    public static OpenStackResult<OpenStackSubnetDTO> queryOpenStackSubnet(List<String> instanceIdList) throws AutomateException {
        try {
            String response = getInstanceResponse(OpenStackEnums.OPENSTACK_SUBNET.getKey(), instanceIdList);
            return new JsonMapper().readValue(response, new TypeReference<OpenStackResult<OpenStackSubnetDTO>>() {});
        } catch (Exception e) {
            log.error("获取自动化的openstackSubnet失败.", e);
            throw new AutomateException(e);
        }
    }

    /**
     * 根据instanceId查询.
     *
     * @param
     * @return
     */
    public static OpenStackResult<OpenStackImageDTO> queryOpenStackImage(List<String> instanceIdList) throws AutomateException {
        try {
            String response = getInstanceResponse(OpenStackEnums.OPENSTACK_IMAGE.getKey(), instanceIdList);
            return new JsonMapper().readValue(response, new TypeReference<OpenStackResult<OpenStackImageDTO>>() {});
        } catch (Exception e) {
            log.error("获取自动化的openstackImage失败.", e);
            throw new AutomateException(e);
        }
    }

    /**
     * 根据instanceId查询.
     *
     * @param
     * @return
     */
    public static OpenStackResult<OpenStackAdminDTO> queryOpenStackAdmin(List<String> instanceIdList) throws AutomateException {
        try {
            String response = getInstanceResponse(OpenStackEnums.OPENSTACK_ADMIN.getKey(), instanceIdList);
            return new JsonMapper().readValue(response, new TypeReference<OpenStackResult<OpenStackAdminDTO>>() {});
        } catch (Exception e) {
            log.error("获取自动化的openstackAdmin失败.", e);
            throw new AutomateException(e);
        }
    }

    /**
     * 根据instanceId查询.
     *
     * @param
     * @return
     */
    public static OpenStackResult<OpenStackServerDTO> queryOpenStackServer(List<String> instanceIdList) throws AutomateException {
        try {
            String response = getInstanceResponse(OpenStackEnums.OPENSTACK_ADMIN.getKey(), instanceIdList);
            return new JsonMapper().readValue(response, new TypeReference<OpenStackResult<OpenStackServerDTO>>() {});
        } catch (Exception e) {
            log.error("获取自动化的openstackServer失败.", e);
            throw new AutomateException(e);
        }
    }

    /**
     * 根据instanceId查询.
     *
     * @param
     * @return
     */
    public static OpenStackResult<OpenStackNetworkDTO> queryOpenStackNetwork(List<String> instanceIdList) throws AutomateException {
        try {
            String response = getInstanceResponse(OpenStackEnums.OPENSTACK_ADMIN.getKey(), instanceIdList);
            return new JsonMapper().readValue(response, new TypeReference<OpenStackResult<OpenStackNetworkDTO>>() {});
        } catch (Exception e) {
            log.error("获取自动化的openstackNetwork失败.", e);
            throw new AutomateException(e);
        }
    }

    /**
     * 查询网段-端口数据
     * @param instanceIdList
     * @return
     */
    public static EasyOpsResult<VmwareNetworkPortGroupDto> queryNetPortGroup(List<String> instanceIdList) throws AutomateException {
        try {
            String response = getInstanceResponse(EventInstanceModuleEnum.VMWARE_NETWORK_PORTGROUP.getKey(), instanceIdList);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            return new JsonMapper().readValue(response, new TypeReference<EasyOpsResult<VmwareNetworkPortGroupDto>>() {});
        } catch (Exception e) {
            log.error("获取自动化的VMWARE_NETWORK_PORTGROUP模型失败", e);
            throw new AutomateException(e);
        }
    }

    public static EasyOpsResult<VmwareNetworkPortGroupDto> queryNetPortGroup(List<String> instanceIdList, Integer pageNo,
                                                                             Integer pageSize) throws AutomateException {
        try {
            String response = getInstanceResponse(EventInstanceModuleEnum.VMWARE_NETWORK_PORTGROUP.getKey(), instanceIdList, pageNo,
                    pageSize);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            return new JsonMapper().readValue(response, new TypeReference<EasyOpsResult<VmwareNetworkPortGroupDto>>() {});
        } catch (Exception e) {
            log.error("获取自动化的VMWARE_NETWORK_PORTGROUP模型失败", e);
            throw new AutomateException(e);
        }
    }

    /**
     * 查询instanceId数据.
     *
     * @param moduleId
     *            模型ID
     * @param instanceIdList
     *            实例id列表
     * @return 接口响应数据
     */
    private static String getInstanceResponse(String moduleId, List<String> instanceIdList) {
        Map<String, String> data2 = new TreeMap<>();
        EasyOpsAPI easyOpsAPI = new EasyOpsAPI();
        return easyOpsAPI.doApi(String.format("/cmdb_resource/object/%s/instance/_search", moduleId), data2,
                buildRequestBody(instanceIdList), "POST");
    }

    private static String getInstanceResponse(String moduleId, List<String> instanceIdList, Integer pageNo, Integer pageSize) {
        Map<String, String> data2 = new TreeMap<>();
        EasyOpsAPI easyOpsAPI = new EasyOpsAPI();
        return easyOpsAPI.doApi(String.format("/cmdb_resource/object/%s/instance/_search", moduleId), data2,
                buildRequestBody(instanceIdList, pageNo, pageSize), "POST");
    }

    private static String buildRequestBody(List<String> instanceIdList) {
        return buildRequestBody(instanceIdList, 1, 200);
    }

    private static String buildRequestBody(List<String> instanceIdList, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 200 : pageSize;
        String instanceIdStr = JSONArray.toJSONString(instanceIdList);
        String requestBody = "{\"page\":" + pageNo + ",\"page_size\":" + pageSize + ",\"query\":{\"instanceId\":{\"$in\":"
                + instanceIdStr + "}}}";
        return requestBody;
    }

    /**
     * 填充存活ip-网络设备
     *
     * @param eventId
     * @param dto
     * @param entity
     */
    public static void fillIpAddressPool(String eventId, CmdbIpAddressPoolDTO dto, CmdbIpAddressPoolEntity entity) {
        if (entity == null) {
            entity = new CmdbIpAddressPoolEntity();
        }
        entity.setIp(dto.getIp());
        entity.setIptype(dto.getIptype());
        entity.setMac(dto.getMac());
        entity.setGateway(dto.getGateway());
        fillBaseField(eventId, dto, entity);
    }

    /**
     * 填充存活ip-网络设备配置文件解析
     *
     * @param eventId
     * @param dto
     * @param entity
     */
    public static void fillIpConfPool(String eventId, CmdbIpConfPoolDTO dto, CmdbIpConfPoolEntity entity) {
        if (entity == null) {
            entity = new CmdbIpConfPoolEntity();
        }
        entity.setIp(dto.getIp());
        entity.setIptype(dto.getIptype());
        entity.setMac(dto.getMac());
        entity.setSrcip(dto.getSrcip());
        fillBaseField(eventId, dto, entity);
    }

    /**
     * 填充存活ip-arp扫描网段地址
     *
     * @param eventId
     * @param dto
     * @param entity
     */
    public static void fillIpArpPool(String eventId, CmdbIpArpPoolDTO dto, CmdbIpArpPoolEntity entity) {
        if (entity == null) {
            entity = new CmdbIpArpPoolEntity();
        }
        entity.setIp(dto.getIp());
        entity.setIptype(dto.getIptype());
        entity.setMac(dto.getMac());
        entity.setSrcip(dto.getSrcip());
        entity.setCpu(dto.getCpu());
        entity.setMem(dto.getMem());
        fillBaseField(eventId, dto, entity);
    }

    /**
     * 填充虚拟ip
     *
     * @param eventId
     * @param dto
     * @param entity
     */
    public static void fillVipAddressPool(String eventId, CmdbVipAddressPoolDTO dto, CmdbVipCollectEntity entity) {
        if (entity == null) {
            entity = new CmdbVipCollectEntity();
        }
        entity.setVip(dto.getVip());
        entity.setUsetype(dto.getUsetype());
        entity.setBindip(dto.getBindip());
        entity.setIplist(dto.getIplist());
        fillBaseField(eventId, dto, entity);
    }

    /**
     * 填充subnet
     *
     * @param eventId
     * @param dto
     * @param entity
     */
    public static CmdbOpenstackSubnet fillOpenStackSubnet(String eventId, OpenStackSubnetDTO dto) {
        BoundMapperFacade<CmdbOpenstackSubnet, OpenStackSubnetDTO> mapper = subnetMapperFactory
                .getMapperFacade(CmdbOpenstackSubnet.class, OpenStackSubnetDTO.class);
        CmdbOpenstackSubnet entity = mapper.mapReverse(dto);
        entity.setId(StringUtils.isEmpty(entity.getId()) ? UUIDUtil.getUUID() : entity.getId());
        entity.setEventId(eventId);
        return entity;
    }

    /**
     * 填充admin
     *
     * @param eventId
     * @param dto
     * @param entity
     */
    public static CmdbOpenstackAdmin fillOpenStackAdmin(String eventId, OpenStackAdminDTO dto) {
        BoundMapperFacade<CmdbOpenstackAdmin, OpenStackAdminDTO> mapper = adminMapperFactory
                .getMapperFacade(CmdbOpenstackAdmin.class, OpenStackAdminDTO.class);
        CmdbOpenstackAdmin entity = mapper.mapReverse(dto);
        entity.setId(StringUtils.isEmpty(entity.getId()) ? UUIDUtil.getUUID() : entity.getId());
        entity.setEventId(eventId);
        return entity;
    }

    /**
     * 填充admin
     *
     * @param eventId
     * @param dto
     * @param entity
     */
    public static CmdbOpenstackServer fillOpenStackServer(String eventId, OpenStackServerDTO dto) {
        BoundMapperFacade<CmdbOpenstackServer, OpenStackServerDTO> mapper = adminMapperFactory
                .getMapperFacade(CmdbOpenstackServer.class, OpenStackServerDTO.class);
        CmdbOpenstackServer entity = mapper.mapReverse(dto);
        entity.setId(StringUtils.isEmpty(entity.getId()) ? UUIDUtil.getUUID() : entity.getId());
        entity.setEventId(eventId);
        return entity;
    }

    /**
     * 填充admin
     *
     * @param eventId
     * @param dto
     * @param entity
     */
    public static CmdbOpenstackNetwork fillOpenStackNetwork(String eventId, OpenStackNetworkDTO dto) {
        BoundMapperFacade<CmdbOpenstackNetwork, OpenStackNetworkDTO> mapper = adminMapperFactory
                .getMapperFacade(CmdbOpenstackNetwork.class, OpenStackNetworkDTO.class);
        CmdbOpenstackNetwork entity = mapper.mapReverse(dto);
        entity.setId(StringUtils.isEmpty(entity.getId()) ? UUIDUtil.getUUID() : entity.getId());
        entity.setEventId(eventId);
        return entity;
    }

    /**
     * 填充subnet
     *
     * @param eventId
     * @param dto
     * @param entity
     */
    public static CmdbOpenstackImage fillOpenStackImage(String eventId, OpenStackImageDTO dto) {
        BoundMapperFacade<CmdbOpenstackImage, OpenStackImageDTO> mapper = imageMapperFactory
                .getMapperFacade(CmdbOpenstackImage.class, OpenStackImageDTO.class);
        CmdbOpenstackImage entity = mapper.mapReverse(dto);
        entity.setId(StringUtils.isEmpty(entity.getId()) ? UUIDUtil.getUUID() : entity.getId());
        entity.setEventId(eventId);
        return entity;
    }

    public static List<CmdbOpenstackSubnet> getFillOpenStackSubnet(boolean isUpdate, List<OpenStackSubnetDTO> dtoList,
            List<CmdbOpenstackSubnet> entityList) {
        List<CmdbOpenstackSubnet> dataList = Lists.newArrayList();
        for (OpenStackSubnetDTO dto : dtoList) {
            if (!isUpdate) {
                CmdbOpenstackSubnet entity = fillOpenStackSubnet(null, dto);
                dataList.add(entity);
            } else {
                for (CmdbOpenstackSubnet entity : entityList) {
                    boolean updateFlag = isOpenStackUpdate(entity, dto);
                    if (updateFlag) {
                        CmdbOpenstackSubnet entity2 = fillOpenStackSubnet(null, dto);
                        entity2.setId(entity.getId());
                        dataList.add(entity2);
                        break;
                    }
                }
            }
        }
        return dataList;
    }

    public static List<CmdbOpenstackImage> getFillOpenStackImage(boolean isUpdate, List<OpenStackImageDTO> dtoList,
            List<CmdbOpenstackImage> entityList) {
        List<CmdbOpenstackImage> dataList = Lists.newArrayList();
        for (OpenStackImageDTO dto : dtoList) {
            if (!isUpdate) {
                CmdbOpenstackImage entity = fillOpenStackImage(null, dto);
                dataList.add(entity);
            } else {
                for (CmdbOpenstackImage entity : entityList) {
                    boolean updateFlag = isOpenStackUpdate(entity, dto);
                    if (updateFlag) {
                        CmdbOpenstackImage entity2 = fillOpenStackImage(null, dto);
                        entity2.setId(entity.getId());
                        dataList.add(entity2);
                        break;
                    }
                }
            }
        }
        return dataList;
    }

    /**
     * 填充基础信息.
     *
     * @param eventId
     * @param dto
     * @param entity
     */
    public static void fillBaseField(String eventId, BaseVmwareDTO dto, BaseCollectEntity entity) {
        entity.setId(StringUtils.isEmpty(entity.getId()) ? UUIDUtil.getUUID() : entity.getId());
        entity.setInstanceId(dto.getInstanceId());
        entity.setResource(dto.getResource());
        entity.setTime(dto.getTime());
        entity.setEventId(eventId);
        entity.setSrcCreator(dto.getCreator());
        entity.setSrcCreateTime(dto.getCreatTime());
        entity.setSrcOptTime(new Date(dto.getTimestamp() * 1000L));
        entity.setVersion(dto.getVersion());

    }

    /**
     * 填充vmware模型基础信息
     * @param eventId
     * @param dto
     * @param entity
     */
    private static void fillVmwareBaseField(String eventId, BaseVmwareDTO dto, BaseCmdbVmwareEntity entity) {
        entity.setId(StringUtils.isEmpty(entity.getId()) ? UUIDUtil.getUUID() : entity.getId());
        entity.setName(dto.getName());
        entity.setInstanceId(dto.getInstanceId());
        entity.setSrcCreateTime(dto.getCreatTime());
        entity.setSrcCreator(dto.getCreator());
        entity.setVersion(dto.getVersion());
        entity.setEventId(eventId);
        entity.setSrcOptTime(new Date(dto.getTimestamp() * 1000L));
    }

    /**
     * 根据instanceId+version比对网管和自动化，确定是否更新.
     *
     * @param entity
     *            网管存在的实体.
     * @param dto
     *            自动化查询的dto.
     * @return true/false
     */
    public static boolean isUpdate(BaseCollectEntity entity, BaseVmwareDTO dto) {
        boolean updateFlag = false;
        if (entity != null && dto.getInstanceId().equals(entity.getInstanceId()) && !dto.getVersion().equals(entity.getVersion())) {
            updateFlag = true;
        }
        return updateFlag;
    }

    public static boolean isOpenStackUpdate(BaseOpenStackEntity entity, BaseOpenStackDTO dto) {
        boolean updateFlag = false;
        if (entity != null && dto.getInstanceId().equals(entity.getInstanceId()) && !dto.getVersion().equals(entity.getVersion())) {
            updateFlag = true;
        }
        return updateFlag;
    }

    public static boolean isVmwareUpdate(BaseCmdbVmwareEntity entity, BaseVmwareDTO dto) {
        boolean updateFlag = false;
        if (entity != null && dto.getInstanceId().equals(entity.getInstanceId()) && !dto.getVersion().equals(entity.getVersion())) {
            updateFlag = true;
        }
        return updateFlag;
    }

    public static List<CmdbIpAddressPoolEntity> getFillIpAddressPool(boolean isUpdate, List<CmdbIpAddressPoolDTO> dtoList,
            List<CmdbIpAddressPoolEntity> entityList, String updateStatus) {
        List<CmdbIpAddressPoolEntity> dataList = Lists.newArrayList();
        for (CmdbIpAddressPoolDTO dto : dtoList) {
            if (!isUpdate || "Y".equals(updateStatus)) {
                CmdbIpAddressPoolEntity entity = new CmdbIpAddressPoolEntity();
                fillIpAddressPool(null, dto, entity);
                dataList.add(entity);
            } else {
                for (CmdbIpAddressPoolEntity entity : entityList) {
                    boolean updateFlag = isUpdate(entity, dto);
                    if (updateFlag) {
                        entity.setId(null);
                        fillIpAddressPool(null, dto, entity);
                        dataList.add(entity);
                        break;
                    }
                }
            }
        }
        return dataList;
    }

    public static List<CmdbIpConfPoolEntity> getFillIpConfPool(boolean isUpdate, List<CmdbIpConfPoolDTO> dtoList,
            List<CmdbIpConfPoolEntity> entityList) {
        List<CmdbIpConfPoolEntity> dataList = Lists.newArrayList();
        for (CmdbIpConfPoolDTO dto : dtoList) {
            if (!isUpdate) {
                CmdbIpConfPoolEntity entity = new CmdbIpConfPoolEntity();
                fillIpConfPool(null, dto, entity);
                dataList.add(entity);
            } else {
                for (CmdbIpConfPoolEntity entity : entityList) {
                    boolean updateFlag = isUpdate(entity, dto);
                    if (updateFlag) {
                        entity.setId(null);
                        fillIpConfPool(null, dto, entity);
                        dataList.add(entity);
                        break;
                    }
                }
            }
        }
        return dataList;
    }

    public static List<CmdbIpArpPoolEntity> getFillIpArpPool(boolean isUpdate, List<CmdbIpArpPoolDTO> dtoList,
            List<CmdbIpArpPoolEntity> entityList) {
        List<CmdbIpArpPoolEntity> dataList = Lists.newArrayList();
        for (CmdbIpArpPoolDTO dto : dtoList) {
            if (!isUpdate) {
                CmdbIpArpPoolEntity entity = new CmdbIpArpPoolEntity();
                fillIpArpPool(null, dto, entity);
                dataList.add(entity);
            } else {
                for (CmdbIpArpPoolEntity entity : entityList) {
                    boolean updateFlag = isUpdate(entity, dto);
                    if (updateFlag) {
                        entity.setId(null);
                        fillIpArpPool(null, dto, entity);
                        dataList.add(entity);
                        break;
                    }
                }
            }
        }
        return dataList;
    }

    public static List<CmdbVipCollectEntity> getFillVipAddressPool(boolean isUpdate, List<CmdbVipAddressPoolDTO> dtoList,
            List<CmdbVipCollectEntity> entityList) {
        List<CmdbVipCollectEntity> dataList = Lists.newArrayList();
        for (CmdbVipAddressPoolDTO dto : dtoList) {
            if (!isUpdate) {
                CmdbVipCollectEntity entity = new CmdbVipCollectEntity();
                fillVipAddressPool(null, dto, entity);
                dataList.add(entity);
            } else {
                for (CmdbVipCollectEntity entity : entityList) {
                    boolean updateFlag = isUpdate(entity, dto);
                    if (updateFlag) {
                        fillVipAddressPool(null, dto, entity);
                        dataList.add(entity);
                        break;
                    }
                }
            }
        }
        return dataList;
    }

    public static void fillNetworkPortGroup(String eventId, VmwareNetworkPortGroupDto dto, CmdbVmwareNetworkPortGroup entity) {
        if (entity == null) {
            entity = new CmdbVmwareNetworkPortGroup();
        }
        entity.setResourcePool(dto.getResourcepool());
        entity.setDataCenterName(dto.getDatacentername());
        entity.setPortGroupName(dto.getPortgroupname());
        fillVmwareBaseField(eventId, dto, entity);
    }

    /**
     * 填充网段-端口组实例
     */
    public static List<CmdbVmwareNetworkPortGroup> getFillNetworkPortGroup(boolean isUpdate,List<VmwareNetworkPortGroupDto> dtoList,List<CmdbVmwareNetworkPortGroup> entityList) {
        List<CmdbVmwareNetworkPortGroup> dataList = Lists.newLinkedList();
        for (VmwareNetworkPortGroupDto dto : dtoList) {
            if (!isUpdate) {
                CmdbVmwareNetworkPortGroup entity = new CmdbVmwareNetworkPortGroup();
                fillNetworkPortGroup(null,dto,entity);
                dataList.add(entity);
            } else {
                for (CmdbVmwareNetworkPortGroup entity : entityList) {
                    boolean updateFlag = isVmwareUpdate(entity, dto);
                    if (updateFlag) {
                        fillNetworkPortGroup(null,dto,entity);
                        dataList.add(entity);
                        break;
                    }
                }
            }
        }
        return dataList;
    }

    /**
     * 抽取diff_data要更新的字段值.
     *
     * @param value
     *            diff_data json串
     * @return 转化后的网络和磁盘信息.
     */
    public static <T> List<T> array2List(Object value, Class<T> clazz) {
        List<T> dataList = Lists.newArrayList();
        String jsonString = JSON.toJSONString(value);
        System.out.println(jsonString);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Object targetObj = jsonObject.get("new");
        if (targetObj == null) {
            return dataList;
        }
        if (targetObj instanceof JSONArray) {
            List<T> list = JSONObject.parseArray(targetObj.toString(), clazz);
            dataList.addAll(list);
        } else if (targetObj instanceof JSONObject) {

        } else {
            dataList.add((T) targetObj.toString());
        }
        return dataList;
    }

    public static void main(String[] args) {
        OpenStackResult<OpenStackSubnetDTO> result = queryOpenStackSubnet(ImmutableList.of("5a251155700ed"));
        List<OpenStackSubnetDTO> dataList = result.getData().getDataList();
        String eventId = UUIDUtil.getUUID();
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (OpenStackSubnetDTO dto : dataList) {
            // System.out.println(dto);
            CmdbOpenstackSubnet entity = fillOpenStackSubnet(eventId, dto);
            System.out.println("openstackSubnet==" + entity);
        }

        OpenStackResult<OpenStackImageDTO> result2 = queryOpenStackImage(ImmutableList.of("5a2513aaed229"));
        List<OpenStackImageDTO> dataList2 = result2.getData().getDataList();
        if (CollectionUtils.isEmpty(dataList2)) {
            return;
        }
        for (OpenStackImageDTO dto : dataList2) {
            // System.out.println(dto);
            CmdbOpenstackImage entity = fillOpenStackImage(eventId, dto);
            System.out.println("openstackImage==" + entity);
        }
    }
}
