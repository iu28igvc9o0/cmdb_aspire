package com.aspire.mirror.theme.api.service;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.Result;
import com.aspire.mirror.theme.api.dto.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 业务主题服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.service
 * 类名称:    BizThemeService.java
 * 类描述:    业务主题暴露服务
 * 创建人:    JinSu
 * 创建时间:  2018/10/22 17:01
 * 版本:      v1.0
 */
public interface BizThemeService {
    /**
     * 创建主题
     *
     * @param createRequest 创建主题请求
     * @return BizThemeCreateResponse 创建结果返回
     */
    @PostMapping(value = "/v1/theme/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建主题", notes = "创建主题", response = BizThemeCreateResponse.class, tags = {"Theme API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = BizThemeCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = BizThemeCreateResponse.class)})
    BizThemeCreateResponse createdBizTheme(@RequestBody BizThemeCreateRequest createRequest);

    /**
     * 修改主题
     *
     * @param themeId       主题ID
     * @param updateRequest 修改请求
     * @return BizThemeUpdateResponse 修改返回
     */
    @PutMapping(value = "/v1/theme/{theme_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改主题", notes = "修改主题", response = BizThemeUpdateResponse.class, tags = {"Theme API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = BizThemeUpdateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = BizThemeUpdateResponse.class)})
    BizThemeUpdateResponse modifyByPrimaryKey(@PathVariable("theme_id") String themeId, @RequestBody
            BizThemeUpdateRequest updateRequest);

    /**
     * 删除主题
     *
     * @param themeId 主题ID
     */
    @DeleteMapping(value = "/v1/theme/{theme_id}")
    @ApiOperation(value = "删除主题", notes = "删除主题",
            tags = {"Theme API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    void deleteByPrimaryKey(@PathVariable("theme_id") String themeId);

    /**
     * 分页查询
     *
     * @param pageRequest 分页请求对象
     * @return PageResponse<BizThemeDetailResponse>
     */
    @PostMapping(value = "/v1/theme/pageList")
    @ApiOperation(value = "列表", notes = "获取主题列表", tags = {"Theme API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<BizThemeDetailResponse> pageList(@RequestBody BizThemePageRequest pageRequest);

    /**
     * 查询列表
     *
     * @param searchRequest 查询请求
     * @return List<BizThemeDetailResponse> 结果列表
     */
    @PostMapping(value = "/v1/theme/list")
    @ApiOperation(value = "列表", notes = "获取主题列表", tags = {"Theme API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<BizThemeDetailResponse> select(@RequestBody BizThemeSearchRequest searchRequest);

    /**
     * 查询详情
     *
     * @param themeId 主题ID
     * @return BizThemeDetailResponse 主题详情返回
     */
    @PostMapping(value = "/v1/theme/{theme_id}")
    @ApiOperation(value = "详情", notes = "根据theme_id获取主题详情", tags = {"Theme API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = BizThemeDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    BizThemeDetailResponse findByPrimaryKey(@PathVariable("theme_id") String themeId, @RequestBody GeneralAuthConstraintsAware authParam);

    /**
     * 接收主题数据
     */
    @PostMapping(value = "/v1/theme/createThemeData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "接收主题数据", notes = "接收主题数据", response = Result.class, tags = {"Theme API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Result.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Result.class)})
    Result createThemeData(@RequestBody ThemeDataCreateRequest themeDataCreateRequest);

    /**
     * 查询主题数据
     */
    @GetMapping(value = "/v1/theme/getThemeData/{theme_id}")
    @ApiOperation(value = "获取主题数据", notes = "获取主题数据", tags = {"Theme API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = BizThemeDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    BizThemeDataDetailResponse getThemeData(@PathVariable("theme_id") String themeId, @RequestParam("host") String
            host, @RequestParam("biz_code") String bizCode, @RequestParam("theme_code") String themeCode);

    /**
     * 查询主题历史数据
     */
    @GetMapping(value = "/v1/theme/getThemeDataHis/{theme_id}")
    @ApiOperation(value = "接收主题历史数据", notes = "接收历史主题数据", tags = {"Theme API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = BizThemeDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    BizThemeDataDetailResponse getThemeDataHis(@PathVariable("theme_id") String themeId);

    @GetMapping(value = "/v1/theme/getTrendMapData")
    @ApiOperation(value = "获取趋势图数据", notes = "获取趋势图数据", tags = {"Theme API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = BizThemeDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getTrendMapData(@RequestParam(value = "index_name") String indexName,
                                        @RequestParam(value = "theme_code") String themeCode,
                                        @RequestParam(value = "last_up_time_str") String lastUpTimeStr,
                                        @RequestParam(value = "start") String start,
                                        @RequestParam(value = "end") String end);

    @PostMapping(value = "/v1/theme/validLogContent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取日志数据内容校验结果", notes = "获取日志数据内容校验结果", tags = {"Theme API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> validLogContent(@RequestBody @Validated ThemeLogValidRequest validParam);
//
//    @GetMapping(value = "/v1/theme/test")
//    @ApiOperation(value = "test", notes = "test", tags = {"Theme API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
//            @ApiResponse(code = 500, message = "内部错误")})
//    Object getTest(@RequestParam(name = "freshTime", required = false) String freshTime);
}
