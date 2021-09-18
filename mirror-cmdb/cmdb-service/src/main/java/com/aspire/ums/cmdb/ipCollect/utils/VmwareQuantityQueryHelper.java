package com.aspire.ums.cmdb.ipCollect.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONArray;
import com.aspire.ums.cmdb.automate.exception.AutomateException;
import com.aspire.ums.cmdb.ipCollect.enums.EventInstanceModuleEnum;
import com.aspire.ums.cmdb.ipCollect.exception.CollectException;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.*;
import com.aspire.ums.cmdb.openstack.enums.OpenStackEnums;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackImageDTO;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackResult;
import com.aspire.ums.cmdb.openstack.payload.dto.OpenStackSubnetDTO;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.util.JsonUtil;
import com.aspire.ums.cmdb.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/21 11:32
 */
@Slf4j
public class VmwareQuantityQueryHelper {

    private static EasyOpsAPI easyOpsAPI = new EasyOpsAPI();

    /**
     * 查询存活IP-网络设备.
     *
     * @param
     * @return
     */
    public static EasyOpsResult<CmdbIpAddressPoolDTO> queryIpAddressPool(List<String> instanceIdList, Integer page,
            Integer pageSize, Boolean fieldsFlag) throws CollectException {
        try {
            String response = getInstanceResponse(EventInstanceModuleEnum.IP_ADDRESS_POOL.getKey(), instanceIdList, page, pageSize,
                    fieldsFlag);
            return JsonUtil.jacksonConvert(response, new TypeReference<EasyOpsResult<CmdbIpAddressPoolDTO>>() {});
        } catch (Exception e) {
            log.error("", e);
            throw new CollectException(e);
        }
    }

    /**
     * 查询存活IP-网络设备配置文件解析.
     *
     * @param
     * @return
     */
    public static EasyOpsResult<CmdbIpConfPoolDTO> queryIpConfPool(List<String> instanceIdList, Integer page, Integer pageSize,
            Boolean fieldsFlag) throws CollectException {
        try {
            String response = getInstanceResponse(EventInstanceModuleEnum.IP_CONF_POOL.getKey(), instanceIdList, page, pageSize,
                    fieldsFlag);
            return JsonUtil.jacksonConvert(response, new TypeReference<EasyOpsResult<CmdbIpConfPoolDTO>>() {});
        } catch (Exception e) {
            log.error("", e);
            throw new CollectException(e);
        }
    }

    /**
     * 查询存活IP-arp扫描网段地址.
     *
     * @param
     * @return
     */
    public static EasyOpsResult<CmdbIpArpPoolDTO> queryIpArpPool(List<String> instanceIdList, Integer page, Integer pageSize,
            Boolean fieldsFlag) throws CollectException {
        try {
            String response = getInstanceResponse(EventInstanceModuleEnum.IP_ARP_POOL.getKey(), instanceIdList, page, pageSize,
                    fieldsFlag);
            return JsonUtil.jacksonConvert(response, new TypeReference<EasyOpsResult<CmdbIpArpPoolDTO>>() {});
        } catch (Exception e) {
            log.error("", e);
            throw new CollectException(e);
        }
    }

    /**
     * 根据存活IP.
     *
     * @param page
     * @param pageSize
     * @param fieldsFlag
     *            true-只查instanceId false-查询所有字段
     * @return
     * @throws CollectException
     */
    public static EasyOpsResult<CmdbVipAddressPoolDTO> queryVipAddressPool(List<String> instanceIdList, Integer page,
            Integer pageSize, Boolean fieldsFlag) throws CollectException {
        try {
            String response = getInstanceResponse(EventInstanceModuleEnum.VIP_ADDRESS_POOL.getKey(), instanceIdList, page, pageSize,
                    fieldsFlag);
            return JsonUtil.jacksonConvert(response, new TypeReference<EasyOpsResult<CmdbVipAddressPoolDTO>>() {});
        } catch (Exception e) {
            log.error("", e);
            throw new CollectException(e);
        }
    }

