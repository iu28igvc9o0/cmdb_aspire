package com.aspire.mirror.composite.service.theme;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.Result;
import com.aspire.mirror.composite.service.theme.payload.*;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 业务主题对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.theme
 * 类名称:    ICompBizThemeService.java
 * 类描述:    业务主题对外暴露接口
 * 创建人:    JinSu
 * 创建时间:  2018/10/24 17:40
 * 版本:      v1.0
 */
@Api(value = "主题管理")
@RequestMapping("${version}/theme")
public interface ICompBizThemeService {
        /**
         * 创建主题
         * @param createRequest 创建主题请求
         * @return CompBizThemeCreateResponse 创建结果返回
         */
        @PostMapping(value="/create", produces = MediaType.APPLICATION_JSON_VALUE)
        @ApiOperation(value="创建主题",notes="创建主题",response=CompBizThemeCreateResponse.class,tags={ "Theme API" })
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "返回", response =  CompBizThemeCreateResponse.class),
                @ApiResponse(code = 500, message = "Unexpected error", response = CompBizThemeCreateResponse.class) })
        CompBizThemeCreateResponse createdBizTheme(@RequestBody CompBizThemeCreateRequest createRequest);

        /**
         * 修改主题
         * @param themeId 主题ID
         * @param updateRequest 修改请求
         * @return CompBizThemeUpdateResponse 修改返回
         */
        @PutMapping(value="/{theme_id}", produces = MediaType.APPLICATION_JSON_VALUE)
        @ApiOperation(value="修改主题",notes="修改主题",response=CompBizThemeUpdateResponse.class,tags={ "Theme API" })
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "返回", response =  CompBizThemeUpdateResponse.class),
                @ApiResponse(code = 500, message = "Unexpected error", response = CompBizThemeUpdateResponse.class) })
        CompBizThemeUpdateResponse modifyByPrimaryKey(@PathVariable("theme_id") String themeId, @RequestBody CompBizThemeUpdateRequest updateRequest);

        /**
         *删除主题
         * @param themeId 主题ID
         */
        @DeleteMapping(value = "/{theme_id}")
        @ApiOperation(value = "删除主题", notes = "删除主题",
                tags = {"Theme API"})
        @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
        void deleteByPrimaryKey(@PathVariable("theme_id") String themeId);

        /**
         * 分页查询
         * @param pageRequest 分页请求对象
         * @return PageResponse<CompBizThemeDetailResponse>
         */
        @PostMapping(value = "/pageList")
        @ApiOperation(value = "列表", notes = "获取主题列表", tags = {"Theme API"})
        @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
                @ApiResponse(code = 500, message = "内部错误")})
        PageResponse<CompBizThemeDetailResponse> pageList(@RequestBody CompBizThemePageRequest pageRequest);

        /**
         * 查询列表
         * @param bizCode 业务编码
         * @return List<CompBizThemeDetailResponse> 结果列表
         */
        @PostMapping(value = "/selectByBizCode/{biz_code}")
        @ApiOperation(value = "列表", notes = "获取主题列表", tags = {"Theme API"})
        @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = List.class),
                @ApiResponse(code = 500, message = "内部错误")})
        List<CompBizThemeDetailResponse> selectByBizCode(@PathVariable("biz_code") String bizCode);

        /**
         * 根据ObjectType查询列表
         */
        @GetMapping(value = "/selectByObjectType/{object_type}")
        @ApiOperation(value = "列表", notes = "获取主题列表", tags = {"Theme API"})
        @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = List.class),
                @ApiResponse(code = 500, message = "内部错误")})
        List<CompBizThemeDetailResponse> selectByObjectType(@PathVariable("object_type") String objectType);
        /**
         * 查询详情
         * @param themeId 主题ID
         * @return CompBizThemeDetailResponse 主题详情返回
         */
        @GetMapping(value = "/{theme_id}")
        @ApiOperation(value = "详情", notes = "根据theme_id获取主题详情", tags = {"Theme API"})
        @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompBizThemeDetailResponse.class),
                @ApiResponse(code = 500, message = "内部错误")})
        CompBizThemeDetailResponse findByPrimaryKey(@PathVariable("theme_id") String themeId);

        @GetMapping(value = "/validThemeName/{theme_name}")
        @ApiOperation(value = "查询是否存在主题资源", notes = "根据theme_name获取主题资源", tags = {"Theme API"})
        @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = BaseResponse.class),
                @ApiResponse(code = 500, message = "内部错误")})
        BaseResponse validThemeName(@PathVariable("theme_name") String themeName);

        /**
         * 接收主题数据
         */
        @PostMapping(value = "/createThemeData", produces = MediaType.APPLICATION_JSON_VALUE)
        @ApiOperation(value="接收主题数据",notes="接收主题数据",response=Result.class,tags={ "Theme API" })
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "返回", response =  Result.class),
                @ApiResponse(code = 500, message = "Unexpected error", response = Result.class) })
        Result createThemeData(@RequestBody CompThemeDataCreateRequest themeDataCreateRequest);

        /**
         * 查询主题数据
         */
        @GetMapping(value = "/getThemeData/{theme_id}")
        @ApiOperation(value = "获取主题数据", notes = "获取主题数据", tags = {"Theme API"})
        @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompBizThemeDataDetailResponse.class),
                @ApiResponse(code = 500, message = "内部错误")})
        CompBizThemeDataDetailResponse getThemeData(@PathVariable("theme_id") String themeId, @RequestParam("host") String host, @RequestParam("biz_code") String bizCode, @RequestParam("theme_code") String themeCode);

        /**
         * 查询主题数据
         */
        @GetMapping(value = "/getThemeDataHis/{theme_id}")
        @ApiOperation(value = "接收主题历史数据", notes = "接收主题历史数据", tags = {"Theme API"})
        @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompBizThemeDataDetailResponse.class),
                @ApiResponse(code = 500, message = "内部错误")})
        CompBizThemeDataDetailResponse getThemeDataHis(@PathVariable("theme_id") String themeId);

        @GetMapping(value = "/getTrendMapData")
        @ApiOperation(value = "获取趋势图数据", notes = "获取趋势图数据", tags = {"Theme API"})
        @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Map.class),
                @ApiResponse(code = 500, message = "内部错误")})
        Map<String, Object> getTrendMapData(@RequestParam(value = "index_name") String indexName,
                                            @RequestParam(value = "theme_code") String themeCode,
                                            @RequestParam(value = "last_up_time_str") String lastUpTimeStr,
                                            @RequestParam(value = "start") String start,
                                            @RequestParam(value = "end") String end);

        @GetMapping(value = "/downloadExplainDoc")
        @ApiOperation(value = "下载主题数据接入说明文档", notes = "下载主题数据接入说明文档", tags = {"Theme API"})
        void downloadExplainDoc(HttpServletResponse response);
//
//        @GetMapping(value = "/getThemeDateAccessUrl")
//        @ApiOperation(value = "获取主题数据接入url", notes = "获取主题数据接入url", tags = {"Theme API"})
//        String getThemeDateAccessUrl();

        @PostMapping(value = "/validLogContent", produces = MediaType.APPLICATION_JSON_VALUE)
        @ApiOperation(value = "获取日志数据内容校验结果", notes = "获取日志数据内容校验结果", tags = {"Theme API"})
        @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = List.class),
                @ApiResponse(code = 500, message = "内部错误")})
        List<Map<String, Object>> validLogContent(@RequestBody @Validated CompThemeLogValidRequest validParam);
}
