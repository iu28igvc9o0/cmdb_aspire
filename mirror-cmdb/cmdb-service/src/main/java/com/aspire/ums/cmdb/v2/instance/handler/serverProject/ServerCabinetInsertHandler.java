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
 * 机柜新增后置事件
 *
 * @author jiangxuwen
 * @date 2021/2/4 13:43
 */
@Slf4j
public class ServerCabinetInsertHandler extends AbstractServerProjectInsertFactory {

    @Override
    public void createServerProjectData(String userName, Map<String, Object> instanceData, String operateType) {
        createServerCabinetRecord(userName, instanceData, operateType);
    }

    private void createServerCabinetRecord(String userName, Map<String, Object> serverCabinetData, String operateType) {
        List<Map<String, Object>> serverProjectList = new ArrayList<>();
        List<Map<String, Object>> serverCabinetList = new ArrayList<>();
        List<Map<String, Object>> serverCabinetRecordList = new ArrayList<>();

        Map<String, Object> serverProject = Maps.newHashMap();
        Map<String, Object> serverCabinet = Maps.newHashMap();
        Map<String, Object> serverCabinetRecord = Maps.newHashMap();
        // 机柜申请记录moduleId
        String moduleId = "6749da0e8d2d498e83d6cc5fa8231c77";
        CmdbConfig serverProjectModule = cmdbConfigService.getConfigByCode("moduleId:serverCabinetRecord");
        if (serverProjectModule != null && StringUtils.isNotBlank(serverProjectModule.getConfigValue())) {
            moduleId = serverProjectModule.getConfigValue();
        }
        String id = UUIDUtil.getUUID();
        serverProject.put("id", id);
        // TODO:修改moduleId
        serverProject.put("module_id", moduleId);
        serverProject.put("insert_person", userName);
        serverProject.put("insert_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        serverProject.put("update_person", userName);
        serverProject.put("update_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        serverProjectList.add(serverProject);

        serverCabinet.put("id", id);
        serverCabinet.put("idcType", serverCabinetData.get("idcType"));
        serverCabinet.put("server_room_location", serverCabinetData.get("server_room_location"));
        serverCabinet.put("server_cabinet_code", serverCabinetData.get("server_cabinet_code"));
        serverCabinet.put("server_cabinet_standard", serverCabinetData.get("server_cabinet_standard"));
        serverCabinet.put("project_belong_to", serverCabinetData.get("project_belong_to"));
        serverCabinet.put("business_belong_to", serverCabinetData.get("business_belong_to"));
        serverCabinetList.add(serverCabinet);

        serverCabinetRecord.put("id", id);
        serverCabinetRecord.put("idcType", serverCabinetData.get("idcType"));
        // if ("是".equals(serverCabinetData.get("server_online_status"))) {
        serverCabinetRecord.put("opt_type", "加电");
        // } else if ("否".equals(serverCabinetData.get("server_online_status"))) {
        // serverCabinetRecord.put("opt_type", "下电");
        // }
        serverCabinetRecord.put("server_room_location", serverCabinetData.get("server_room_location"));
        serverCabinetRecord.put("server_cabinet_code", serverCabinetData.get("server_cabinet_code"));
        serverCabinetRecord.put("server_cabinet_standard", serverCabinetData.get("server_cabinet_standard"));
        serverCabinetRecord.put("server_device_count", serverCabinetData.get("online_count"));
        serverCabinetRecord.put("first_online_date", serverCabinetData.get("first_online_date"));
        serverCabinetRecord.put("bill_start_date", serverCabinetData.get("bill_start_date"));
        serverCabinetRecord.put("offline_date", serverCabinetData.get("offline_date"));
        serverCabinetRecord.put("project_belong_to", serverCabinetData.get("project_belong_to"));
        serverCabinetRecord.put("business_belong_to", serverCabinetData.get("business_belong_to"));
        serverCabinetRecordList.add(serverCabinetRecord);

        cmicInstanceService.batchInsertServerProject(serverProjectList);
        cmicInstanceService.batchInsertServerCabinet(serverCabinetList);
        cmicInstanceService.batchInsertServerCabinetRecord(serverCabinetRecordList);
    }

}
