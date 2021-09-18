package com.aspire.ums.cmdb.v2.process.maintenance;

import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.maintenance.entity.MaintenSoftware;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProject;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceProjectService;
import com.aspire.ums.cmdb.maintenance.service.MaintenSoftService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class MaintSoftwareImportFactory extends ImportFactory {

    private MaintenSoftService maintenSoftService;
    private ConfigDictService configDictService;
    private ICmdbMaintenanceProjectService projectService;

    @Override
    public void initSpringBean() {
        if (maintenSoftService == null) {
            maintenSoftService = SpringUtils.getBean(MaintenSoftService.class);
        }
        if (configDictService == null) {
            configDictService = SpringUtils.getBean(ConfigDictService.class);
        }
        if (projectService == null) {
            projectService = SpringUtils.getBean(ICmdbMaintenanceProjectService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        MaintenSoftware software = new MaintenSoftware();
        for (String key : dataMap.keySet()) {
            if (key.contains("必填") && StringUtils.isEmpty(dataMap.get(key))) {
                throw new RuntimeException("列[" + key + "] 不能为空");
            }
            if ("数量[必填]".equals(key)) {
                try {
                    Integer.parseInt(dataMap.get(key));
                } catch (Exception e) {
                    throw new RuntimeException("数量输入有误, 请输入整数");
                }
            }
        }
        CmdbMaintenanceProject project = new CmdbMaintenanceProject();
        project.setProjectName(dataMap.get("项目[必填]"));
        project = this.projectService.get(project);
        if (project == null) {
            throw new RuntimeException("所填项目->" + dataMap.get("项目[必填]") +"<-不存在");
        }
        software.setProject(project.getProjectName());
        software.setProjectId(project.getId());
        List<ConfigDict> dicts = this.configDictService.selectDictsByType("software_type", null, null,null);
        List<String> strDicts = new ArrayList<>();
        if (dicts.size() > 0) {
            dicts.forEach(dict -> {
                strDicts.add(dict.getValue());
            });
        }
        if (dicts.size() == 0 || !strDicts.contains(dataMap.get("分类[必填]"))){
            throw new RuntimeException("所填分类->" + dataMap.get("分类[必填]") +"<-不合法");
        }
        software.setClassify(dataMap.get("分类[必填]"));
        software.setSoftwareName(dataMap.get("软件名称[必填]"));
        DictValidator.validator("单位[必填]", dataMap.get("单位[必填]"), Arrays.asList(new String[]{"套"}));
        software.setUnit(dataMap.get("单位[必填]"));
        software.setNumber(dataMap.get("数量[必填]"));
        software.setAdmin(dataMap.get("维保管理员[必填]"));
        software.setRemark(dataMap.get("备注"));
        this.maintenSoftService.insertMaintenSoftware(software);
    }
}
