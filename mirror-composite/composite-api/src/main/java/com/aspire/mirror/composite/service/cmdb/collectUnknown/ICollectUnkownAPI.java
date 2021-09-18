package com.aspire.mirror.composite.service.cmdb.collectUnknown;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICollectUnkownAPI
 * Author:   hangfang
 * Date:     2019/10/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "自动采集未知设备接口类")
@RequestMapping("${version}/cmdb/collectUnknown")
public interface ICollectUnkownAPI {
    /**
     * 根据条件筛选未知设备
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "根据条件筛选未知设备", notes = "根据条件筛选未知设备", tags = {"CMDB CollectUnknown API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbCollectUnknown> list(@RequestBody CmdbCollectUnknownQuery collectUnknownQuery);

    /**
     * 新增未知设备
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "新增未知设备", notes = "新增未知设备", tags = {"CMDB CollectUnknown API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> insert(@RequestBody CmdbCollectUnknown collectUnknown);

    /**
     * 更新未知设备
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "更新未知采集设备", notes = "更新未知采集设备", tags = {"CMDB CollectUnknown API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "更新成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> update(@RequestParam("id") String id,
                               @RequestParam("handleStatus") Integer handleStatus);

    /**
     * 导出未知设备列表
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation(value = "导出未知设备列表", notes = "导出未知设备列表", tags = {"CMDB CollectUnknown API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "导出成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    void export(@RequestBody CmdbCollectUnknownQuery collectUnknownQuery, HttpServletResponse response);

    /**
     * 维护未知设备列表
     */
    @RequestMapping(value = "/maintain", method = RequestMethod.POST)
    @ApiOperation(value = "维护未知设备列表", notes = "维护未知设备列表", tags = {"CMDB CollectUnknown API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "维护成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> maintain(@RequestBody CmdbCollectUnknownQuery collectUnknownQuery);

    /**
     * 导出维护失败列表
     */
    @RequestMapping(value = "/exportErrorFile/{processId}", method = RequestMethod.POST)
    @ApiOperation(value = "维护未知设备列表", notes = "维护未知设备列表", tags = {"CMDB CollectUnknown API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "维护成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> exportErrorFile(@PathVariable("processId")String processId, HttpServletResponse response);

    }
