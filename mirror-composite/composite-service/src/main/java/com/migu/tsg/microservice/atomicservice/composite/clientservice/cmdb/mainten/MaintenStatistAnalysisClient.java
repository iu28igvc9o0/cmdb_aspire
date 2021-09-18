package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.ums.cmdb.maintenance.payload.MaintenStatistAnalysisRequest;

/**
 * 类名称: MaintenStatistAnalysisClient
 * 类描述: 维保统计分析
 * 创建人: luowenbo
 * 创建时间: 2019/11/15
 * 版本: v1.0
 */

@FeignClient(value = "CMDB")
public interface MaintenStatistAnalysisClient {

    @PostMapping("/cmdb/mainten/statistic/first")
    List<Map<String, Object> > firstLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request);

    @PostMapping("/cmdb/mainten/statistic/second")
    List<Map<String, Object> > secondLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request);

    @PostMapping("/cmdb/mainten/statistic/third")
    List<Map<String, Object> > thirdLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request);

    @PostMapping("/cmdb/mainten/statistic/fourth")
    List<Map<String, Object> > fourthLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request);

    @PostMapping("/cmdb/mainten/statistic/periodAnalysis")
    List<Map<String,Object>> maintenancePeriodAnalysis(@RequestBody MaintenStatistAnalysisRequest request);
}
