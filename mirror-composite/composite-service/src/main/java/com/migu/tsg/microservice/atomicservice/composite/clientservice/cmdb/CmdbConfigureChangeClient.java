package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import com.aspire.mirror.composite.service.cmdb.payload.CompConfigureChangeRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.ConfigureChangeResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;


@FeignClient( "CMDB")	
public interface CmdbConfigureChangeClient {
    @PostMapping(value = "/v1/cmdb/configureChange/selectConfigureChange")
    List<Map<String, Object>> selectConfigureChange(@RequestBody CompConfigureChangeRequest  compConfigureChangeRequest );
}
