package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.ums.cmdb.maintenance.payload.CmdbIncrementStatistRequest;

/**
 * @ClassName CmdbIncrementStatistClient
 * @Description TODO
 * @Author luowenbo
 * @Date 2020/2/19 17:11
 * @Version 1.0
 */
@FeignClient(value = "CMDB")
public interface CmdbIncrementStatistClient {
    /*
     *  按照时间维度，来统计每个月的设备增量
     * */
    @PostMapping(value = "/cmdb/maintenance/statist/time" )
    List<Map<String,Object>> statistIncrementByTime(@RequestBody CmdbIncrementStatistRequest req);
}
