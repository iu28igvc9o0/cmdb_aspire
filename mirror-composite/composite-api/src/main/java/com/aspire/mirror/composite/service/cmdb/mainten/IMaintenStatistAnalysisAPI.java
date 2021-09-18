package com.aspire.mirror.composite.service.cmdb.mainten;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.maintenance.payload.MaintenStatistAnalysisRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Api(value = "维保统计分析接口类")
@RequestMapping("${version}/cmdb/mainten/statistic")
public interface IMaintenStatistAnalysisAPI {

    /**
     *  第一层接口查询
     * @return 模型列表
     */
    @PostMapping(value = "/first" )
    @ApiOperation(value = "第一层接口查询", notes = "第一层接口查询", tags = {"CMDB IMaintenStatistAnalysisAPI API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> firstLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request);

    /**
     *  第二层接口查询
     * @return 模型列表
     */
    @PostMapping(value = "/second" )
    @ApiOperation(value = "第二层接口查询", notes = "第二层接口查询", tags = {"CMDB IMaintenStatistAnalysisAPI API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> secondLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request);

    /**
     *  第三层接口查询
     * @return 模型列表
     */
    @PostMapping(value = "/third" )
    @ApiOperation(value = "第三层接口查询", notes = "第三层接口查询", tags = {"CMDB IMaintenStatistAnalysisAPI API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> thirdLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request);

    /**
     *  第四层接口查询
     * @return 模型列表
     */
    @PostMapping(value = "/fourth" )
    @ApiOperation(value = "第四层接口查询", notes = "第四层接口查询", tags = {"CMDB IMaintenStatistAnalysisAPI API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> fourthLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request);

    /**
     *  维保统计分析第一层导出
     * @return 模型列表
     */
    @PostMapping(value = "/export/first" )
    @ApiOperation(value = "维保统计分析第一层导出", notes = "维保统计分析导出", tags = {"CMDB IMaintenStatistAnalysisAPI API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject firstExport(@RequestBody(required = false) MaintenStatistAnalysisRequest request, HttpServletResponse response);

    /**
     *  维保统计分析第二层导出
     * @return 模型列表
     */
    @PostMapping(value = "/export/second" )
    @ApiOperation(value = "维保统计分析第二层导出", notes = "维保统计分析第二层导出", tags = {"CMDB IMaintenStatistAnalysisAPI API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject secondExport(@RequestBody(required = false) MaintenStatistAnalysisRequest request, HttpServletResponse response);

    /**
     *  维保统计分析第三层导出
     * @return 模型列表
     */
    @PostMapping(value = "/export/third" )
    @ApiOperation(value = "维保统计分析第三层导出", notes = "维保统计分析第三层导出", tags = {"CMDB IMaintenStatistAnalysisAPI API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject thirdExport(@RequestBody(required = false) MaintenStatistAnalysisRequest request, HttpServletResponse response);

    /**
     *  维保统计分析第四层导出
     * @return 模型列表
     */
    @PostMapping(value = "/export/fourth" )
    @ApiOperation(value = "维保统计分析第四层导出", notes = "维保统计分析第四层导出", tags = {"CMDB IMaintenStatistAnalysisAPI API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject fourthExport(@RequestBody(required = false) MaintenStatistAnalysisRequest request, HttpServletResponse response);

    /**
     *  维保周期分析
     * @return 模型列表
     */
    @PostMapping(value = "/periodAnalysis" )
    @ApiOperation(value = "维保周期分析", notes = "维保周期分析", tags = {"CMDB IMaintenStatistAnalysisAPI API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> maintenancePeriodAnalysis(@RequestBody MaintenStatistAnalysisRequest request);

    /**
     *  维保周期导出
     * @return 模型列表
     */
    @PostMapping(value = "/export/period" )
    @ApiOperation(value = "维保周期导出", notes = "维保周期导出", tags = {"CMDB IMaintenStatistAnalysisAPI API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject maintenPeriodExport(@RequestBody(required = false) MaintenStatistAnalysisRequest request, HttpServletResponse response);
}