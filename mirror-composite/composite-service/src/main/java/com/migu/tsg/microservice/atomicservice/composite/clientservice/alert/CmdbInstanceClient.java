package com.migu.tsg.microservice.atomicservice.composite.clientservice.alert;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.ums.cmdb.instance.IInstanceAPI;

@FeignClient(value = "CMDB")
public interface CmdbInstanceClient extends IInstanceAPI {
}
