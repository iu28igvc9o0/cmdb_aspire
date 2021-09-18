package com.aspire.mirror.template.api.service;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.template.api.dto.*;
import com.aspire.mirror.template.api.dto.vo.DynamicModelBatchCreateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * triggers对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.service
 * 类名称:    TriggersService.java
 * 类描述:    triggers对外暴露接口层
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Api(value = "triggers")
public interface TriggersService {
    /**
     * 批量创建触发器信息
     *
     * @param triggersCreateRequest 批量创建请求对象
     * @return TriggersCreateResponse 触发器创建响应对象
     */
    @PostMapping(value = "/v1/triggers/batchCreate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建triggers", notes = "创建triggers", response = TriggersCreateResponse.class, tags =
            {"/v1/triggers"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = TriggersCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = TriggersCreateResponse.class)})
    List<TriggersCreateResponse> batchCreatedTriggers(@RequestBody TriggersBatchCreateRequest triggersCreateRequest);
//    /**
//     * 创建触发器信息
//     *
//     * @param triggersCreateRequest 创建请求对象
//     * @return TriggersCreateResponse triggers创建响应对象
//     */
//    @PostMapping(value="/v1/triggers/create", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value="创建triggers",notes="创建triggers",response=TriggersCreateResponse.class,tags={ "/v1/triggers" })
//    @ApiResponses(value = {
//    @ApiResponse(code = 200, message = "返回", response =  TriggersCreateResponse.class),
//    @ApiResponse(code = 500, message = "Unexpected error", response = TriggersCreateResponse.class) })
//    TriggersCreateResponse createdTriggers(@RequestBody TriggersCreateRequest triggersCreateRequest);

    /**
     * 根据模板删除多条触发器信息
     *
     * @param itemIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/triggers/deleteByItemIdArrays/{item_ids}")
    @ApiOperation(value = "删除多条triggers信息", notes = "删除多条触发器信息",
            tags = {"/v1/triggers"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByItemIdArrays(@PathVariable("item_ids") String itemIds);

//    /**
//     * 根据主键删除单条triggers信息
//     *
//     * @param triggerId 主键
//     * @@return Result 返回结果
//     */
    @DeleteMapping(value = "/v1/triggers/{trigger_id}")
    @ApiOperation(value = "删除单条triggers信息", notes = "删除单条triggers信息",
    tags = {"/v1/triggers"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("trigger_id") String triggerId);

//    /**
//     * 根据主键修改triggers信息
//     *
//     * @param triggersUpdateRequest triggers修改请求对象
//     * @return TriggersUpdateResponse triggers修改响应对象
//     */
//    @PutMapping(value="/v1/triggers/{trigger_id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value="修改triggers",notes="修改触发器",response=TriggersUpdateResponse.class,tags={ "/v1/triggers" })
//    @ApiResponses(value = {
//    @ApiResponse(code = 200, message = "返回", response =  TriggersUpdateResponse.class),
//    @ApiResponse(code = 500, message = "Unexpected error", response = TriggersUpdateResponse.class) })
//    TriggersUpdateResponse modifyByPrimaryKey(@PathVariable("trigger_id") String triggerId, @RequestBody
// TriggersUpdateRequest triggersUpdateRequest);

    /**
     * 根据主键查找触发器详情信息
     *
     * @param triggerId 触发器主键
     * @return TriggersVO 触发器详情响应对象
     */
    @GetMapping(value = "/v1/triggers/{trigger_id}")
    @ApiOperation(value = "详情", notes = "根据triggerId获取触发器详情", tags = {"/v1/triggers"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = TriggersDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    TriggersDetailResponse findByPrimaryKey(@PathVariable("trigger_id") String triggerId);

    /**
     * 根据主键查询触发器集合信息
     *
     * @param triggerIds 触发器主键(多个以逗号分隔)
     * @return List<TriggersVO> 触发器查询响应对象
     */
    @GetMapping(value = "/v1/triggers/list/{item_ids}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/triggers"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<TriggersDetailResponse> listByItemIdArrays(@PathVariable("item_ids") String triggerIds);

    /**
     * 根据模板查询触发器列表
     *
     * @param templateId 模板ID
     * @return List<TriggersVO> 触发器查询响应对象
     */
    @GetMapping(value = "/v1/triggers/listByTemplateId/{template_id}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/triggers"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<TriggersDetailResponse> listByTemplateId(@PathVariable("template_id") String templateId);


    @PutMapping(value="/v1/triggers/updateExpressionByItemId", produces = MediaType.APPLICATION_JSON_VALUE)
    void updateExpressionByItemId(@RequestBody TriggersUpdateRequest triggersUpdateRequest);

    @PostMapping(value = "/v1/triggers/batchDynamicModelData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建动态阈值数据", notes = "创建动态阈值数据", tags = {"/v1/triggers"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    GeneralResponse batchDynamicModelData(@RequestBody DynamicModelBatchCreateVO dynamicModelBatchCreateVO);

    @PostMapping(value = "/v1/triggers/zabbixItemAndPrototypeRelationSync", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "zabbix自动发现指标与指标模型关系上报", notes = "zabbix自动发现指标与指标模型关系上报", tags = {"triggers API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    GeneralResponse zabbixItemAndPrototypeRelationSync(@RequestBody ItemSyncRequest itemMap);
}
