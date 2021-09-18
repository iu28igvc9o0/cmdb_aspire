package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.ums.cmdb.restful.alert.IAlertRestfulAPI;

@FeignClient(value = "CMDB")
public interface CmdbAlertRestfulClient extends IAlertRestfulAPI {
}
