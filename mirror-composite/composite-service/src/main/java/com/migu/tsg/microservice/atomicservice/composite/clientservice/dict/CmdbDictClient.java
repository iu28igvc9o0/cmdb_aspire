package com.migu.tsg.microservice.atomicservice.composite.clientservice.dict;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.ums.cmdb.dict.ICmdbDictAPI;

@FeignClient(value = "CMDB")
public interface CmdbDictClient extends ICmdbDictAPI {
}
