package com.aspire.mirror.log.api.service;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统日志服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.log.api.service
 * 类名称:    ISysLogService.java
 * 类描述:    系统日志服务
 * 创建人:    JinSu
 * 创建时间:  2019/6/14 14:59
 * 版本:      v1.0
 */
@Api(value = "系统日志接口")
public interface ISysLogService {
    @PostMapping(value = "/getLogData", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取日志数据", notes = "获取日志数据", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<SysLogResponse> getLogData(@RequestBody SysLogSearchRequest request);

    @PostMapping(value = "/createLogFilterRule", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "创建日志过滤规则", notes = "创建日志过滤规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CreateLogFilterRuleResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CreateLogFilterRuleResp createLogFilterRule(@RequestBody CreateLogFilterRuleReq request);

    @GetMapping(value = "/getLogFilterRuleDetail", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取日志过滤规则详情", notes = "获取日志过滤规则详情", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = LogFilterRuleDetail.class),
            @ApiResponse(code = 500, message = "内部错误")})
    LogFilterRuleDetail getLogFilterRuleDetail(@RequestParam("uuid") String uuid);

    @GetMapping(value = "/getLogFilterRuleList", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取日志过滤规则列表", notes = "获取日志过滤规则列表", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = LogFilterRuleDetail.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<LogFilterRuleDetail> getLogFilterRuleList(@RequestParam(value = "ruleType",required = false) String ruleType);

    @DeleteMapping(value = "/deleteLogFilterRule")
    @ApiOperation(value = "删除日志过滤规则", notes = "删除日志过滤规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void deleteLogFilterRule(@RequestParam("uuid") String uuid);

    @GetMapping(value = "/getLogDataByFilterRule", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "根据过滤规则获取日志数据", notes = "根据过滤规则获取日志数据", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功",responseContainer = "PageResponse", response = SysLogResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<SysLogResponse> getLogDataByFilterRule(@RequestParam("uuid") String uuid,
                                                        @RequestParam("pageNo") String pageNo,
                                                        @RequestParam("pageSize") String pageSize);

    @PostMapping(value = "/updateLogFilterRule", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "更新日志过滤规则", notes = "更新日志过滤规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void updateLogFilterRule(@RequestBody CreateLogFilterRuleReq request);

    @PostMapping(value = "/createLogAlertRule", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "创建日志告警规则", notes = "创建日志告警规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CreateLogFilterRuleResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CreateLogFilterRuleResp createLogAlertRule(@RequestBody CreateLogAlertRuleReq request);

    @GetMapping(value = "/getLogAlertRuleDetail", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取日志告警规则详情", notes = "获取日志告警规则详情", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = LogAlertRuleDetail.class),
            @ApiResponse(code = 500, message = "内部错误")})
    LogAlertRuleDetail getLogAlertRuleDetail(@RequestParam("uuid") String uuid);

    @PostMapping(value = "/getLogAlertRuleList", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取日志告警规则列表", notes = "获取日志告警规则列表", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = LogAlertRuleDetail.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<LogAlertRuleDetail> getLogAlertRuleList(@RequestBody LogAlertRuleListReq request);

    @DeleteMapping(value = "/deleteLogAlertRule")
    @ApiOperation(value = "删除日志告警规则", notes = "删除日志告警规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void deleteLogAlertRule(@RequestBody List<String> uuidList);

    @PostMapping(value = "/updateLogAlertRule", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "更新日志告警规则", notes = "更新日志告警规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void updateLogAlertRule(@RequestBody CreateLogAlertRuleReq request);

    @PostMapping(value = "/openLogAlertRule")
    @ApiOperation(value = "启用日志告警规则", notes = "启用日志告警规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void openLogAlertRule(@RequestBody List<String> uuidList);

    @PostMapping(value = "/closeLogAlertRule")
    @ApiOperation(value = "停用日志告警规则", notes = "停用日志告警规则", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void closeLogAlertRule(@RequestBody List<String> uuidList);

    @PostMapping(value = "/insertLogAlertLinked")
    @ApiOperation(value = "添加日志告警表数据", notes = "添加日志告警表数据", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void insertLogAlertLinked(@RequestBody CreateLogAlertLinkedReq request);

    @GetMapping(value = "/checkName", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "校验规则名称", notes = "校验规则名称", tags = {"SYSTEM LOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String checkName(@RequestParam("ruleName") String ruleName);
}
