package com.aspire.mirror.inspection.api.service ;

import com.aspire.mirror.inspection.api.dto.ReportItemCallBackRequest;
import com.aspire.mirror.inspection.api.dto.model.InspectionCountQueryModel;
import com.aspire.mirror.inspection.api.dto.model.InspectionCountResp;
import com.aspire.mirror.inspection.api.dto.model.OpsTimeConsumeStatisticBase;
import com.aspire.mirror.inspection.api.dto.model.ReportDataWrap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
* 巡检报表api接口    <br/>
* Project Name:inspection-api
* File Name:ReportDataService.java
* Package Name:com.aspire.mirror.inspection.api.service
* ClassName: ReportDataService <br/>
* date: 2018年9月3日 下午3:55:39 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Api(value = "report_data_service")
public interface ReportDataService{
	
	@GetMapping(value="v1/report_data/{reportId}")
	@ApiOperation(value = "巡检报表数据获取接口", notes = "巡检报表数据获取接口")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
	ReportDataWrap retriveReportData(@PathVariable("reportId") String reportId);
	
    @PostMapping(value = "/v1/report_data/itemdata_callback")
    @ApiOperation(value = "巡检报表监控项数据回调", notes = "巡检报表监控项数据回调")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    void onReportItemDataCallBack(@RequestBody ReportItemCallBackRequest bizObj);

    @GetMapping("v1/report_data/getBaseInfo/{reportId}")
    @ApiOperation(value = "获取报表数据基本信息", notes = "获取报表数据基本信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    ReportDataWrap getBaseInfo(@PathVariable("reportId") String reportId);

    @PostMapping("v1/report_data/getInspectionCount")
    @ApiOperation(value = "获取巡检报告数", notes = "获取巡检报告数")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    InspectionCountResp getInspectionCount(@RequestBody InspectionCountQueryModel inspectionCountQueryModel);

    @PostMapping(value = "v1/report_data/getInspectionTimeStatistic", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "获取巡检耗时相关统计", notes = "获取巡检耗时相关统计")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误")})
    OpsTimeConsumeStatisticBase getInspectionTimeStatistic(@RequestBody Map<String,Object> param);
}
