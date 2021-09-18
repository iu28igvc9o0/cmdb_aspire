package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.ums.cmdb.allocate.IAllocateIpConfigAPI;

@FeignClient(value = "CMDB")
public interface CmdbAllocateIpConfigClient extends IAllocateIpConfigAPI {
}
