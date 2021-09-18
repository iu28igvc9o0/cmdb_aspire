package com.aspire.ums.cmdb.v3.module.event.data;

import java.time.LocalDate;
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
 * 网络线路汇总页面录入时,保存网络线路申请记录
 *
 * @author jiangxuwen
 * @date 2021/2/22 18:33
 */
public class ServerNetworkLineOptEvent extends AbstractModuleEvent {

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
                updateData.putAll(instanceData);
            }
        }
        updateData.put("instance_id", instanceId);
        updateData.put("id", instanceId);
        updateData.put("module_id", moduleId);
        updateData.put("opt_type", optTypeValue);
        createNetworkLineRecord(username, updateData);
        createNetworkLineHis(username, updateData);
        if (isNew) {
            createNetworkLineBill(username, updateData);
        }
        returnMap.put("flag", true);
        returnMap.put("msg", "操作成功");
        return returnMap;
    }

    private void createNetworkLineRecord(String userName, Map<String, Object> serverCabinetData) {
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
        networkLineRecord.put("opt_type", serverCabinetData.get("opt_type"));
        networkLineRecord.put("owner_person", serverCabinetData.get("owner_person"));
        networkLineRecord.put("order_no", serverCabinetData.get("order_no"));
        networkLineRecordList.add(networkLineRecord);

        cmicInstanceService.batchInsertServerProject(serverProjectList);
        cmicInstanceService.batchInsertNetworkLineMgrList(networkLineMgrList);
        cmicInstanceService.batchInsertNetworkLineRecord(networkLineRecordList);
    }

    private void createNetworkLineHis(String userName, Map<String, Object> serverCabinetData) {
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

    private void createNetworkLineBill(String userName, Map<String, Object> instanceData) {
        String moduleId = "ce4e68d1e95b4eb893703dedd160e0c3";
        String id = UUIDUtil.getUUID();
        int year = LocalDate.now().getYear();
        CmdbConfig serverProjectModule = cmdbConfigService.getConfigByCode("moduleId:networkLineBill");
        if (serverProjectModule != null && StringUtils.isNotBlank(serverProjectModule.getConfigValue())) {
            moduleId = serverProjectModule.getConfigValue();
        }
        instanceData.put("module_id", moduleId);
        instanceData.put("instance_id", id);
        instanceData.put("id", id);
        instanceData.put("bill_year", String.valueOf(year));
        instanceService.addInstance(userName, instanceData, "自动生成");
    }
}