    public static OpenStackResult<OpenStackSubnetDTO> queryOpenstackSubnet(List<String> instanceIdList, Integer page,
            Integer pageSize, Boolean fieldsFlag) throws AutomateException {
        try {
            String response = getInstanceResponse(OpenStackEnums.OPENSTACK_SUBNET.getKey(), instanceIdList, page, pageSize,
                    fieldsFlag);
            return JsonUtil.jacksonConvert(response, new TypeReference<OpenStackResult<OpenStackSubnetDTO>>() {});
        } catch (Exception e) {
            log.error("", e);
            throw new AutomateException(e);
        }
    }

    public static OpenStackResult<OpenStackImageDTO> queryOpenstackImage(List<String> instanceIdList, Integer page,
            Integer pageSize, Boolean fieldsFlag) throws AutomateException {
        try {
            String response = getInstanceResponse(OpenStackEnums.OPENSTACK_IMAGE.getKey(), instanceIdList, page, pageSize,
                    fieldsFlag);
            return JsonUtil.jacksonConvert(response, new TypeReference<OpenStackResult<OpenStackImageDTO>>() {});
        } catch (Exception e) {
            log.error("", e);
            throw new AutomateException(e);
        }
    }

    public static EasyOpsResult<VmwareNetworkPortGroupDto> queryNetworkPortGroup(List<String> instanceIdList,Integer page, Integer pageSize, Boolean fieldsFlag)
            throws AutomateException {
        try {
            String response = getInstanceResponse(EventInstanceModuleEnum.VMWARE_NETWORK_PORTGROUP.getKey(),instanceIdList, page, pageSize, fieldsFlag);
            return new JsonMapper().readValue(response, new TypeReference<EasyOpsResult<VmwareNetworkPortGroupDto>>() {});
        } catch (IOException e) {
            log.error("获取自动化的VMWARE_NETWORK_PORTGROUP模型失败", e);
            throw new AutomateException(e);
        }
    }

    /**
     * @param instanceIdList
     *            实例ID
     * @param page
     *            页码页码
     * @param pageSize
     *            页大小
     * @param fieldsFlag
     *            指定获取 instanceId
     * @return
     */
    private static String buildRequestBody(List<String> instanceIdList, Integer page, Integer pageSize, Boolean fieldsFlag) {
        String fields;
        if (fieldsFlag) {
            fields = ",\"fields\":{\"instanceId\":true,\"_version\":true}";
        } else if (instanceIdList != null) {
            String instanceIdStr = JSONArray.toJSONString(instanceIdList);
            fields = ",\"query\":{\"instanceId\":{\"$in\":" + instanceIdStr + "}}";
        } else {
            fields = "";
        }

        String requestBody = String.format("{\"page\":%d,\"page_size\":%d%s}", page == null ? 1 : page,
                pageSize == null ? 200 : pageSize, fields);
        // System.out.println(requestBody);
        return requestBody;
    }

    /**
     * 查询全量数据.
     *
     * @param moduleId
     *            模型ID
     * @param page
     *            实例id
     * @param pageSize
     *            是否加载实例关联数据
     * @return 接口响应数据
     */
    private static String getInstanceResponse(String moduleId, List<String> instanceIdList, Integer page, Integer pageSize,
            Boolean fieldsFlag) {
        Map<String, String> data2 = new TreeMap<>();
        return easyOpsAPI.doApi(String.format("/cmdb_resource/object/%s/instance/_search", moduleId), data2,
                buildRequestBody(instanceIdList, page, pageSize, fieldsFlag), "POST");
    }

    /**
     * 获取某个模型的实例总数
     * @param moduleId 模型ID
     */
    public static int getInstanceCount(String moduleId) {
        Map<String, String> data = new TreeMap<>();
        String requestBody = "{}";
        EasyOpsAPI easyOpsAPI = new EasyOpsAPI();
        int count = 0;
        try {
            String post = easyOpsAPI.doApi(String.format("/cmdb_resource/object/%s/instance/_search_total", moduleId), data,
                    requestBody, "POST");
            if (StringUtils.isNotEmpty(post)) {
                Map map = new JsonMapper().readValue(post, Map.class);
                if (null != map && null != map.get("data")) {
                    count = Integer.parseInt(map.get("data").toString());
                }
            }
        } catch (IOException e) {
            log.error("模型{}总量获取失败",moduleId);
        }
        return count;
    }

}
