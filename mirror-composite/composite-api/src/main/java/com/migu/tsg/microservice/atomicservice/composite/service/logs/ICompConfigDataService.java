package com.migu.tsg.microservice.atomicservice.composite.service.logs;

import com.aspire.mirror.common.entity.PageResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.CompConfigDataResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.CompConfigDataSearchRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.ConfigDataExportRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.log.api.service
 * 类名称:    ICompConfigDataService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/19 13:47
 * 版本:      v1.0
 */
@Api(value = "配置文件数据接口")
@RequestMapping("${version}/config")
public interface ICompConfigDataService {
    @PostMapping(value = "/getConfigData", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取配置数据", notes = "获取配置数据", tags = {"CONFIG DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompConfigDataResponse> getConfigData(@RequestBody CompConfigDataSearchRequest request);

    @PostMapping(value = "/exportConfigData", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "导出配置数据", notes = "导出配置数据", tags = {"CONFIG DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportConfigData(@RequestBody ConfigDataExportRequest request, HttpServletResponse response);

    @GetMapping(value = "/getConfigById/{index}/{id}")
    @ApiOperation(value = "获取配置数据详情", notes = "获取配置数据详情", tags = {"CONFIG DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompConfigDataResponse.class), @ApiResponse(code=500, message = "内部错误")})
    CompConfigDataResponse getConfigById(@PathVariable("index") String index, @PathVariable("id") String id);

    @PostMapping(value = "/uploadConfigFile", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "上传配置文件", notes = "上传配置文件", tags = {"CONFIG DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> uploadConfigFile(@RequestParam("idcType") String idcType,
                                        @RequestParam("uploadInfo") String uploadInfo,
                                        @RequestParam("file") MultipartFile file);
}
