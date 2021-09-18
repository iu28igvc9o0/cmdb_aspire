package com.aspire.ums.bills.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司 FileName: CmdbFeignClient Author: zhu.juwang Date: 2020/7/22 14:46 Description: CMDB服务的 Feign
 * Client 类 History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
// @FeignClient(value = "CMDB", url = "${feign.cmdb}")
@FeignClient(value = "CMDB", url = "http://10.1.203.100:2222")
public interface CmdbFeignClient {

    /**
     * CI业务系统配额模型数据查询
     * 
     * @return
     */
    @RequestMapping(value = "/v1/cmdb/module/businessQuota/list", method = RequestMethod.GET)
    List<Map<String, Object>> getAllBusinessQuotaInfo();

    /**
     *  CI需要计费业务系统配额模型数据查询
     * @return
     */
    @RequestMapping(value = "/v1/cmdb/module/businessQuota/listNeedCharge", method = RequestMethod.GET)
    List<Map<String, Object>> getNeedChargeBusinessQuotaInfo();
    /**
     * 获取字典值列表
     * 
     * @param type
     *            字典类型
     * @param pid
     *            父字典ID
     * @param pValue
     *            父字典值
     * @param pType
     *            父字典类型
     * @return 字典值列表
     */
    @GetMapping("/cmdb/configDict/getDictsByType")
    List<Map<String, Object>> getDictsByType(@RequestParam("type") String type,
            @RequestParam(value = "pid", required = false) String pid,
            @RequestParam(value = "pValue", required = false) String pValue,
            @RequestParam(value = "pType", required = false) String pType);

    /**
     * 获取部门信息
     * @param
     * @return
     */
    @GetMapping("/cmdb/orgManager/getById")
    Map<String, Object> getDepartmentById(@RequestParam("id") String id);
}
