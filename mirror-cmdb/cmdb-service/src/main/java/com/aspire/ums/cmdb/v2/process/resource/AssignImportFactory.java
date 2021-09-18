package com.aspire.ums.cmdb.v2.process.resource;

import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.instance.payload.CmdbAssign;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbAssignService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import com.aspire.ums.cmdb.v2.process.validate.NumberValidator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/11/14
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Slf4j
public class AssignImportFactory extends ImportFactory {
    private ConfigDictService configDictService;
    private ICmdbAssignService assignService;
    private ModuleService moduleService;
    private List<Map<String, Object>> department1s;
    private List<Map<String, Object>> department2s;
    private List<Map<String, Object>> bizSystemList;
    private List<Map<String, String>> poolList;

    private List<Map<String, Object>> getBizSystemList(String department1,String department2) {
        bizSystemList = new ArrayList<>();
        Map<String, Object> queryParams = new HashMap<>();
        if (StringUtils.isNotEmpty(department1)) {
            queryParams.put("department1", department1);
        }
        if (StringUtils.isNotEmpty(department2)) {
            queryParams.put("department2", department2);
        }
        List<Map<String, Object>> businessList = moduleService.getModuleData(queryParams,null,"business");
        if (businessList != null && businessList.size() > 0) {
            businessList.forEach((data) -> {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("key", data.get("bizSystem"));
                dataMap.put("value", data.get("bizSystem"));
                bizSystemList.add(dataMap);
            });
        }
        return bizSystemList;
    }

    private List<Map<String, Object>> getDepartment1List() {
        if (department1s == null) {
            department1s = new ArrayList<>();
            String sql = "SELECT  id `id`, orgName `key`, orgName `value` FROM cmdb_org_system os WHERE parent_id='0' AND isdel='0'";
            department1s = super.getDictList(sql);
        }
        return department1s;
    }

    private List<Map<String, Object>> getDepartment2List(String parentId) {
        String sql = "SELECT c.orgName `key` ,c.orgName `value` FROM cmdb_org_system c WHERE c.parent_id='" + parentId + "' AND isdel='0'";
        department2s = super.getDictList(sql);
        return department2s;
    }

    private List<Map<String, String>> getPoolList() {
        poolList = new ArrayList<>();
        List<ConfigDict> dicts = getConfigDictService().selectDictsByType("idctype", null, null, null);
        dicts.forEach(dict -> {
            Map<String, String> dictMap = new HashMap<>();
            dictMap.put("key",dict.getName());
            dictMap.put("value", dict.getValue());
            poolList.add(dictMap);
        });
        return poolList;
    }

    @Override
    public void initSpringBean() {
        if (configDictService == null) {
            configDictService = SpringUtils.getBean(ConfigDictService.class);
        }
        if (assignService == null) {
            assignService = SpringUtils.getBean(ICmdbAssignService.class);
        }
        if (moduleService == null) {
            moduleService = SpringUtils.getBean(ModuleService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        CmdbAssign assign = new CmdbAssign();
        for (String key : dataMap.keySet()) {
            String columnValue = dataMap.get(key);
            if (key.contains("必填")){
                EmptyValidator.notEmpty(key, columnValue);
            }
            if ("资源池名称[必填]".equals(key)) {
                DictValidator.validator("资源池名称[必填]", dataMap.get("资源池名称[必填]"), getPoolList());
                assign.setResourcePool(columnValue);
            }
            if ("类型[必填]".equals(key)) {
                if (columnValue.length() > 40) {
                    throw new RuntimeException("列[" + key + "]长度超过限制，最大40");
                }
                assign.setType(columnValue);
            }
            if ("一级租户[必填]".equals(key)) {
                DictValidator.validator("一级租户[必填]", dataMap.get("一级租户[必填]"), getDepartment1List());
                assign.setDepartment1(columnValue);
            }
            if ("二级租户[必填]".equals(key)) {
                String parentId = "";
                for (Map d1 : department1s) {
                    if (d1.get("value").equals(assign.getDepartment1())) {
                        parentId = d1.get("id").toString();
                    }
                }
                DictValidator.validator("二级租户[必填]", dataMap.get("二级租户[必填]"), getDepartment2List(parentId));
                assign.setDepartment2(columnValue);
            }
            if ("业务系统[必填]".equals(key)) {
                DictValidator.validator("业务系统[必填]", dataMap.get("业务系统[必填]"), getBizSystemList(assign.getDepartment1(), assign.getDepartment2()));
                assign.setBizSystem(columnValue);
            }
            if ("租户资源需求[必填]".equals(key)) {
                NumberValidator.validatePositiveIntegerNotZero(key, columnValue);
                assign.setDepartNeedCount(Integer.parseInt(String.valueOf(columnValue)));
            }
            if ("已建设量[必填]".equals(key)) {
                NumberValidator.validatePositiveInteger(key, columnValue);
                assign.setBuiltCount(Integer.parseInt(String.valueOf(columnValue)));
            }
            if ("已分配量[必填]".equals(key)) {
                NumberValidator.validatePositiveInteger(key, columnValue);
                assign.setAssignedCount(Integer.parseInt(String.valueOf(columnValue)));
            }
            if ("预分配量[必填]".equals(key)) {
                NumberValidator.validatePositiveInteger(key, columnValue);
                assign.setPreAssignCount(Integer.parseInt(String.valueOf(columnValue)));
            }
            if ("未建设量[必填]".equals(key)) {
                NumberValidator.validatePositiveInteger(key, columnValue);
                assign.setUnBuiltCount(Integer.parseInt(String.valueOf(columnValue)));
            }
        }
        getAssignService().save(assign);
    }
}
