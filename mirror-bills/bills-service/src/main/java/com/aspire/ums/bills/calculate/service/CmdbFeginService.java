package com.aspire.ums.bills.calculate.service;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBusinessQuotaService
 * @description: 业务系统配额模型
 * @author: luowenbo
 * @create: 2020-08-04 14:30
 **/
public interface CmdbFeginService {

    /**
     * 获取业务系统配额信息
     * @return
     */
    List<Map<String,Object>> getAllBusinessQuotaInfo();

    /**
     * 获取需要计费业务系统配额信息
     * @return
     */
    List<Map<String, Object>> getNeedChargeBusinessQuotaInfo();

    List<Map<String,Object>> getBillsDeviceTypes(String parentValue);

    List<Map<String,Object>> getResourceTypes();

    /**
     * 获取内部租户的标识
     * @return
     */
    String getInnerDepartmentFlag();
    /**
     * 获取CI详情
     */
    Map<String, Object> getInstanceDetail( String moduleId,String instanceId);

    Map<String, Object> getConfigByCode(String configCode);

    Map<String, Object> getModuleColumns(String moduleId);

}
