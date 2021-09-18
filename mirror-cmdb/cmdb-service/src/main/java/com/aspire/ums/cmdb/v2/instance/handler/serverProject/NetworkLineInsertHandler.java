package com.aspire.ums.cmdb.v2.instance.handler.serverProject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 网络线路新增后置事件
 *
 * @author jiangxuwen
 * @date 2021/2/4 13:43
 */
@Slf4j
public class NetworkLineInsertHandler extends AbstractServerProjectInsertFactory {

    @Override
    public void createServerProjectData(String userName, Map<String, Object> instanceData, String operateType) {
        createNetworkLineRecord(userName, instanceData, operateType);
        createNetworkLineHis(userName, instanceData, operateType);
    }

    private void createNetworkLineRecord(String userName, Map<String, Object> serverCabinetData, String operateType) {
        List<Map<String, Object>> serverProjectList = new ArrayList<>();
        List<Map<String, Object>> networkLineMgrList = new ArrayList<>();
        List<Map<String, Object>> networkLineRecordList = new ArrayList<>();

        Map<String, Object> serverProject = Maps.newHashMap();
        Map<String, Object> networkLineMgr = Maps.newHashMap();
        Map<String, Object> networkLineRecord = Maps.newHashMap();
        String id = UUIDUtil.getUUID();
        // 网络线路 申请记录moduleId
        String moduleId = "b43fb7edaaf943bcaff310d72b02102f";
        CmdbConfig serverProjectModule = cmdbConfigService.getConfigByCode("moduleId:networkLineRecord");
        if (serverProjectModule != null && StringUtils.isNotBlank(serverProjectModule.getConfigValue())) {
            moduleId = serverProjectModule.getConfigValue();
        }
        serverProject.put("id", id);
        // TODO:修改moduleId
        serverProject.put("module_id", moduleId);
        serverProject.put("insert_person", userName);
        serverProject.put("insert_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        serverProject.put("update_person", userName);
        serverProject.put("update_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        serverProjectList.add(serverProject);

        networkLineMgr.putAll(serverCabinetData);
        networkLineMgr.put("id", id);
        networkLineMgrList.add(networkLineMgr);

        networkLineRecord.put("id", id);
        // TODO:现在写死是 新增,后期接入bpm,跟bpm对应。还需要增减数量.
        networkLineRecord.put("opt_type", "新增");
        networkLineRecord.put("owner_person", serverCabinetData.get("owner_person"));
        networkLineRecord.put("order_no", serverCabinetData.get("order_no"));
        networkLineRecordList.add(networkLineRecord);

        cmicInstanceService.batchInsertServerProject(serverProjectList);
        cmicInstanceService.batchInsertNetworkLineMgrList(networkLineMgrList);
        cmicInstanceService.batchInsertNetworkLineRecord(networkLineRecordList);
    }

    private void createNetworkLineHis(String userName, Map<String, Object> serverCabinetData, String operateType) {
        List<Map<String, Object>> serverProjectList = new ArrayList<>();
        List<Map<String, Object>> networkLineHisList = new ArrayList<>();

        Map<String, Object> serverProject = Maps.newHashMap();
        Map<String, Object> networkLineHis = Maps.newHashMap();
        String id = UUIDUtil.getUUID();
        // 网络线路 申请记录moduleId
        String moduleId = "c0eca8917b434b109b84e490c76aae67";
        CmdbConfig serverProjectModule = cmdbConfigService.getConfigByCode("moduleId:networkLineHistory");
        if (serverProjectModule != null && StringUtils.isNotBlank(serverProjectModule.getConfigValue())) {
            moduleId = serverProjectModule.getConfigValue();
        }
        serverProject.put("id", id);
        // TODO:修改moduleId
        serverProject.put("module_id", moduleId);
        serverProject.put("insert_person", userName);
        serverProject.put("insert_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        serverProject.put("update_person", userName);
        serverProject.put("update_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        serverProjectList.add(serverProject);

        // TODO: 1.保存network_line_id 2.如果是走bpm流程，数量需要增减.
        networkLineHis.putAll(serverCabinetData);
        networkLineHis.put("id", id);
        networkLineHis.put("network_line_id", serverCabinetData.get("instance_id"));
        networkLineHisList.add(networkLineHis);

        cmicInstanceService.batchInsertServerProject(serverProjectList);
        cmicInstanceService.batchInsertNetworkLineHis(networkLineHisList);
    }
}
