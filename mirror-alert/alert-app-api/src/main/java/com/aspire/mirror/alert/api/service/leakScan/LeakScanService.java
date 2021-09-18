package com.aspire.mirror.alert.api.service.leakScan;

import com.aspire.mirror.alert.api.dto.leakScan.*;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 安全漏洞扫描
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.service
 * 类名称:    LeakScanService.java
 * 类描述:    告警服务
 * 创建人:    LiangJun
 * 创建时间:  2019/7/17 10:45
 * 版本:      v1.0
 */
public interface LeakScanService {

    @PostMapping(value = "/v1/alerts/leak-scan/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全漏洞扫描-总览", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<LeakScanSummaryResponse> leakScanSummary(@RequestBody LeakScanSummaryRequest request);

    @PostMapping(value = "/v1/alerts/leak-scan/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全漏洞扫描-导出", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<Map<String, Object>> export(@RequestBody LeakScanSummaryRequest request) throws IllegalAccessException;

    @PostMapping(value = "/v1/alerts/leak-scan/report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全漏洞扫描-报告", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<LeakScanReportResponse> leakScanReports(@RequestBody LeakScanReportRequest reportRequest);

    @GetMapping(value = "/v1/alerts/leak-scan/record/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = LeakScanRecordResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = LeakScanRecordResponse.class)})
    LeakScanRecordResponse getLeakScanRecord(@PathVariable(value = "id") String id);

    @PutMapping(value = "/v1/alerts/leak-scan/bpm/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全漏洞扫描-更新状态", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void modifyBpmRepairStat(@PathVariable(value = "id") String fileId);
    
    
    @PostMapping(value = "/v1/alerts/leak-scan/detailList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "漏洞扫描清单", notes = "漏洞扫描清单", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<LeakScanSummaryResponse> leakScanDetailByDate(@RequestBody LeakScanSummaryRequest request);
    
    @PostMapping(value = "/v1/alerts/leak-scan/exportDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "漏洞扫描清单-导出", notes = "漏洞扫描清单导出", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<Map<String, Object>> exportDetail(@RequestBody LeakScanSummaryRequest request) throws IllegalAccessException;

    @GetMapping(value = "/v1/alerts/leak-scan/leaksRankDistribute", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "漏洞等级分布", notes = "漏洞等级分布", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    Map<String, Object> leaksRankDistribute(@RequestParam(value = "beginDate", required = false) String beginDate,
                                            @RequestParam(value = "endDate", required = false) String endDate);
    
    @GetMapping(value = "/v1/alerts/leak-scan/leakStatByBiz", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "业务系统漏洞数统计", notes = "漏洞等级分布", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<Map<String, Object>> leakStatByBiz(@RequestParam(value = "beginDate", required = false) String beginDate,
                                            @RequestParam(value = "endDate", required = false) String endDate) ;

    @GetMapping(value = "/v1/alerts/leak-scan/leakStatListByBiz", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "业务系统漏洞数统计列表", notes = "漏洞等级分布", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<Map<String, Object>> leakStatListByBiz(@RequestParam("beginDate")String beginDate, 
			@RequestParam("endDate")String endDate, @RequestParam(value="rankType"
    		,required=false)String rankType,@RequestParam(value="begin"
    		,required=false) Integer begin,@RequestParam(value="pageSize"
    		,required=false)Integer pageSize) ;
    
    @GetMapping(value = "/v1/alerts/leak-scan/leakTrend", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全漏洞趋势", notes = "漏洞等级分布", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<Map<String, Object>> leakTrend(@RequestParam(value = "beginDate",required = false) String beginDate,
            @RequestParam(value = "endDate",required = false) String endDate
            ,@RequestParam(value="begin",required=false) Integer begin
            ,@RequestParam(value="pageSize",required=false)Integer pageSize) ;
    
    @GetMapping(value = "/v1/alerts/leak-scan/leakSummary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全扫描总览", notes = "漏洞等级分布", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    Map<String, Object> leakSummary(@RequestParam(value = "beginDate",required = false) String beginDate,
                                    @RequestParam(value = "endDate",required = false) String endDate);

    @GetMapping(value = "/leakByBizSystem", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(综合首页)业务系统漏洞数统计", notes = "(综合首页)业务系统漏洞数统计", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<Map<String, Object>> getLeakByBizSystem();

    @GetMapping(value = "/getLeakByIp", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(综合首页)设备漏洞数统计", notes = "(综合首页)设备漏洞数统计", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<Map<String, Object>> getLeakByIp();

    @GetMapping(value = "/getLeakByIdcType", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "(综合首页)资源池漏洞数统计", notes = "(综合首页)资源池漏洞数统计", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<Map<String,Object>> getLeakByIdcType();
}
