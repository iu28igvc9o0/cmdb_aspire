package com.migu.tsg.microservice.atomicservice.composite.service.logs;

import com.aspire.mirror.common.entity.PageResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 系统日志服务类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.theme
 * 类名称:    ICompSysLogService.java
 * 类描述:    系统日志暴露接口
 * 创建人:    JinSu
 * 创建时间:  2019/6/14 10:05
 * 版本:      v1.0
 */
@Api(value = "主题管理")
@RequestMapping("${version}/log")
public interface ICompSysLogService {

    @PostMapping(value = "/getLogData", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取日志数据", notes = "获取日志数据", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功",responseContainer = "PageResponse", response = CompSysLogResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompSysLogResponse> getLogData(@RequestBody CompSysLogSearchRequest request);

    @PostMapping(value = "/exportSysLog", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "导出日志数据", notes = "导出日志数据", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportSysLog(@RequestBody SysLogExportRequest request, HttpServletResponse response);

    @PostMapping(value = "/createLogFilterRule", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "创建日志过滤规则", notes = "创建日志过滤规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompCreateLogFilterRuleResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompCreateLogFilterRuleResp createLogFilterRule(@RequestBody CompCreateLogFilterRuleReq request);

    @GetMapping(value = "/getLogFilterRuleDetail", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取日志过滤规则详情", notes = "获取日志过滤规则详情", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompLogFilterRuleDetail.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompLogFilterRuleDetail getLogFilterRuleDetail(@RequestParam("uuid") String uuid);

    @GetMapping(value = "/getLogFilterRuleList", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取日志过滤规则列表", notes = "获取日志过滤规则列表", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompLogFilterRuleDetail.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CompLogFilterRuleDetail> getLogFilterRuleList(
            @RequestParam(value = "ruleType",required = false) String ruleType);

    @DeleteMapping(value = "/deleteLogFilterRule")
    @ApiOperation(value = "删除日志过滤规则", notes = "删除日志过滤规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String deleteLogFilterRule(@RequestParam("uuid") String uuid);

    @GetMapping(value = "/getLogDataByFilterRule", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "根据过滤规则获取日志数据", notes = "根据过滤规则获取日志数据", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功",responseContainer = "PageResponse", response = CompSysLogResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompSysLogResponse> getLogDataByFilterRule(@RequestParam("uuid") String uuid,
                                                            @RequestParam("pageNo") String pageNo,
                                                            @RequestParam("pageSize") String pageSize);

    @PostMapping(value = "/updateLogFilterRule", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "更新日志过滤规则", notes = "更新日志过滤规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String updateLogFilterRule(@RequestBody CompCreateLogFilterRuleReq request);

    @PostMapping(value = "/createLogAlertRule", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "创建日志告警规则", notes = "创建日志告警规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompCreateLogFilterRuleResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompCreateLogFilterRuleResp createLogAlertRule(@RequestBody CompCreateLogAlertRuleReq request);

    @GetMapping(value = "/getLogAlertRuleDetail", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取日志告警规则详情", notes = "获取日志告警规则详情", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompLogAlertRuleDetail.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompLogAlertRuleDetail getLogAlertRuleDetail(@RequestParam("uuid") String uuid);

    @PostMapping(value = "/getLogAlertRuleList", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取日志告警规则列表", notes = "获取日志告警规则列表", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompLogAlertRuleDetail.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompLogAlertRuleDetail> getLogAlertRuleList(@RequestBody CompLogAlertRuleListReq request);

    @DeleteMapping(value = "/deleteLogAlertRule")
    @ApiOperation(value = "删除日志告警规则", notes = "删除日志告警规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String deleteLogAlertRule(@RequestBody List<String> uuidList);

    @PostMapping(value = "/updateLogAlertRule", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "更新日志告警规则", notes = "更新日志告警规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String updateLogAlertRule(@RequestBody CompCreateLogAlertRuleReq request);

    @PostMapping(value = "/openLogAlertRule")
    @ApiOperation(value = "启用日志告警规则", notes = "启用日志告警规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String openLogAlertRule(@RequestBody List<String> uuidList);

    @PostMapping(value = "/closeLogAlertRule")
    @ApiOperation(value = "停用日志告警规则", notes = "停用日志告警规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String closeLogAlertRule(@RequestBody List<String> uuidList);

    @GetMapping(value = "/checkName", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "校验规则名称", notes = "校验规则名称", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String checkName(@RequestParam("ruleName") String ruleName);
}
