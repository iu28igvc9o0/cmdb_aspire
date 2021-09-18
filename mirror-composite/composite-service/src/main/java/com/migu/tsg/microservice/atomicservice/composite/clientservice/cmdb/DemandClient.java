package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.inspection.payload.InsertDemandEntity;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/13 14:34
 * 版本: v1.0
 */
@FeignClient(value = "CMDB")
public interface DemandClient {
    
    @RequestMapping(value = "/cmdb/demandManager/list", method = RequestMethod.POST)
    public JSONObject list(@RequestBody Map<String, Object> queryMap);

    @RequestMapping(value = "/cmdb/demandManager/get", method = RequestMethod.GET)
    public JSONObject get(@RequestParam("demandId") String demandId);

    @RequestMapping(value = "/cmdb/demandManager/list/header", method = RequestMethod.GET)
    public List<Map<String, Object>> getTableHeader();
    
    @RequestMapping(value = "/cmdb/demandManager/save", method = RequestMethod.POST)
    public String save(@RequestBody InsertDemandEntity insertDemandEntity);
    
    @RequestMapping(value = "/cmdb/demandManager/update", method = RequestMethod.PUT)
    public String update(@RequestBody InsertDemandEntity insertDemandEntity);
    
    @RequestMapping(value = "/cmdb/demandManager/demandTypeList", method = RequestMethod.GET)
    List<Map<String, Object>> getDemandTypeList();
    
    @RequestMapping(value = "/cmdb/demandManager/exportDemand", method = RequestMethod.POST)
    public JSONObject exportDemand (@RequestBody Map<String, Object> sendRequest);
    
}
