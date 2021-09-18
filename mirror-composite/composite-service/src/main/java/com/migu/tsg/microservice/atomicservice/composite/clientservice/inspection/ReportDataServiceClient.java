package com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection;

import com.aspire.mirror.composite.service.inspection.payload.CompReportDetailResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompReportResponse;
import com.aspire.mirror.inspection.api.dto.model.ReportDataWrap;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 报告数据获取
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection
 * 类名称:    ReportDataServiceClient.java
 * 类描述:    报告数据获取
 * 创建人:    JinSu
 * 创建时间:  2018/8/31 10:44
 * 版本:      v1.0
 */
@FeignClient(value = "INSPECTION-SERVICE")
public interface ReportDataServiceClient {
    @GetMapping(value="v1/report_data/{reportId}")
    @ApiOperation(value = "巡检报表数据获取接口", notes = "巡检报表数据获取接口")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    CompReportResponse retriveReportData(@PathVariable("reportId") String reportId);

    @GetMapping("v1/report_data/getBaseInfo/{reportId}")
    @ApiOperation(value = "获取报表数据基本信息", notes = "获取报表数据基本信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    CompReportResponse getBaseInfo(@PathVariable("reportId") String reportId);
}
