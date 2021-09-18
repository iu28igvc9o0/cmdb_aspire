package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.automate;

import com.aspire.ums.cmdb.common.ResultVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-09-15 11:34
 * @description
 */
@FeignClient(value = "CMDB")
public interface AutomateClient {

    @PostMapping(value = "/cmdb/autoMate/instance/create")
    ResultVo create(String param);

    @PostMapping(value = "/cmdb/autoMate/instance/getAutomateHostDetail")
    ResultVo getAutomateHostDetail(@RequestBody Map<String,String> param);

    @GetMapping(value = "/cmdb/autoMate/instance/getAutomateColumns")
    List<Map<String, Object>> getAutomateColumns();

    @GetMapping(value = "/cmdb/autoMate/instance/buildExportHeaderList")
    Map<String,List<String>> buildExportHeaderList();

    @PostMapping(value = "/cmdb/autoMate/instance/findAutomateConfList")
    List<Map<String, String>> findAutomateConfList(@RequestBody Map<String,Object> param);
}
