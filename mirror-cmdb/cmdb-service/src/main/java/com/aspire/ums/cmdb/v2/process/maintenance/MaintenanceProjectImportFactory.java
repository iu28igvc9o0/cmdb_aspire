package com.aspire.ums.cmdb.v2.process.maintenance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProject;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceProjectService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: MaintenanceProjectImportFactory
 * Author:   zhu.juwang
 * Date:     2019/8/5 17:20
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
public class MaintenanceProjectImportFactory extends ImportFactory {
    private List<Map<String, Object>> serviceTypes;
    private List<Map<String, Object>> maintenanceTypes;
    private Map<String, Object> produces;
    private List<Map<String, Object>> deviceTypes;
    private List<Map<String, Object>> maintenanceProjectTypes;
    private List<Map<String, Object>> procureTypes;
    private List<Map<String, Object>> deviceAreas;
    private List<Map<String, Object>> payMethods;
    private ICmdbMaintenanceProjectService projectService;
    private ModuleService moduleService;

    @Override
    public void initBasic() {
        super.initBasic();
        serviceTypes = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'mainten_service_type'");
        deviceTypes = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'device_type'");
        maintenanceTypes = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'maintenance_type'");
        maintenanceProjectTypes = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'maintenance_project_type'");
        procureTypes = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'procure_type'");
        deviceAreas = super.getDictList("select idc_code `key`, idc_name `value` from cmdb_idc_manager where is_delete='0'");
        payMethods = super.getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'pay_method'");
        produces = new HashMap<>();
        Map<String,Object> params = JSON.parseObject("{\"params\": {\"produce_type\": \"维保供应商\"}}");
        List<Map<String, Object>> rsList = this.moduleService.getModuleData(params,null,"default_produce_module_id");
        for (Map<String, Object> item : rsList) {
            Map<String,String> tmpObj = new HashMap<>();
            tmpObj.put("mainteName",item.get("provider_cont") == null ? null : item.get("provider_cont").toString());
            tmpObj.put("maintePhone",item.get("service_concat_phone") == null ? null : item.get("service_concat_phone").toString());
            tmpObj.put("maintenEmail",item.get("service_concat_email") == null ? null : item.get("service_concat_email").toString());
            tmpObj.put("contractName",item.get("mainten_concat") == null ? null : item.get("mainten_concat").toString());
            tmpObj.put("contractPhone",item.get("mainten_concat_phone") == null ? null : item.get("mainten_concat_phone").toString());
            tmpObj.put("contractEmail",item.get("mainten_concat_email") == null ? null : item.get("mainten_concat_email").toString());
            produces.put(item.get("produce_name").toString(),tmpObj);
        }
    }

