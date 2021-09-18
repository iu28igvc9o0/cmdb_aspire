package com.migu.tsg.microservice.atomicservice.composite.controller;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.google.common.base.Joiner;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbV2Helper;
import com.migu.tsg.microservice.atomicservice.composite.helper.OpsHelper;
import com.migu.tsg.microservice.atomicservice.composite.helper.ResourceAuthV2Helper;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class CommonResourceV2Controller {
    @Autowired
    protected ResourceAuthV2Helper resAuthHelper;
    @Autowired
    private CmdbV2Helper cmdbV2Helper;
    @Autowired
    private OpsHelper opsHelper;
    @Value("${systemType:}")
    private String systemType;

    /**
     * 处理  站点-设备类型-设备  的权限请求参数. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param totalConstraints
     * @param awareModel
     */
    protected boolean applyGeneralAuthConstraints(final Map<String, Set<String>> totalConstraints, final
    GeneralAuthConstraintsAware awareModel) {
        // 个性化判断是否是去RBAC改造
        if ("simple".equals(systemType)) {
            return true;
        }

        Set<String> idcTypeList = totalConstraints.get(ResourceAuthV2Helper.AREA);
        Set<String> roomIdSet = totalConstraints.get(ResourceAuthV2Helper.ROOM);
        Set<String> deviceTypeList = totalConstraints.get(ResourceAuthV2Helper.DEVICE_TYPE);
        Set<String> bizSystemList = totalConstraints.get(ResourceAuthV2Helper.BIZ_SYSTEM);
        Set<String> deviceIdList = totalConstraints.get(ResourceAuthV2Helper.DEVICE);
        if (!CollectionUtils.isEmpty(idcTypeList)) {
            awareModel.setAuthIdcIdList(new ArrayList<>(idcTypeList));
        }
        if (!CollectionUtils.isEmpty(roomIdSet)) {
            roomIdSet = handlePrecintWildcard(roomIdSet);
            awareModel.setAuthPrecinctList(new ArrayList<>(roomIdSet));
        }
        if (!CollectionUtils.isEmpty(deviceTypeList)) {
            deviceTypeList = handleDeviceTypeWildcard(deviceTypeList);
            awareModel.setAuthDeviceTypeList(new ArrayList<>(deviceTypeList));
        }
        if (!CollectionUtils.isEmpty(bizSystemList)) {
            bizSystemList = handleBizSysWildcard(bizSystemList);
            awareModel.setAuthBizSystemIdList(new ArrayList<>(bizSystemList));
        }
        if (!CollectionUtils.isEmpty(deviceIdList)) {
            awareModel.setAuthDeviceIdList(new ArrayList<>(deviceIdList));
        }

        Set<String> pipelineIdList = totalConstraints.get(ResourceAuthV2Helper.OPS_PIPELINE);
        Set<String> scriptIdList = totalConstraints.get(ResourceAuthV2Helper.OPS_SCRIPT);
        Set<String> yumIdList = totalConstraints.get(ResourceAuthV2Helper.OPS_YUM);
        Set<String> groupIdList = totalConstraints.get(ResourceAuthV2Helper.OPS_GROUP);
        if (!CollectionUtils.isEmpty(pipelineIdList)) {
            awareModel.setAuthPipelineIdList(pipelineIdList.stream().map(item -> item.substring(item.lastIndexOf("_")
                    + 1
            )).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(scriptIdList)) {
            awareModel.setAuthScriptIdList(scriptIdList.stream().map(item -> item.substring(item.lastIndexOf("_") + 1
            )).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(yumIdList)) {
            awareModel.setAuthYumIdList(yumIdList.stream().map(item -> item.substring(item.lastIndexOf("_") + 1
            )).collect(Collectors.toList()));
        }

        if (!CollectionUtils.isEmpty(groupIdList)) {
            List<String> tempGroupIdList = opsHelper.queryAllChildGroup(new ArrayList<>(groupIdList));
            awareModel.setAuthGroupIdList(new ArrayList<>(tempGroupIdList));
        }
        return awareModel.checkAuthConditions();
    }

    /**
     * 处理precint通配符, 把通配符下的所有precint都查出来 <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param rawPrecintSet
     * @return
     */
    Set<String> handlePrecintWildcard(Set<String> rawPrecintSet) {
        Set<String> result = new HashSet<String>();

        List<Map<String, String>> listroom = cmdbV2Helper.getRoomByIds(Joiner.on(",").join(rawPrecintSet));

        for (Map<String, String> configDict : listroom) {
            result.add(configDict.get("code"));
        }
        return result;
    }

    Set<String> handleDeviceTypeWildcard(Set<String> deviceTypeList) {
        Set<String> result = new HashSet<String>();
        List<Map<String, Object>> allConfigDict = cmdbV2Helper.listOption("device_type");
        for (Map<String, Object> configDict : allConfigDict) {
            if (deviceTypeList.contains(MapUtils.getString(configDict, "id"))) {
                result.add(MapUtils.getString(configDict, "value"));
            }
        }
        return result;
    }

    Set<String> handleBizSysWildcard(Set<String> bizSystemList) {
        Set<String> result = new HashSet<String>();
        List<Map<String, String>> mapList = cmdbV2Helper.getBizSystemByIds(Joiner.on(",").join(bizSystemList));
        for (Map<String, String> configDict : mapList) {
            result.add(configDict.get("code"));
        }
        return result;
    }

}
