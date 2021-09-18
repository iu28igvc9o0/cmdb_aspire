package com.aspire.ums.cmdb.maintenance.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.ICmdbMaintenanceProjectAPI;
import com.aspire.ums.cmdb.maintenance.payload.*;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceProjectInstanceService;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbMaintenanceProjectController
 * Author:   zhu.juwang
 * Date:     2019/7/29 17:30
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbMaintenanceProjectController implements ICmdbMaintenanceProjectAPI {
    @Autowired
    private ICmdbMaintenanceProjectService projectService;
    @Autowired
    private ICmdbMaintenanceProjectInstanceService projectInstanceService;
    @Autowired
    private ICmdbMaintenanceProjectService cmdbMaintenanceProjectService;

    @Override
    public Result<Map<String, Object>> getSimpleList(@RequestBody CmdbMaintenanceProjectQuery entity) {
        log.info("Request into CmdbMaintenanceProjectController.getSimpleList()  params -> {}", entity);
        try {
            return projectService.getSimpleList(entity);
        } catch (Exception e) {
            log.error("getSimpleList maintenance project error.", e);
        }
        return null;
    }

    @Override
    public JSONObject insertMaintenanceProject(@RequestBody CmdbMaintenanceProject maintenanceProject) {
        log.info("Request into CmdbMaintenanceProjectController.insertMaintenanceProject()  params -> {}", maintenanceProject);
        JSONObject jsonObject = new JSONObject();
        try {
            projectService.save(maintenanceProject);
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            log.error("Create maintenance project error.", e);
        }
        return jsonObject;
    }

    @Override
    public Result<CmdbMaintenanceProject> getMaintenanceProjectList(@RequestBody CmdbMaintenanceProjectQuery query) {
        log.info("Request into CmdbMaintenanceProjectController.getMaintenanceProjectList()  params -> {}", query);
        try {
            return projectService.listByEntity(query);
        } catch (Exception e) {
            log.error("Create maintenance project error.", e);
        }
        return null;
    }

    @Override
    public Result<Map<String,Object>> getMaintenanceProjectListNotMoney(@RequestBody CmdbMaintenanceProjectQuery query) {
        log.info("Request into CmdbMaintenanceProjectController.getMaintenanceProjectListNotMoney()  params -> {}", query);
        try {
            return projectService.listNotMoney(query);
        } catch (Exception e) {
            log.error("Create maintenance project error.", e);
        }
        return null;
    }

    @Override
    public CmdbMaintenanceProject getMaintenanceProjectInfo(@RequestParam("projectId") String projectId) {
        log.info("Request into CmdbMaintenanceProjectController.getMaintenanceProjectInfo()  params -> {}", projectId);
        try {
            CmdbMaintenanceProject queryProject = new CmdbMaintenanceProject();
            queryProject.setId(projectId);
            return projectService.get(queryProject);
        } catch (Exception e) {
            log.error("Create maintenance project error.", e);
        }
        return null;
    }

    @Override
    public CmdbMaintenanceProject getMaintenanceProjectInfoByName(@RequestParam("projectName") String projectName) {
        log.info("Request into CmdbMaintenanceProjectController.getMaintenanceProjectInfoByName()  params -> {}", projectName);
        try {
            CmdbMaintenanceProject queryProject = new CmdbMaintenanceProject();
            queryProject.setProjectName(projectName);
            return projectService.get(queryProject);
        } catch (Exception e) {
            log.error("Create maintenance project error.", e);
        }
        return null;
    }

    @Override
    public JSONObject bindProjectInstance(@RequestBody List<CmdbMaintenanceProjectInstance> projectInstanceList,
                                          @RequestParam("project_id") String projectId) {
        log.info("Request into CmdbMaintenanceProjectController.bindProjectInstance()  params -> {}, projectId -> {}",
                projectInstanceList, projectId);
        JSONObject jsonObject = new JSONObject();
        try {
            projectInstanceService.batchInsert(projectInstanceList, projectId);
            // 同步设备绑定的数据到ES中，且更新CI信息
            List<String> instanceList = new ArrayList<>();
            for(CmdbMaintenanceProjectInstance item: projectInstanceList) {
                instanceList.add(item.getInstanceId());
            }
            cmdbMaintenanceProjectService.syncInstanceMaintenanceInfo(projectId,instanceList);
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            log.error("Bind maintenance project instance error.", e);
        }
        return jsonObject;
    }

    @Override
    public JSONObject removeBindInstance(@RequestParam("project_instance_id") String projectInstanceId,
                                         @RequestParam("project_id") String projectId) {
        log.info("Request into CmdbMaintenanceProjectController.removeBindInstance()  project_instance_id -> {}, projectId -> {}",
                projectInstanceId, projectId);
        JSONObject jsonObject = new JSONObject();
        try {
            return projectInstanceService.delete(projectInstanceId, projectId);
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            log.error("Bind maintenance project instance error.", e);
        }
        return jsonObject;
    }

    @Override
    public Result<CmdbMaintenanceProjectBindInstance> listBindInstance(@RequestBody CmdbMaintenanceProjectBindInstanceQuery query) {
        log.info("Request into CmdbMaintenanceProjectController.getMaintenanceProjectList()  params -> {}", query);
        try {
            return projectInstanceService.getProjectBindInstanceList(query);
        } catch (Exception e) {
            log.error("Get maintenance project bind instance error.", e);
        }
        return null;
    }

    @Override
    public JSONObject deleteMaintenanceProject(@RequestParam("project_id") String projectId) {
        log.info("Request into CmdbMaintenanceProjectController.deleteMaintenanceProject()  params -> {}", projectId);
        JSONObject jsonObject = new JSONObject();
        try {
            return projectService.deleteMaintenanceProject(projectId);
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            log.error("Delete maintenance project error error.", e);
        }
        return jsonObject;
    }

    @Override
    public List<Map<String, Object>> exportProjectList(@RequestBody CmdbMaintenanceProjectQuery query) {
        log.info("Request into CmdbMaintenanceProjectController.exportProjectList()  params -> {}", query);
        try {
            return projectService.exportProjectList(query);
        } catch (Exception e) {
            log.error("Get maintenance project list error.", e);
        }
        return null;
    }

    @Override
    public CmdbMaintenanceProject getValidProjectByDeviceSn(@RequestParam("device_sn") String deviceSn) {
        log.info("Request into CmdbMaintenanceProjectController.getValidProjectByDeviceSn()  params -> {}", deviceSn);
        try {
            return projectService.getValidProjectByDeviceSn(deviceSn);
        } catch (Exception e) {
            log.error("Get maintenance project error.", e);
        }
        return null;
    }

    @Override
    public JSONObject addServiceNum(@RequestBody List<CmdbMaintenanceServiceNum> request) {
        log.info("Request into CmdbMaintenanceProjectController.addServiceNum()  params -> {}", request);
        JSONObject returnJson = new JSONObject();
        try{
            projectService.insertServiceNum(request);
            returnJson.put("flag", "success");
            returnJson.put("msg", "新增成功");
        } catch (Exception e) {
            log.error("Get maintenance project error.", e);
            returnJson.put("flag", "error");
            returnJson.put("msg", e.getMessage());
        }
        return returnJson;
    }

    @Override
    public String syncInstanceMaintenanceInfo(@RequestParam(value = "project_id", required = false) String projectId) {
        return projectService.syncInstanceMaintenanceInfo(projectId,null);
    }

    @Override
    public List<Map<String, Object>> getMaintenObjByTimeAndSn(@RequestBody Map<String,String> mpValue) {
        return projectService.getMaintenObjByTimeAndSn(mpValue);
    }
}
