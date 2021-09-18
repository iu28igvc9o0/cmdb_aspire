package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.gvSafe;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.ums.cmdb.gvSafe.IGvSafeFaultAPI;

@FeignClient(value = "CMDB")
public interface GvSafeFaultClient extends IGvSafeFaultAPI {
}
