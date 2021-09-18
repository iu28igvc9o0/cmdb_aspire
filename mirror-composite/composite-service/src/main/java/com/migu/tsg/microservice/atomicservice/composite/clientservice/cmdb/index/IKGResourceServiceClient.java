package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.ums.cmdb.index.KGResourceIndexAPI;

@FeignClient(value = "CMDB")
public interface IKGResourceServiceClient extends KGResourceIndexAPI {
}
