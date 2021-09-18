package com.aspire.mirror.composite.service.leakScan;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.leakScan.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 告警暴露接口服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.alert
 * 类名称:    ICompAlertsService.java
 * 类描述:    告警暴露接口服务
 * 创建人:    Liangjun
 * 创建时间:  2019/7/20 16:57
 * 版本:      v1.0
 */
@Api(value = "告警信息管理")
@RequestMapping("/${version}/alerts/leak-scan/")
public interface ICompSecurityScanService {

    @PostMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全漏洞扫描-总览", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<CompLeakScanSummaryResponse> leakScanSummary(@RequestBody CompLeakScanSummaryRequest compRequest);

    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全漏洞扫描-导出", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    void export(@RequestBody CompLeakScanSummaryRequest compRequest, HttpServletResponse response);

    @PostMapping(value = "/report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全漏洞扫描-报告", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<CompLeakScanReportResponse> leakScanReports(@RequestBody CompLeakScanReportRequest compRequest);

    @GetMapping(value = "/record/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompLeakScanRecordResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompLeakScanRecordResponse.class)})
    CompLeakScanRecordResponse getLeakScanRecord(@PathVariable(value = "id") String id);

    @PutMapping(value = "/bpm/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void modifyBpmRepairStat(@PathVariable(value = "id") String id);
    
    @PostMapping(value = "/detailList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "漏洞扫描清单", notes = "漏洞扫描清单", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<CompLeakScanSummaryResponse> leakScanDetailByDate(@RequestBody CompLeakScanSummaryRequest request);
    
    
    @RequestMapping(value = "/exportDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全漏洞扫描-导出", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    void exportDetail(@RequestBody CompLeakScanSummaryRequest compRequest, HttpServletResponse response);

    @GetMapping(value = "/leaksRankDistribute", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "漏洞等级分布", notes = "漏洞等级分布", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    Map<String, Object> leaksRankDistribute(@RequestParam(value = "beginDate", required = false) String beginDate,
                                            @RequestParam(value = "endDate", required = false) String endDate) throws IllegalAccessException;
    
    @GetMapping(value = "/leakStatByBiz", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "业务系统漏洞数统计", notes = "漏洞等级分布", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<Map<String, Object>> leakStatByBiz(@RequestParam(value = "beginDate", required = false) String beginDate,
                                            @RequestParam(value = "endDate", required = false) String endDate) throws IllegalAccessException;

    @GetMapping(value = "/leakStatListByBiz", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "业务系统漏洞数统计列表", notes = "漏洞等级分布", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<Map<String, Object>> leakStatListByBiz(@RequestParam("beginDate") String beginDate
    		,@RequestParam("endDate") String endDate,@RequestParam(value="rankType"
    		,required=false) String rankType
    		,@RequestParam(value="begin",required=false) Integer begin
            ,@RequestParam(value="pageSize",required=false)Integer pageSize) throws IllegalAccessException;
    
    @GetMapping(value = "/leakTrend", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全漏洞趋势", notes = "漏洞等级分布", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<Map<String, Object>> leakTrend(@RequestParam(value = "beginDate",required = false) String beginDate,
                                        @RequestParam(value = "endDate",required = false) String endDate
                                        ,@RequestParam(value="begin",required=false) Integer begin
                                        ,@RequestParam(value="pageSize",required=false)Integer pageSize) throws IllegalAccessException;
    
    @GetMapping(value = "/leakSummary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全扫描总览", notes = "漏洞等级分布", tags = {"Leak Scan API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    Map<String, Object> leakSummary(@RequestParam(value = "beginDate",required = false) String beginDate,
                                    @RequestParam(value = "endDate",required = false) String endDate) throws IllegalAccessException;
    
    @RequestMapping(value = "/exportScanResult", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "安全扫描结果明细-导出", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    void exportScanResult(@RequestParam("beginDate") String beginDate
    		,@RequestParam("endDate") String endDate, HttpServletResponse response);
    
    @RequestMapping(value = "/exportleakStatListByBizResult", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "漏洞扫描清单导出", notes = "安全漏洞扫描", tags = {"Leak Scan API"})
    void exportleakStatListByBizResult(@RequestParam("beginDate") String beginDate
    		,@RequestParam("endDate") String endDate,@RequestParam(value="rankType"
    		,required=false) String rankType, HttpServletResponse response);

    @GetMapping(value = "/getLeakByBizSystem", produces = MediaType.APPLICATION_JSON_VALUE)
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
