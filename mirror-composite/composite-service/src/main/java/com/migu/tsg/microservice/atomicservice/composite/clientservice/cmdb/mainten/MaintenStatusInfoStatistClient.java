package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.MaintenStatusInfoStatistRequest;

/**
 * 类名称: MaintenStatusInfoStatistClient
 * 类描述: 维保状态信息统计
 * 创建人: luowenbo
 * 创建时间: 2019/12/24 17:41
 * 版本: v1.0
 */

@FeignClient(value = "CMDB")
public interface MaintenStatusInfoStatistClient {

    @PostMapping("/cmdb/mainten/statistic/statusInfo")
    List<Map<String,Object>> statistMaintenStatusInfo(@RequestBody MaintenStatusInfoStatistRequest request);

    @PostMapping("/cmdb/mainten/statistic/maintenList")
    Result<Map<String,Object>> getMaintenProjectList(@RequestBody MaintenStatusInfoStatistRequest request);
}