    @Override
    public void initSpringBean() {
        if (projectService == null) {
            projectService = SpringUtils.getBean(ICmdbMaintenanceProjectService.class);
        }
        if (moduleService == null) {
            moduleService = SpringUtils.getBean(ModuleService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        CmdbMaintenanceProject project = new CmdbMaintenanceProject();
        String columnValue;
        BigDecimal newMoney = null;
        for (String key : dataMap.keySet()) {
            columnValue = dataMap.get(key);
            if (key.contains("必填")) {
                EmptyValidator.notEmpty(key, columnValue);
            }
            if ("项目名称[必填]".equals(key)) {
                project.setProjectName(columnValue);
                continue;
            }
            if ("合同编号".equals(key)) {
                project.setProjectNo(columnValue);
                continue;
            }
            if ("合同供应商".equals(key)) {
                if(this.produces.containsKey(columnValue)) {
                    Map<String,String> mp = (Map<String,String>) this.produces.get(columnValue);
                    project.setContractProduceName(mp.get("contractName"));
                    project.setContractProducePhone(mp.get("contractPhone"));
                    project.setContractProduceEmail(mp.get("contractEmail"));
                } else {
                    Set<String> s = this.produces.keySet();
                    throw new RuntimeException("列["+key+"]值[" + columnValue + "]不在可选范围. 可选值["+ JSONArray.toJSONString(s) +"]");
                }
                project.setContractProduce(columnValue);
                continue;
            }
            if ("合同联系人名称".equals(key)) {
                project.setContractProduceName(columnValue);
                continue;
            }
            if ("合同联系人邮箱".equals(key)) {
                FormateValidator.isEmailFormate(key,columnValue);
                project.setContractProduceEmail(columnValue);
                continue;
            }
            if ("合同联系人电话".equals(key)) {
                FormateValidator.isPhoneFormate(key,columnValue);
                project.setContractProduceName(columnValue);
                continue;
            }
            if ("合同供应商".equals(key)) {
                project.setContractProduce(columnValue);
                continue;
            }
            if ("维保类型".equals(key)) {
                DictValidator.validator(key, columnValue, this.maintenanceTypes);
                project.setMaintenanceType(columnValue);
                continue;
            }
            if ("服务形式[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.serviceTypes);
                project.setServiceType(columnValue);
                continue;
            }
            if ("开始时间[必填]".equals(key)) {
                DateValidator.validate(key, columnValue);
                project.setServiceStartTime(columnValue);
                continue;
            }
            if ("结束时间[必填]".equals(key)) {
                DateValidator.validate(key, columnValue);
                project.setServiceEndTime(columnValue);
                continue;
            }
            if ("服务供应商[必填]".equals(key)) {
                if(this.produces.containsKey(columnValue)) {
                    Map<String,String> mp = (Map<String,String>) this.produces.get(columnValue);
                    project.setMaintenProduceName(mp.get("maintenName"));
                    project.setMaintenProducePhone(mp.get("maintenPhone"));
                    project.setMaintenProduceEmail(mp.get("maintenEmail"));
                } else {
                    Set<String> s = this.produces.keySet();
                    throw new RuntimeException("列["+key+"]值[" + columnValue + "]不在可选范围. 可选值["+ JSONArray.toJSONString(s) +"]");
                }
                project.setMaintenProduce(columnValue);
                continue;
            }
            if ("服务联系人名称".equals(key)) {
                project.setMaintenProduceName(columnValue);
                continue;
            }
            if ("服务联系人邮箱".equals(key)) {
                FormateValidator.isEmailFormate(key,columnValue);
                project.setMaintenProduceEmail(columnValue);
                continue;
            }
            if ("服务联系人电话".equals(key)) {
                FormateValidator.isPhoneFormate(key,columnValue);
                project.setMaintenProducePhone(columnValue);
                continue;
            }
            if("设备区域[必填]".equals(key)) {
                if(columnValue.indexOf("，") !=  -1) {
                    columnValue = columnValue.replace('，',',');
                }
                String[] strArray = columnValue.split(",");
                for(int i=0; i<strArray.length;i++) {
                    DictValidator.validator(key, strArray[i], this.deviceAreas);
                }
                project.setDeviceArea(columnValue);
                continue;
            }
            if("维保对象类型[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.maintenanceProjectTypes);
                project.setMaintenanceProjectType(columnValue);
                continue;
            }
            if("采购类型[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.procureTypes);
                project.setProcureType(columnValue);
                continue;
            }
            if("金额(万)".equals(key)) {
                // 小数点只取6位
                int spotIndex = columnValue.indexOf(".");
                if(spotIndex != -1 && columnValue.substring(spotIndex+1).length() > 6) {
                    String tmpStr = columnValue.substring(0,spotIndex+7);
                    newMoney = new BigDecimal(tmpStr);
                } else {
                    newMoney = "".equals(columnValue) ? null: new BigDecimal(columnValue);
                }
				// hangfang 2020.07.30 null引用
                if (newMoney != null) {
                    project.setMoney(newMoney);
                }
                continue;
            }
            if("设备类型".equals(key)) {
                if(!"".equals(columnValue)) {
                    DictValidator.validator(key, columnValue, this.deviceTypes);
                    project.setDeviceType(columnValue);
                }
                continue;
            }
            if("税前金额(万)".equals(key)) {
                // 小数点只取6位
                int spotIndex = columnValue.indexOf(".");
                if(spotIndex != -1 && columnValue.substring(spotIndex+1).length() > 6) {
                    String tmpStr = columnValue.substring(0,spotIndex+7);
                    newMoney = new BigDecimal(tmpStr);
                } else {
                    newMoney = "".equals(columnValue) ? null: new BigDecimal(columnValue);
                }
                if (newMoney != null) {
                    project.setPreTax(newMoney);
                }
                continue;
            }
            if("税率(%)".equals(key)) {
                NumberValidator.validateWithScope(key,columnValue,1,100);
                project.setTaxRate(columnValue);
                continue;
            }
            if("税后金额(万)".equals(key)) {
                // 小数点只取6位
                int spotIndex = columnValue.indexOf(".");
                if(spotIndex != -1 && columnValue.substring(spotIndex+1).length() > 6) {
                    String tmpStr = columnValue.substring(0,spotIndex+7);
                    newMoney = new BigDecimal(tmpStr);
                } else {
                    newMoney = "".equals(columnValue) ? null: new BigDecimal(columnValue);
                }
                if (newMoney != null) {
                    project.setAfterTax(newMoney);
                }
                continue;
            }
            if("单价(万)".equals(key)) {
                // 小数点只取6位
                int spotIndex = columnValue.indexOf(".");
                if(spotIndex != -1 && columnValue.substring(spotIndex+1).length() > 6) {
                    String tmpStr = columnValue.substring(0,spotIndex+7);
                    newMoney = new BigDecimal(tmpStr);
                } else {
                    newMoney = "".equals(columnValue) ? null: new BigDecimal(columnValue);
                }
                if (newMoney != null) {
                    project.setUnitPrice(newMoney);
                }
                continue;
            }
            if("总价(万)".equals(key)) {
                // 小数点只取6位
                int spotIndex = columnValue.indexOf(".");
                if(spotIndex != -1 && columnValue.substring(spotIndex+1).length() > 6) {
                    String tmpStr = columnValue.substring(0,spotIndex+7);
                    newMoney = new BigDecimal(tmpStr);
                } else {
                    newMoney = "".equals(columnValue) ? null: new BigDecimal(columnValue);
                }
                if (newMoney != null) {
                    project.setTotalPrice(newMoney);
                }
                continue;
            }
            if("结算方式".equals(key)) {
                DictValidator.validator(key, columnValue, this.payMethods);
                project.setPayMethod(columnValue);
                continue;
            }
            if("折扣钱金额(万)".equals(key)) {
                // 小数点只取6位
                int spotIndex = columnValue.indexOf(".");
                if(spotIndex != -1 && columnValue.substring(spotIndex+1).length() > 6) {
                    String tmpStr = columnValue.substring(0,spotIndex+7);
                    newMoney = new BigDecimal(tmpStr);
                } else {
                    newMoney = "".equals(columnValue) ? null: new BigDecimal(columnValue);
                }
                if (newMoney != null) {
                    project.setDiscountAmount(newMoney);
                }
                continue;
            }
            if("折扣率(%)".equals(key)) {
                NumberValidator.validateWithScope(key,columnValue,1,100);
                project.setDiscountRate(columnValue);
                continue;
            }
            if("上期维保项目名称".equals(key)) {
                if(!"".equals(columnValue) && columnValue != null) {
                    CmdbMaintenanceProject queryProject = new CmdbMaintenanceProject();
                    queryProject.setProjectName(columnValue);
                    CmdbMaintenanceProject rtObj = this.projectService.get(queryProject);
                    if(rtObj == null) {
                        throw new RuntimeException("维保项目：‘" + columnValue + "’不存在");
                    }
                    project.setQuarterFlag(rtObj.getQuarterFlag());
                }
                continue;
            }
        }
        // 判断新增还是修改
        CmdbMaintenanceProject queryProject = new CmdbMaintenanceProject();
        queryProject.setProjectName(project.getProjectName());
        CmdbMaintenanceProject dbProject = this.projectService.get(queryProject);
        String projectId = null;
        if (dbProject != null) {
            projectId = dbProject.getId();
            project.setId(projectId);
            project.setQuarterFlag(dbProject.getQuarterFlag());
        }
        try {
            this.projectService.save(project);
        } catch (Exception e) {
            throw new RuntimeException("新增或更新维保项目失败:" + e.getMessage(), e);
        }
    }
}
