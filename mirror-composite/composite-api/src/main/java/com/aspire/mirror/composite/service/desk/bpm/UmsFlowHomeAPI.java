package com.aspire.mirror.composite.service.desk.bpm;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.mirror.composite.service.desk.bpm.payload.UmsFlowHomeInstParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @projectName: UmsFlowHomeAPI
 * @description: 接口
 * @author: tongzhihong
 * @create: 2020-11-24 10:37
 **/
@Api("desk_bpm")
@RequestMapping("${version}/bpm/runtime/umsHome/")
public interface UmsFlowHomeAPI {
	
    @RequestMapping(value = "getBpmInsData", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取用户实例数据-代办，已办，我发起", notes = "获取用户实例数据-代办，已办，我发起", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object getBpmInsData(@RequestBody UmsFlowHomeInstParam req);
    
    @RequestMapping(value = "getOfficialDocumentList", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取用户代办公告列表", notes = "获取用户代办公告列表", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object getOfficialDocumentList(@RequestBody UmsFlowHomeInstParam req);
    
    @RequestMapping(value = "getInstEfficiencyShow", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取效能展示数据", notes = "获取效能展示数据", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object getInstEfficiencyShow(@RequestBody UmsFlowHomeInstParam req);
    
    @RequestMapping(value = "getInstEfficiencyReport", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取效能报表数据--点击总数，运行中，关闭，超时", notes = "获取效能报表数据--点击总数，运行中，关闭，超时", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object getInstEfficiencyReport(@RequestBody UmsFlowHomeInstParam req);
    
    @RequestMapping(value = "getWorkTop", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取工单Top数据", notes = "获取工单Top数据", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object getWorkTop(@RequestBody UmsFlowHomeInstParam req);
    
    @RequestMapping(value = "getWorkAssessmentReport", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取工单考核数据", notes = "获取工单考核数据", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object getWorkAssessmentReport(@RequestBody UmsFlowHomeInstParam req);
    
    @RequestMapping(value = "getDepartmentInstCloseInfo", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取服务业务部部门闭单率", notes = "获取服务业务部部门闭单率", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object getDepartmentInstCloseInfo(@RequestBody UmsFlowHomeInstParam req);
    
    @RequestMapping(value = "instSearch", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "工单搜索", notes = "工单搜索", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object instSearch(@RequestBody UmsFlowHomeInstParam req);
    
    @RequestMapping(value = "alarmStatistics", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "告警统计分析", notes = "告警统计分析", tags = {"desk API "})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    public Object alarmStatistics(@RequestBody UmsFlowHomeInstParam req);
}
