package com.aspire.mirror.composite.service.template;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.template.payload.*;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 监控项对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template
 * 类名称:    ICompItemsService.java
 * 类描述:    监控项对外暴露接口
 * 创建人:    JinSu
 * 创建时间:  2018/8/8 14:43
 * 版本:      v1.0
 */
@Api(value = "监控项信息管理")
@RequestMapping("${version}/items")
public interface ICompItemsService {
    /**
     * 创建监控项信息
     *
     * @param itemsCreateRequest 监控项创建请求对象
     * @return ItemsCreateResponse 监控项创建响应对象
     */
    @PostMapping(value = "/create")
    @ApiOperation(value = "创建监控项", notes = "创建监控项", response = CompItemsCreateResponse.class, tags = {"Items API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompItemsCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompItemsCreateResponse.class)})
    CompItemsCreateResponse createdItems(@RequestParam("resource_type") String resourceType, @RequestParam("action") String action, @RequestBody CompItemsCreateRequest itemsCreateRequest);

    /**
     * 批量创建监控项信息
     *
     * @param request 监控项批量创建请求对象
     * @return ItemsCreateResponse 监控项创建响应对象
     */
    @PostMapping(value = "/batchCreate", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "创建监控项", notes = "创建监控项", response = CompItemsCreateResponse.class, tags = {"Items API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = BaseResponse.class)})
    BaseResponse batchCreateItems(@RequestParam("resource_type") String resourceType, @RequestParam("action") String action, @RequestBody CompItemsBatchCreateRequest request);

    /**
     * 根据主键删除多条监控项信息
     *
     * @param itemIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/{item_ids}")
    @ApiOperation(value = "删除多条监控项信息", notes = "删除多条监控项信息",
            tags = {"Items API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    BaseResponse deleteByPrimaryKeyArrays(@PathVariable("item_ids") String itemIds, @RequestParam("resource_type") String resourceType, @RequestParam("action") String action);

    /**
     * 根据主键修改监控项信息
     *
     * @param itemsUpdateRequest 监控项修改请求对象
     * @return ItemsUpdateResponse 监控项修改响应对象
     */
    @PutMapping(value = "/{item_id}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "修改监控项", notes = "修改监控项", response = CompItemsUpdateResponse.class, tags = {"Items API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompItemsUpdateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompItemsUpdateResponse.class)})
    CompItemsUpdateResponse modifyByPrimaryKey(@PathVariable("item_id") String itemId, @RequestParam("resource_type") String resourceType, @RequestParam("action") String action, @RequestBody CompItemsUpdateRequest itemsUpdateRequest);

    /**
     * 根据主键查找监控项详情信息
     *
     * @param itemId 监控项主键
     * @return ItemsVO 监控项详情响应对象
     */
    @GetMapping(value = "/{item_id}")
    @ApiOperation(value = "详情", notes = "根据itemId获取监控项详情", tags = {"Items API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompItemsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompItemsDetailResponse findByPrimaryKey(@PathVariable("item_id") String itemId);

    /**
     * 根据page对象查询监控项
     *
     * @param request 分页查询监控对象
     * @return PageResponse<ItemsDetailResponse> page返回对象
     */
    @PostMapping(value = "/pageList")
    @ApiOperation(value = "分页查询", notes = "分页查询", tags = {"Items API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompItemsDetailResponse> pageList(@RequestBody CompItemsPageRequest request);


    /**
     * 查询关联监控项列表
     */
    @GetMapping(value = "/zbxItem/list")
    @ApiOperation(value = "查询列表", notes = "查询列表", tags = {"Items API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompMonitorDetailResponse> relationItemList(@RequestParam("room") String room, @RequestParam(value="name", required= false) String name, @RequestParam(value = "template_id", required = false) String templateId, @RequestParam(value = "key", required = false) String key);

    /**
     *获取计算主题指标结果
     */
    @GetMapping(value = "/getThemeCalcResult/{item_id}")
    @ApiOperation(value = "查询计算主题指标结果", notes = "查询计算主题指标结果", tags = {"Item API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getThemeCalcResult(@PathVariable("item_id") String itemId, @RequestParam(value = "start_time", required = false) String startTime, @RequestParam(value="end_time", required = false) String endTime);

}
