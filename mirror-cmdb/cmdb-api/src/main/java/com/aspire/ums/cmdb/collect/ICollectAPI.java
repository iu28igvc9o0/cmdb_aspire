package com.aspire.ums.cmdb.collect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.payload.InsertCollectEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICollectAPI
 * Author:   zhu.juwang
 * Date:     2019/5/6 12:10
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "自动采集接口类")
@RequestMapping("/cmdb/collect")
public interface ICollectAPI {
    /**
     * 查询模型列表, 返回模型下配置的采集配置信息
     * @return 模型列表
     */
    @RequestMapping(value = "/module/list", method = RequestMethod.GET)
    @ApiOperation(value = "查询模型列表", notes = "查询模型列表, 返回模型下配置的采集配置信息", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONArray.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONArray getModuleList();

    /**
     * 根据模型ID, 返回模型下配置的采集配置信息
     * @return 采集配置列表
     */
    @RequestMapping(value = "/{moduleId}", method = RequestMethod.GET)
    @ApiOperation(value = "查询采集配置", notes = "根据模型ID, 返回模型下配置的采集配置信息", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONArray.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONArray getCollectsByModuleId(@PathVariable("moduleId") String moduleId);

    /**
     * 根据模型ID, 返回模型下所有字段信息
     * @return 字段列表
     */
    @RequestMapping(value = "/forms/{moduleId}", method = RequestMethod.GET)
    @ApiOperation(value = "查询模型字段", notes = "根据模型ID, 返回模型下所有字段信息", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONArray.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONArray getFormsByModuleId(@PathVariable("moduleId") String moduleId);

    /**
     * 根据采集配置ID, 返回采集的历史记录
     * @return 采集记录列表
     */
    @RequestMapping(value = "/getCollectRecord/{collectId}", method = RequestMethod.GET)
    @ApiOperation(value = "查询采集历史", notes = "根据采集配置ID, 返回采集的历史记录", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject getCollectRecordByCollectId(@PathVariable("collectId") String collectId,
                                           @RequestParam("pageNumber") Integer pageNumber,
                                           @RequestParam("pageSize") Integer pageSize);

    /**
     * 新增采集配置
     * @return 新增状态 成功/失败 失败原因
     */
    @RequestMapping(value = "/{moduleId}", method = RequestMethod.POST)
    @ApiOperation(value = "新增采集配置", notes = "新增采集配置", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> saveCollect(@PathVariable("moduleId") String moduleId,
                                    @RequestBody InsertCollectEntity collectEntity);

    /**
     * 删除采集配置
     * @return 删除状态 成功/失败 失败原因
     */
    @RequestMapping(value = "/{collectId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除采集配置", notes = "删除采集配置", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "删除成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> deleteCollect(@PathVariable("collectId") String collectId);

    /**
     * 获取配置异常列表
     * @return 配置异常信息列表
     */
    @RequestMapping(value = "/changelog/{moduleId}", method = RequestMethod.POST)
    @ApiOperation(value = "查询配置异常列表", notes = "获取配置异常列表", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject getChangeLogs(@PathVariable("moduleId") String moduleId, @RequestBody Map<String, Object> requestInfo);

    /**
     * 获取配置异常详情
     * @return 配置异常信息详情
     */
    @RequestMapping(value = "/changelog/detail/{batchId}", method = RequestMethod.GET)
    @ApiOperation(value = "查询配置异常详情", notes = "获取配置异常详情", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getChangeLogDetail(@PathVariable("batchId") String batchId);

    /**
     * 发送邮件告警
     * @return 发送邮件告警
     */
    @RequestMapping(value = "/changelog/sendmail/", method = RequestMethod.POST)
    @ApiOperation(value = "发送邮件告警", notes = "发送邮件告警", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "发送成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> sendNotice(@RequestBody JSONObject sendRequest);
}
