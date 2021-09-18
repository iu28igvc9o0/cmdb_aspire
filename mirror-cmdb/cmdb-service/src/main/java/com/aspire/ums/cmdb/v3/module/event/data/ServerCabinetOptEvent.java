package com.aspire.ums.cmdb.v3.module.event.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.cmic.service.ICmdbCmicInstanceService;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceIpManagerService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import com.aspire.ums.cmdb.v3.module.event.EventConst;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import com.google.common.collect.Maps;

/**
 * 机柜汇总页面录入时,保存机柜申请记录
 *
 * @author jiangxuwen
 * @date 2021/2/22 17:39
 */
public class ServerCabinetOptEvent extends AbstractModuleEvent {

    protected ModuleService moduleService;

    protected ICmdbInstanceService instanceService;

    protected ICmdbInstanceIpManagerService ipManagerService;

    protected ICmdbV3ModuleCatalogService catalogService;

    protected SchemaService schemaService;

    protected ICmdbCmicInstanceService cmicInstanceService;

    protected ICmdbConfigService cmdbConfigService;

    protected ConfigDictService configDictService;

    protected ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void initSpringBeans() {
        moduleService = moduleService == null ? SpringUtils.getBean(ModuleService.class) : moduleService;
        instanceService = instanceService == null ? SpringUtils.getBean(ICmdbInstanceService.class) : instanceService;
        ipManagerService = ipManagerService == null ? SpringUtils.getBean(ICmdbInstanceIpManagerService.class) : ipManagerService;
        catalogService = catalogService == null ? SpringUtils.getBean(ICmdbV3ModuleCatalogService.class) : catalogService;
        schemaService = schemaService == null ? SpringUtils.getBean(SchemaService.class) : schemaService;
        cmicInstanceService = cmicInstanceService == null ? SpringUtils.getBean(ICmdbCmicInstanceService.class)
                : cmicInstanceService;
        cmdbConfigService = cmdbConfigService == null ? SpringUtils.getBean(ICmdbConfigService.class) : cmdbConfigService;
        configDictService = configDictService == null ? SpringUtils.getBean(ConfigDictService.class) : configDictService;
        taskExecutor = taskExecutor == null ? SpringUtils.getBean("taskExecutor", ThreadPoolTaskExecutor.class) : taskExecutor;
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        Map<String, Object> returnMap = Maps.newHashMap();
        String optType = handleData.get("changeType").toString();
        // TODO: 对接bpm流程工单
        boolean bpmFlag = handleData.containsKey("bpmFlag");
        String optTypeValue = "";
        String statusValue = "";
        String id11 = configDictService.getIdByNoteAndCol("新增", "server_opt_type");
        String id12 = configDictService.getIdByNoteAndCol("删除", "server_opt_type");
        String id13 = configDictService.getIdByNoteAndCol("修改", "server_opt_type");
        String id14 = configDictService.getIdByNoteAndCol("上电", "server_opt_type");
        String id15 = configDictService.getIdByNoteAndCol("下架", "server_opt_type");

        String id21 = configDictService.getIdByNoteAndCol("是", "whether");
        String id22 = configDictService.getIdByNoteAndCol("否", "whether");
        boolean isUpdate = false;
        boolean isDelete = false;
        boolean isNew = false;
        if (EventConst.EVENT_TYPE_DATA_INSERT.equals(optType)) {
            isNew = true;
            optTypeValue = id11;
            if (bpmFlag) {
                optTypeValue = id14;
            }
        } else if (EventConst.EVENT_TYPE_DATA_UPDATE.equals(optType)) {
            isUpdate = true;
            optTypeValue = id13;
        } else if (EventConst.EVENT_TYPE_DATA_DELETE.equals(optType)) {
            optTypeValue = id12;
            if (bpmFlag) {
                optTypeValue = id15;
            }
            isDelete = true;
        }
        String username = handleData.get("username").toString();
        Map<String, Object> oldInstanceData = Maps.newHashMap();
        if (isUpdate) {
            oldInstanceData = instanceService.getInstanceDetail(moduleId, instanceId);
        }
        if (isDelete) {
            oldInstanceData = JSONObject.parseObject(JSON.toJSONString(handleData.get("instanceDetail")), Map.class);
        }
        List<CmdbCollectApproval> approvals = JSONArray.parseArray(JSON.toJSONString(handleData.get("approvals")),
                CmdbCollectApproval.class);
        Map<String, Object> updateData = Maps.newHashMap();
        if (isUpdate || isDelete) {
            updateData.putAll(oldInstanceData);
        }
        for (CmdbCollectApproval approval : approvals) {
            Map<String, Object> instanceData = (Map<String, Object>) JSONObject.parse(approval.getResourceData());
            if (!isDelete) {
                if (instanceData.containsKey("online_count") && instanceData.get("online_count").toString() != null) {
                    int onlineCount = Integer.parseInt(instanceData.get("online_count").toString());
                    if (onlineCount > 0) {
                        instanceData.put("server_online_status", id21);
                    }
                    if (onlineCount <= 0) {
                        instanceData.put("server_online_status", id22);
                    }
                }
                updateData.putAll(instanceData);
            }

            if (isNew) {
                Object billingStartDateObj = instanceData.get("bill_start_date");
                if (billingStartDateObj == null || StringUtils.isBlank(billingStartDateObj.toString())) {
                    updateData.put("bill_start_date", instanceData.get("first_online_date"));
                }
            }
        }
        updateData.put("instance_id", instanceId);
        updateData.put("id", instanceId);
        updateData.put("module_id", moduleId);
        updateData.put("opt_type", optTypeValue);
        instanceService.updateInstance(updateData);
        createServerCabinetRecord(username, updateData);

        returnMap.put("flag", true);
        returnMap.put("msg", "操作成功");
        return returnMap;
    }

    private void createServerCabinetRecord(String userName, Map<String, Object> serverCabinetData) {
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
        serverCabinetRecord.put("opt_type", serverCabinetData.get("opt_type"));
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
