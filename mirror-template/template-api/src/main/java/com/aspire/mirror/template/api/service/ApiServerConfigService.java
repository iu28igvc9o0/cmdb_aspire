package com.aspire.mirror.template.api.service;

import com.aspire.mirror.template.api.dto.*;
import com.aspire.mirror.template.api.dto.vo.ApiServerConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * api_server_config对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.service
 * 类名称:    ApiServerConfigService.java
 * 类描述:    api_server_config对外暴露接口层
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:09
 */
@Api(value = "api_server_config")
public interface ApiServerConfigService {
    /**
     * 创建api_server_config信息
     *
     * @param apiServerConfigCreateRequest api_server_config创建请求对象
     * @return ApiServerConfigCreateResponse api_server_config创建响应对象
     */
    @PostMapping(value = "/v1/api_server_config/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建api_server_config", notes = "创建api_server_config", response =
            ApiServerConfigCreateResponse.class, tags = {"/v1/api_server_config"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ApiServerConfigCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ApiServerConfigCreateResponse.class)})
    ApiServerConfigCreateResponse createdApiServerConfig(@RequestBody ApiServerConfigCreateRequest
                                                                 apiServerConfigCreateRequest);

    /**
     * 根据主键删除单条api_server_config信息
     *
     * @param apiServerId 主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/api_server_config/{api_server_config}")
    @ApiOperation(value = "删除单条api_server_config信息", notes = "删除单条api_server_config信息",
            tags = {"/v1/api_server_config"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("api_server_id") String apiServerId);

    /**
     * 根据主键删除多条api_server_config信息
     *
     * @param apiServerIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/api_server_config/{api_server_id}")
    @ApiOperation(value = "删除多条api_server_config信息", notes = "删除多条api_server_config信息",
            tags = {"/v1/api_server_config"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("api_server_ids") String apiServerIds);

    /**
     * 根据主键修改api_server_config信息
     *
     * @param apiServerConfigUpdateRequest api_server_config修改请求对象
     * @return ApiServerConfigUpdateResponse api_server_config修改响应对象
     */
    @PutMapping(value = "/v1/api_server_config/{api_server_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改api_server_config", notes = "修改api_server_config", response =
            ApiServerConfigUpdateResponse.class, tags = {"/v1/api_server_config"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ApiServerConfigUpdateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ApiServerConfigUpdateResponse.class)})
    ApiServerConfigUpdateResponse modifyByPrimaryKey(@PathVariable("api_server_id") String apiServerId, @RequestBody
            ApiServerConfigUpdateRequest apiServerConfigUpdateRequest);

    /**
     * 根据主键查找api_server_config详情信息
     *
     * @param apiServerId api_server_config主键
     * @return ApiServerConfigVO api_server_config详情响应对象
     */
    @GetMapping(value = "/v1/api_server_config/{api_server_id}")
    @ApiOperation(value = "详情", notes = "根据apiServerId获取api_server_config详情", tags = {"/v1/api_server_config"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ApiServerConfigVO.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ApiServerConfigVO findByPrimaryKey(@PathVariable("api_server_id") String apiServerId);

    /**
     * 根据主键查询api_server_config集合信息
     *
     * @param apiServerIds api_server_config主键(多个以逗号分隔)
     * @return List<ApiServerConfigVO> api_server_config查询响应对象
     */
    @GetMapping(value = "/v1/api_server_config/list/{api_server_id}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/api_server_config"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ApiServerConfigVO> listByPrimaryKeyArrays(@PathVariable("api_server_id") String apiServerIds);


    /**
     * 根据机房id查询api_server_config集合信息
     *
     * @param roomIds 机房id(多个以逗号分隔)
     * @return List<ApiServerConfigVO> api_server_config查询响应对象
     */
    @GetMapping(value = "/v1/api_server_config/listByRoomIds/{room_ids}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/api_server_config"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ApiServerConfigVO> listApiSvrConfigsByRoomIds(@PathVariable("room_ids") String roomIds);

    /**
     * 根据机房id查zabbix连接信息
     *
     * @param room 主机ID
     * @return
     */
    @GetMapping(value = "/v1/api_server_config/findByRoom/{room}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/api_server_config"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    ApiServerConfigDetailResponse findByRoom(@PathVariable("room") String room);
}
