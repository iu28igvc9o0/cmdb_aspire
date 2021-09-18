package com.aspire.mirror.composite.service.opsmanage;

import com.aspire.mirror.ops.api.domain.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 敏感指令封装层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.opsmanage
 * 类名称:    ICompSensitiveService.java
 * 类描述:    敏感指令封装层
 * 创建人:    JinSu
 * 创建时间:  2020/2/11 13:51
 * 版本:      v1.0
 */
@Api(value = "敏感指令服务")
@RequestMapping(value = "/v1/ops-service/sensitive/")
public interface ICompSensitiveService {
    @PostMapping(value = "/querySensitiveConfigList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询列表", notes = "查询列表", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsScript.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<SensitiveConfig> querySensitiveConfigList(@RequestBody SensitiveConfigQueryModel queryParam);

    @PostMapping(value = "/saveSensitiveConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存敏感指令", notes = "保存敏感指令", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse saveSensitiveConfig(@RequestBody SensitiveConfig sensitiveConfig);

    @GetMapping(value = "/getSensitiveConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据id查询敏感指令", notes = "根据id查询敏感指令", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SensitiveConfig.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    SensitiveConfig querySensitiveConfigById(@RequestParam("sensitiveConfigId") Long sensitiveConfigId);


    @DeleteMapping(value = "/removeSensitiveConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除敏感指令", notes = "删除敏感指令", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse removeSensitiveConfig(@RequestParam("sensitiveConfigId") Long sensitiveConfigId);

    @PostMapping(value = "/checkScriptSensitivity", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "检查脚本是否包含敏感指令", notes = "检查脚本是否包含敏感指令", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse checkScriptSensitivity(@RequestBody CheckScriptRequst checkScriptRequst);

    @PostMapping(value = "/querySensitiveReviewList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "审核列表", notes = "审核列表", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<SensitiveReviewPageResponse> querySensitiveReviewList(@RequestBody SensitiveReviewQueryModel queryParam);

    @PutMapping(value = "/reviewInstance", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "审核敏感指令数据", notes = "审核敏感指令数据", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse reviewInstance(@RequestParam("review_ids") String reviewIds, @RequestParam("review_status") Integer reviewStatus);

    @PutMapping(value = "/updateStatusByRuleId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改规则状态", notes = "修改规则状态", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse updateStatusByRuleId(@RequestParam("status") Integer status, @RequestParam("sensitiveRuleId") Long sensitiveRuleId);

    @GetMapping(value = "/getSensitiveReviewByPipelineInstanceId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据作业实例ID查询敏感指令审核数据", notes = "根据作业实例ID查询敏感指令审核数据", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SensitiveConfig.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<SensitiveReview> querySensitiveReviewByPipelineInstanceId(@RequestParam("pipelineInstanceId") Long pipelineInstanceId);


    @PutMapping(value = "/updateObjectStatusByWhiteId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改白名单状态", notes = "修改白名单状态", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse updateObjectStatusByWhiteId(@RequestParam("status") Integer status, @RequestParam("sensitiveRuleWhiteId") Long sensitiveRuleWhiteId);

    @PostMapping(value = "/querySensitiveRuleWhiteList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "白名单列表", notes = "白名单列表", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SensitiveReviewPageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<SensitiveRuleWhite> querySensitiveRuleWhiteList(@RequestBody SensitiveRuleWhiteQueryModel queryParam);

    @PostMapping(value = "/querySensitiveLevelList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "敏感指令级别列表", notes = "敏感指令级别列表", response = PageListQueryResult.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SensitiveLevel.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<SensitiveLevel> querySensitiveLevelList(@RequestBody SensitiveLevelQueryModel queryParam);

    @PutMapping(value = "/updateSensitiveLevelById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改敏感指令级别", notes = "修改敏感指令级别", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse updateSensitiveLevelById(@RequestBody SensitiveLevel sensitiveLevel);

    @GetMapping(value = "/getSensitiveRuleListByPipelineId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据作业Id获取指标规则列表", notes = "根据作业Id获取指标规则列表", response = GeneralResponse.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<SensitiveRule> getSensitiveRuleListByPipelineId(@RequestParam("pipelineId") String pipelineId);

    @PostMapping(value = "/querySensitiveRuleList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "敏感指令规则列表", notes = "敏感指令规则列表", response = PageListQueryResult.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SensitiveRule.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<SensitiveRule> querySensitiveRuleList(@RequestBody SensitiveRuleQueryModel sensiTiveRuleQueryModel);

    @PostMapping(value = "/batchCreateSensitiveRuleWhite", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "批量添加敏感指令规则白名单", notes = "批量添加敏感指令规则白名单", response = PageListQueryResult.class, tags = {"Sensitive service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SensitiveRule.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse batchCreateSensitiveRuleWhite(@RequestBody SensitiveRuleWhiteBatchCreateReq sensitiveRuleWhiteBatchCreateReq);
}
