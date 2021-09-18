package com.aspire.mirror.composite.service.cmdb;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.payload.CompAutoDiscoveryLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICompDiscoveryLogService
 * Author:   HANGFANG
 * Date:     2019/4/22 19:36
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

@Api(value = "CMDB新发现数据管理")
@RequestMapping("${version}/cmdb/discovery")
public interface ICompDiscoveryLogService {

    /**
     * 获取指定moduleId的规则列表
     * @param moduleId 模型ID
     * @param pageNumber 当前页数
     * @param pageSize 每页数量
     * @return 新发现数据列表
     */
    @PostMapping(value = "/log/list/{moduleId}")
    @ApiOperation(value = "获取模型下新发现数据", notes = "获取模型下新发现数据", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject getLogList(@PathVariable("moduleId") String moduleId,
                                  @RequestParam(value = "pageNumber") Integer pageNumber,
                                  @RequestParam(value = "pageSize") Integer pageSize,
                                  @RequestBody(required = false) JSONObject queryData);

    @PostMapping(value = "/log/shield")
    @ApiOperation(value = "屏蔽新发现数据", notes = "屏蔽新发现数据", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> shieldIp(HttpServletResponse response, @RequestBody List<CompAutoDiscoveryLogVO> discoveryLogs);

    @RequestMapping(value = "/log/detail/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "获取新发现数据详情", notes = "获取新发现数据详情", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    CompAutoDiscoveryLogVO getLogDetailById(@PathVariable("id") String logId);

    @PostMapping(value = "/log/bind/{instanceId}")
    @ApiOperation(value = "绑定新发现数据", notes = "绑定新发现数据", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> bind(HttpServletResponse response, @PathVariable("instanceId") String instanceId,
                                    @RequestBody CompAutoDiscoveryLogVO discoveryLog);

    @PutMapping(value = "/log/update/{id}")
    @ApiOperation(value = "更新新发现数据", notes = "更新新发现数据", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> update(HttpServletResponse response, @PathVariable("id") String id, @RequestParam(value = "status") String status);


    @RequestMapping(value = "/log/listInstance/{moduleId}", method = RequestMethod.GET)
    @ApiOperation(value = "根据moduleid获取instances", notes = "根据moduleid获取instances", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject listInstanceByModuleId(HttpServletResponse response, @PathVariable(value = "moduleId") String moduleId);

        /**
         * 导出规则
         * @return
         */
    @PostMapping(value = "/log/export/{moduleId}")
    @ApiOperation(value = "导出新发现数据列表", notes = "导出新发现规则列表", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> exportLogs(HttpServletResponse response, @PathVariable("moduleId") String moduleId, @RequestBody Map<String, Object> sendRequest);
}
