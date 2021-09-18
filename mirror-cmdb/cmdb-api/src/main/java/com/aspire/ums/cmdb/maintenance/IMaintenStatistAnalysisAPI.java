package com.aspire.ums.cmdb.maintenance;

import com.aspire.ums.cmdb.maintenance.payload.MaintenStatistAnalysisRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Api(value = "维保统计分析接口类")
@RequestMapping("/cmdb/mainten/statistic")
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
     *  维保周期分析
     * @return 模型列表
     */
    @PostMapping(value = "/periodAnalysis" )
    @ApiOperation(value = "维保周期分析", notes = "维保周期分析", tags = {"CMDB IMaintenStatistAnalysisAPI API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> maintenancePeriodAnalysis(@RequestBody MaintenStatistAnalysisRequest request);
}
