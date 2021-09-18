package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.cmic;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.ResultVo;

/**
 * 
 * @author cuizhijun
 *
 */
@FeignClient(value = "CMDB")
public interface ICmdbIPManageOperationLogClient {

   
    @PostMapping("/cmdb/cmic/ip/operationLog/log")
    ResultVo<String> addOperationLog(@RequestParam("menuUrl") String menuUrl);


}
