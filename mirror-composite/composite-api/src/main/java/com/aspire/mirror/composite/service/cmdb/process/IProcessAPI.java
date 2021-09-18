package com.aspire.mirror.composite.service.cmdb.process;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.composite.service.cmdb.process.payload.ProcessParams;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IProcessAPI
 * Author:   zhu.juwang
 * Date:     2019/6/11 10:03
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "进程处理API")
@RequestMapping("${version}/cmdb/process")
public interface IProcessAPI {
    /**
     * 导入CI列表
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ApiOperation(value = "导入Excel列表", notes = "导入Excel列表", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> importProcess(@RequestParam(value = "filename") MultipartFile file,
                                      ProcessParams processParams,
                                      HttpServletRequest request);

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
    Map<String, String> exportErrorFile(@PathVariable("processId") String processId, HttpServletResponse response);

    /**
     * 下载报表文件
     * @param exportParams 下载参数
     * @return
     */
    @RequestMapping(value = "/export/{exportType}", method = RequestMethod.POST)
    @ApiOperation(value = "下载失败文件", notes = "下载失败文件", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "下载成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> exportReport(@PathVariable("exportType") String exportType,
                                     @RequestBody Map<String, Object> exportParams,
                                     HttpServletResponse response);

    /**
     * 解析excel数据返回前端
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/importDataToView", method = RequestMethod.POST)
    @ApiOperation(value = "解析excel数据返回前端", notes = "解析excel数据返回前端", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "解析成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
	Map<String, Object> importDataToView(@PathVariable(value = "filename") MultipartFile file, 
			 ProcessParams processParams, HttpServletRequest request);
}
