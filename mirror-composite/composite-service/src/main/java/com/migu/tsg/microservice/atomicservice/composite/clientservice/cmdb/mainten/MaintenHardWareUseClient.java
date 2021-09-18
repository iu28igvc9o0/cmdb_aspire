package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUse;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUseRequest;

/**
 * 类名称: MaintenHardWareUseClient
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/8/4 13:41
 * 版本: v1.0
 */
@FeignClient(value = "CMDB")
public interface MaintenHardWareUseClient {

    @PostMapping("/cmdb/mainten/hardwareuse/addHardwareUse")
    Map<String, Object> addHardwareUse(@RequestBody HardwareUse hardwareUse);

    @PostMapping("/cmdb/mainten/hardwareuse/batchAddHardwareUse")
    Map<String, Object> batchAddHardwareUse(@RequestBody List<HardwareUse> list);

    @PostMapping("/cmdb/mainten/hardwareuse/updateHardwareUse")
    Map<String, Object> updateHardwareUse(@RequestBody HardwareUse hardwareUse);

    @PostMapping("/cmdb/mainten/hardwareuse/batchUpdateHardwareUse")
    Map<String, Object> batchUpdateHardwareUse(@RequestBody HardwareUse hardwareUseList);

    @PostMapping("/cmdb/mainten/hardwareuse/selectHardwareUseByPage")
    Result<HardwareUse> selectHardwareUseByPage(@RequestBody HardwareUseRequest request);

    @RequestMapping(value = "/cmdb/mainten/hardwareuse/export", method = RequestMethod.POST)
    JSONObject export(@RequestBody Map<String, Object> sendRequest);

    @RequestMapping(value = "/cmdb/mainten/hardwareuse/getHardwareTableList", method = RequestMethod.POST)
    List<Map<String,String>> getHardwareTableList();

    @DeleteMapping(value = "/cmdb/mainten/hardwareuse/deleteHardwareUse")
    Map<String,Object> deleteHardwareUse(@RequestParam("id") String id);
}
