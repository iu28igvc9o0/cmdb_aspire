package com.aspire.ums.cmdb.v2.process.maintenance;

import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProject;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectInstance;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceProjectInstanceService;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceProjectService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.cache.CacheConst;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: MaintenanceProjectDeviceImportFactory
 * Author:   zhu.juwang
 * Date:     2019/8/6 9:26
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
public class MaintenanceProjectDeviceImportFactory extends ImportFactory {
    private ICmdbMaintenanceProjectService projectService;
    private ICmdbMaintenanceProjectInstanceService projectInstanceService;
    private ICmdbInstanceService instanceService;
    private CmdbMaintenanceProject maintenanceProject;

    @Override
    public void initBasic() {
        super.initBasic();
        Map<String, Object> importData = super.getProcessImportData(super.getImportProcess().getProcessId());
        Map<String, Object> processParams = (Map<String, Object>) importData.get("processParams");
        if (processParams.get("maintenanceProjectName") != null) {
            String projectName = processParams.get("maintenanceProjectName").toString();
            CmdbMaintenanceProject query = new CmdbMaintenanceProject();
            query.setProjectName(projectName);
            maintenanceProject = getProjectService().get(query);
            if (maintenanceProject == null) {
                throw new RuntimeException("维保项目名称[" + projectName + "]不存在, 关联设备失败");
            }
        }
    }

    private ICmdbMaintenanceProjectService getProjectService() {
        if (projectService == null) {
            projectService = SpringUtils.getBean(ICmdbMaintenanceProjectService.class);
        }
        return projectService;
    }

    private ICmdbMaintenanceProjectInstanceService getProjectInstanceService() {
        if (projectInstanceService == null) {
            projectInstanceService = SpringUtils.getBean(ICmdbMaintenanceProjectInstanceService.class);
        }
        return projectInstanceService;
    }

    private ICmdbInstanceService getInstanceService() {
        if (instanceService == null) {
            instanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
        return instanceService;
    }

    @Override
    public void initSpringBean() {
        this.getProjectService();
        this.getProjectInstanceService();
        this.getInstanceService();
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        String columnValue;
        String deviceSn = "";
        for (String key : dataMap.keySet()) {
            columnValue = dataMap.get(key);
            if (key.contains("必填")) {
                EmptyValidator.notEmpty(key, columnValue);
            }
            if ("设备序列号[必填]".equals(key)) {
                if (columnValue.contains(",")) {
                    throw new RuntimeException("设备序列号格式不正确, 请确保每行一个设备序列号");
                }
                deviceSn = columnValue;
            }
        }
        // 判断设备序列号是否已经绑定, 如果已经绑定则跳过
        CmdbMaintenanceProject existsBind = projectService.getValidProjectByDeviceSn(deviceSn);
        if (existsBind != null) {
            throw new RuntimeException("设备序列号已经与维保项目名称[" + existsBind.getProjectName() + "]绑定.");
        }
        try {
            CmdbMaintenanceProjectInstance projectInstance = new CmdbMaintenanceProjectInstance();
            projectInstance.setProjectId(maintenanceProject.getId());
            projectInstance.setDeviceSn(deviceSn);
            CmdbInstance queryInstance = new CmdbInstance();
            queryInstance.setDeviceSn(deviceSn);
            CmdbInstance cmdbInstance = getInstanceService().get(queryInstance);
            List<String> instanceStr = new ArrayList<>();
            if (cmdbInstance != null) {
                projectInstance.setInstanceId(cmdbInstance.getId());
                instanceStr.add(cmdbInstance.getId());
            }
            getProjectInstanceService().insert(projectInstance);
            // 同步设备绑定的数据到ES中
            getProjectService().syncInstanceMaintenanceInfo(projectInstance.getProjectId(),instanceStr);
        } catch (Exception e) {
            throw new RuntimeException("维保项目绑定设备失败:" + e.getMessage(), e);
        }
    }
}
