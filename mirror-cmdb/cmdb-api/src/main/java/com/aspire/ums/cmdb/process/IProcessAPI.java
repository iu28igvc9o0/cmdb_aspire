package com.aspire.ums.cmdb.process;

import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IProcessAPI
 * Author:   zhu.juwang
 * Date:     2019/6/11 9:55
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/process")
public interface IProcessAPI {

    /**
     * 导入CI列表
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ApiOperation(value = "导入Excel列表", notes = "导入Excel列表", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> importProcess(@RequestParam("importType") String importType, @RequestBody Map<String, Object> importData);

    /**
     * 批量维护未知设备
     */
    @RequestMapping(value = "/unknown", method = RequestMethod.POST)
    @ApiOperation(value = "批量维护未知设备", notes = "批量维护未知设备", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "维护成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> unknownProcess(@RequestParam("username") String username, @RequestBody CmdbCollectUnknownQuery query);

    /**
     * 获取导入文件进度
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/import/{processId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取导入文件进度", notes = "获取导入文件进度", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getImportProcess(@PathVariable("processId") String processId);

    /**
     * 移除导入文件进度
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/import/{processId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "移除导入文件进度", notes = "移除导入文件进度", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> removeImportProcess(@PathVariable("processId") String processId);

    /**
     * 下载失败文件
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/export/error/{processId}", method = RequestMethod.POST)
    @ApiOperation(value = "下载失败文件", notes = "下载失败文件", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "下载成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ImportProcess exportErrorFile(@PathVariable("processId") String processId);

    /**
     * 下载报表文件
     * @param exportType 下载类型
     * @param exportParams 下载参数
     * @return
     */
    @RequestMapping(value = "/export/{exportType}", method = RequestMethod.POST)
    @ApiOperation(value = "下载失败文件", notes = "下载失败文件", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "下载成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> exportReport(@PathVariable("exportType") String exportType,
                                           @RequestBody Map<String, Object> exportParams);
}
