package com.aspire.mirror.composite.service.cmdb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICompCollectService
 * Author:   zhu.juwang
 * Date:     2019/3/13 16:59
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "CMDB采集管理")
@RequestMapping("${version}/cmdb")
public interface ICompCollectService {

    /**
     * 根据字典值编码，查询字典值列表
     * @param code 字典编码
     * @return 字典值列表
     */
    @RequestMapping(value = "/dict/getDict/{code}", method = RequestMethod.GET)
    @ApiOperation(value = "查询字典列表", notes = "根据字典值编码，查询字典值列表", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONArray getDictList(@PathVariable("code") String code);

    /**
     * 查询模型列表, 返回模型下配置的采集配置信息
     * @return 模型列表
     */
    @RequestMapping(value = "/collect/module/list", method = RequestMethod.GET)
    @ApiOperation(value = "查询模型列表", notes = "查询模型列表, 返回模型下配置的采集配置信息", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONArray getModuleList();

    /**
     * 根据模型ID, 返回模型下配置的采集配置信息
     * @return 采集配置列表
     */
    @RequestMapping(value = "/collect/{moduleId}", method = RequestMethod.GET)
    @ApiOperation(value = "查询采集配置列表", notes = "根据模型ID, 返回模型下配置的采集配置信息", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONArray getCollectsByModuleId(@PathVariable("moduleId") String moduleId);

    /**
     * 根据模型ID, 返回模型下所有字段信息
     * @return 字段列表
     */
    @RequestMapping(value = "/collect/forms/{moduleId}", method = RequestMethod.GET)
    @ApiOperation(value = "查询模型下字段列表", notes = "根据模型ID, 返回模型下所有字段信息", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONArray getFormsByModuleId(@PathVariable("moduleId") String moduleId);

    /**
     * 根据采集配置ID, 返回采集的历史记录
     * @return 采集记录列表
     */
    @RequestMapping(value = "/collect/getCollectRecord/{collectId}", method = RequestMethod.GET)
    @ApiOperation(value = "查询采集记录列表", notes = "根据采集配置ID, 返回采集的历史记录", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject getCollectRecordByCollectId(@PathVariable("collectId") String collectId, Integer pageNumber, Integer pageSize);

    /**
     * 新增采集配置
     * @return 新增状态 成功/失败 失败原因
     */
    @PostMapping(value = "/collect/{moduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增采集配置", notes = "新增采集配置", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> saveCollect(HttpServletResponse response, @PathVariable("moduleId") String moduleId, @RequestBody JSONObject requestInfo);

    /**
     * 删除采集配置
     * @return 删除状态 成功/失败 失败原因
     */
    @RequestMapping(value = "/collect/{collectId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除采集配置", notes = "删除采集配置", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> deleteCollect(@PathVariable("collectId") String collectId);

    /**
     * 获取配置异常列表
     * @return 配置异常信息列表
     */
    @RequestMapping(value = "/collect/changelog/{moduleId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取配置异常列表", notes = "获取配置异常列表", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject getChangeLogs(@PathVariable("moduleId") String moduleId, @RequestBody Map<String, Object> requestInfo);

    /**
     * 获取配置异常详情
     * @return 配置异常信息详情
     */
    @RequestMapping(value = "/collect/changelog/detail/{batchId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取配置异常详情", notes = "获取配置异常详情", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getChangeLogDetail(@PathVariable("batchId") String batchId);

    /**
     * 获取配置异常详情
     * @return 配置异常信息详情
     */
    @RequestMapping(value = "/collect/changelog/sendmail/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "发送邮件告警", notes = "发送邮件告警", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> sendNotice(HttpServletResponse response, @RequestBody JSONObject sendRequest);

    /**
     * 导出异常数据配置
     * @return 导出异常数据配置
     */
    @RequestMapping(value = "/collect/changelog/export/{moduleId}", method = RequestMethod.POST)
    @ApiOperation(value = "发送邮件告警", notes = "发送邮件告警", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> exportCollectException(HttpServletResponse response, @PathVariable("moduleId") String moduleId,
                                               @RequestBody Map<String, Object> sendRequest);
}
