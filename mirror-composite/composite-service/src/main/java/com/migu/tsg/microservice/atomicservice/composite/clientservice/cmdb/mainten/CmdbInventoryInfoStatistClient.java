package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.CmdbInventoryInfoStatistRequest;

/**
 * @ClassName CmdbInventoryInfoStatistClient
 * @Description FeignClient
 * @Author luowenbo
 * @Date 2020/2/17 14:15
 * @Version 1.0
 */
@FeignClient(value = "CMDB")
public interface CmdbInventoryInfoStatistClient {

    /**
     *  页面第一层接口
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/inventory/first" )
    List<Map<String,Object>> firstLayer();

    /**
     *  页面第二层接口
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/inventory/second" )
    Result<Map<String,Object>> secondLayer(@RequestBody CmdbInventoryInfoStatistRequest req);

    /**
     *  页面第三层接口
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/inventory/third" )
    Result<Map<String,Object>> thirdLayer(@RequestBody CmdbInventoryInfoStatistRequest req);
}
