package com.aspire.mirror.alert.server.clientservice;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.ums.cmdb.collectUnknown.ICollectUnkownAPI;

@FeignClient(value = "CMDB", url = "http://10.1.203.100:2222")
public interface CmdbCollectUnknowClient extends ICollectUnkownAPI {
}
