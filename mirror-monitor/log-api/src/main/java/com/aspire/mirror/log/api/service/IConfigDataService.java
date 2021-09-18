package com.aspire.mirror.log.api.service;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.ConfigDataResponse;
import com.aspire.mirror.log.api.dto.ConfigDataSearchRequest;
import com.aspire.mirror.log.api.dto.ConfigFileUploadReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.log.api.service
 * 类名称:    IConfigDataService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/19 13:47
 * 版本:      v1.0
 */
@Api(value = "配置文件数据接口")
public interface IConfigDataService {
    @PostMapping(value = "/getConfigData", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取配置数据", notes = "获取配置数据", tags = {"CONFIG DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<ConfigDataResponse> getConfigData(@RequestBody ConfigDataSearchRequest request);

    @GetMapping(value = "/getConfigById/{index}/{id}")
    @ApiOperation(value = "根据ID获取配置数据", notes = "根据ID获取配置数据", tags = {"CONFIG DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    ConfigDataResponse getConfigById(@PathVariable("index") String index, @PathVariable("id") String id);

    @PostMapping(value = "/uploadConfigFile", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "上传配置文件", notes = "上传配置文件", tags = {"CONFIG DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void uploadConfigFile(@RequestBody List<ConfigFileUploadReq> request);

}
