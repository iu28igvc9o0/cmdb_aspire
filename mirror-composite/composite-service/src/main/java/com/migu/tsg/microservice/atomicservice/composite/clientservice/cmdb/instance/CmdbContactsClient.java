package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.instance.payload.CmdbContactsResponse;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/2 18:19
 */
@FeignClient(value = "CMDB")
public interface CmdbContactsClient {

    @PostMapping(value = "/cmdb/addressLibrary/findContactsInfo")
    ResultVo<CmdbContactsResponse> findContactsInfo(@RequestBody Map<String, Object> param);

    @PostMapping(value = "/cmdb/addressLibrary/allocation")
    ResultVo<CmdbContactsResponse> allocation(@RequestBody Map<String, Object> param);
}
