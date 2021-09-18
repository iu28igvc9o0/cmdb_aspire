package com.aspire.cmdb.agent.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.Result;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Map;

@FeignClient(value = "CMDB")
// @FeignClient(value = "CMDB",url = "http://10.12.70.39:2222/")
public interface CmdbServiceClient {

    @RequestMapping(value = "/cmdb/restful/common/instance/statistics", method = RequestMethod.POST)
    Map<String,Object> getInstanceStatistics(@RequestBody Map<String, Object> params);
 
}
