package com.aspire.mirror.alert.server.clientservice;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.ums.cmdb.restful.common.ICommonRestfulAPI;

/**
 * @author baiwp
 * @title: CmdbInstanceClient
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/7/239:58
 */
@FeignClient(value = "CMDB", url = "http://10.1.203.100:2222")
public interface CmdbResfulClient extends ICommonRestfulAPI {
}
