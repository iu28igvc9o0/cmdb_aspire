package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.process;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ProcessClient
 * Author:   zhu.juwang
 * Date:     2019/6/11 10:06
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ProcessClient {
    /**
     * 导入CI列表
     */
    @RequestMapping(value = "/cmdb/process/import", method = RequestMethod.POST)
    Map<String, String> importProcess(@RequestParam("importType") String importType, @RequestBody Map<String, Object> importData);


    /**
     * 批量维护未知设备
     */
    @RequestMapping(value = "/cmdb/process/unknown", method = RequestMethod.POST)
    @ApiOperation(value = "批量维护未知设备", notes = "批量维护未知设备", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "维护成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> unknownProcess(@RequestParam("username") String username, @RequestBody CmdbCollectUnknownQuery query);

    /**
     * 获取导入文件进度
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/cmdb/process/import/{processId}", method = RequestMethod.GET)
    Map<String, Object> getImportProcess(@PathVariable("processId") String processId);

    /**
     * 移除导入文件进度
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/cmdb/process/import/{processId}", method = RequestMethod.DELETE)
    Map<String, Object> removeImportProcess(@PathVariable("processId") String processId);

    /**
     * 下载失败文件
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/cmdb/process/export/error/{processId}", method = RequestMethod.POST)
    ImportProcess exportErrorFile(@PathVariable("processId") String processId);

    /**
     * 下载报表文件
     * @param exportType 下载类型
     * @param exportParams 下载参数
     * @return
     */
    @RequestMapping(value = "/cmdb/process/export/{exportType}", method = RequestMethod.POST)
    List<Map<String, Object>> exportReport(@PathVariable("exportType") String exportType,
                                           @RequestBody Map<String, Object> exportParams);
}
