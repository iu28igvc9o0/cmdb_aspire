package com.aspire.mirror.template.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.template.api.dto.ItemsBatchCreateRequest;
import com.aspire.mirror.template.api.dto.ItemsCreateRequest;
import com.aspire.mirror.template.api.dto.ItemsCreateResponse;
import com.aspire.mirror.template.api.dto.ItemsDetailResponse;
import com.aspire.mirror.template.api.dto.ItemsPageRequest;
import com.aspire.mirror.template.api.dto.ItemsUpdateRequest;
import com.aspire.mirror.template.api.dto.ItemsUpdateResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 监控项对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.service
 * 类名称:    ItemsService.java
 * 类描述:    监控项对外暴露接口层
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Api(value = "监控项")
public interface ItemsService {
    /**
     * 创建监控项信息
     *
     * @param itemsCreateRequest 监控项创建请求对象
     * @return ItemsCreateResponse 监控项创建响应对象
     */
    @PostMapping(value = "/v1/items/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建监控项", notes = "创建监控项", response = ItemsCreateResponse.class, tags = {"Item API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ItemsCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ItemsCreateResponse.class)})
    ItemsCreateResponse createdItems(@RequestBody ItemsCreateRequest itemsCreateRequest);

    /**
     * 批量创建监控项信息
     *
     * @param request 监控项批量创建请求对象
     * @return ItemsCreateResponse 监控项创建响应对象
     */
    @PostMapping(value = "/v1/items/batchCreate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建监控项", notes = "创建监控项", response = ItemsCreateResponse.class, tags = {"Item API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ItemsCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ItemsCreateResponse.class)})
    List<ItemsCreateResponse> batchCreateItems(@RequestBody ItemsBatchCreateRequest request);

    /**
     * 根据主键删除多条监控项信息
     *
     * @param itemIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/items/{item_ids}")
    @ApiOperation(value = "删除多条监控项信息", notes = "删除多条监控项信息",
            tags = {"Item API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("item_ids") String itemIds);

    /**
     * 根据主键修改监控项信息
     *
     * @param itemsUpdateRequest 监控项修改请求对象
     * @return ItemsUpdateResponse 监控项修改响应对象
     */
    @PutMapping(value = "/v1/items/{item_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改监控项", notes = "修改监控项", response = ItemsUpdateResponse.class, tags = {"Item API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ItemsUpdateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ItemsUpdateResponse.class)})
    ItemsUpdateResponse modifyByPrimaryKey(@PathVariable("item_id") String itemId, @RequestBody ItemsUpdateRequest
            itemsUpdateRequest);

    /**
     * 根据主键查找监控项详情信息
     *
     * @param itemId 监控项主键
     * @return ItemsVO 监控项详情响应对象
     */
    @GetMapping(value = "/v1/items/{item_id}")
    @ApiOperation(value = "详情", notes = "根据itemId获取监控项详情", tags = {"Item API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ItemsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ItemsDetailResponse findByPrimaryKey(@PathVariable("item_id") String itemId);

    /**
     * 根据page对象查询监控项
     *
     * @param request 分页查询监控对象
     * @return PageResponse<ItemsDetailResponse> page返回对象
     */
    @PostMapping(value = "/v1/items/pageList")
    @ApiOperation(value = "分页查询", notes = "分页查询", tags = {"Item API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<ItemsDetailResponse> pageList(@RequestBody ItemsPageRequest request);

    /**
     * 根据主键查询monitor_items集合信息
     *
     * @param itemIds monitor_items主键(多个以逗号分隔)
     * @return List<ItemsVO> monitor_items查询响应对象
     */
    @GetMapping(value = "/v1/items/listItem")
    @ApiOperation(value = "查询", notes = "查询", tags = {"Item API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ItemsDetailResponse> listByPrimaryKeyArrays(@RequestParam("item_ids") String itemIds);

    /**
     * 根据模板id,查询模板下的监控项. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param joinedTempIds
     * @return
     */
    @GetMapping(value = "/v1/items/listByTemplate/{joinedTempIds}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"Item API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ItemsDetailResponse> listItemsByTemplateIds(@PathVariable("joinedTempIds") String joinedTempIds);

    /**
     * 根据监控项ID和业务code编码查询最新的监控值
     * <p>
     *
     * @param itemId 监控项ID
     * @return sysCode 业务编码
     */
    @GetMapping(value = "/v1/items/findLastUpValueByItemId/{item_id}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"Item API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    String findLastUpValueByItemId(@PathVariable("item_id") String itemId, @RequestParam(value = "sys_code", required = false) String sysCode);

    /**
     *获取计算主题指标结果
     */
    @GetMapping(value = "/v1/items/getThemeCalcResult/{item_id}")
    @ApiOperation(value = "查询计算主题指标结果", notes = "查询计算主题指标结果", tags = {"Item API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getThemeCalcResult(@PathVariable("item_id") String itemId, @RequestParam(value = "start_time", required = false) String startTime, @RequestParam(value="end_time", required = false) String endTime);

    /**
     * 根据主题ID获取监控项列表
     * @param themeId 主题ID
     * @return List<ItemsDetailResponse> 监控项列表
     */
    @GetMapping(value = "/v1/items/listByThemeId/{theme_id}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"Item API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ItemsDetailResponse> selectByThemeId(@PathVariable("theme_id") String themeId);
}
