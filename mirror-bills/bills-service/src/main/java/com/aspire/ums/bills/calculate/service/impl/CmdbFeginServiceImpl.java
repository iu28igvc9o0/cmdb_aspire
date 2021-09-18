package com.aspire.ums.bills.calculate.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.bills.calculate.service.CmdbFeginService;
import com.aspire.ums.bills.client.CmdbFeignClient;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBusinessQuotaServiceImpl
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-04 14:32
 **/
@Service
public class CmdbFeginServiceImpl implements CmdbFeginService {
    @Autowired
    private CmdbFeignClient cmdbFeignClient;

    @Override
    public List<Map<String, Object>> getAllBusinessQuotaInfo() {
        return cmdbFeignClient.getAllBusinessQuotaInfo();
    }

    @Override
    public List<Map<String, Object>> getNeedChargeBusinessQuotaInfo() {
        return cmdbFeignClient.getNeedChargeBusinessQuotaInfo();
    }

    @Override
    public List<Map<String, Object>> getBillsDeviceTypes(String parentValue) {
        return cmdbFeignClient.getDictsByType("bills_device_type",null,parentValue,null);
    }

    @Override
    public List<Map<String, Object>> getResourceTypes() {
        return cmdbFeignClient.getDictsByType("resource_type",null,null,null);
    }

    @Override
    public String getInnerDepartmentFlag() {
        String innerDepartmentFlag = null;
        List<Map<String, Object>> dicts = cmdbFeignClient.getDictsByType("inner_department_flag",null,null,null);
        if(!CollectionUtils.isEmpty(dicts)) {
            Map<String, Object> dict = dicts.get(0);
            innerDepartmentFlag = dict.get("name").toString();
        }
        return innerDepartmentFlag;
    }

    @Override
    public Map<String, Object> getInstanceDetail(String moduleId, String instanceId) {
        return cmdbFeignClient.getInstanceDetail(moduleId, instanceId);
    }

    @Override
    public Map<String, Object> getConfigByCode(String configCode) {
        return cmdbFeignClient.getConfigByCode(configCode);
    }

    @Override
    public Map<String, Object> getModuleColumns(String moduleId) {
        return cmdbFeignClient.getModuleColumns(moduleId);
    }
}
