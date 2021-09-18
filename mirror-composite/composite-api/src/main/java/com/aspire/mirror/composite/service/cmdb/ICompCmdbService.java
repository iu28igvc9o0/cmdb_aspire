package com.aspire.mirror.composite.service.cmdb;

import com.aspire.mirror.composite.service.inspection.payload.CompDeviceListResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompDevicePageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.cmdb
 * 类名称:    ICompCmdbService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/20 10:14
 * 版本:      v1.0
 */
@Api(value = "设备信息管理")
@RequestMapping("${version}/cmdb")
public interface ICompCmdbService {
    /**
     * 查询设备列表
     */
    @PostMapping(value = "/deviceList")
    @ApiOperation(value = "查询", notes = "查询", tags = {"CMDB API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    CompDeviceListResponse deviceList(@RequestBody CompDevicePageRequest devicePageRequest);

    /**
     * 设备模型树获取
     */
    @GetMapping(value = "/getModuleTree")
    @ApiOperation(value = "设备模型查询", notes = "设备模型查询", tags = {"CMDB API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getModuleTree();
    /**
     * 机房信息获取
     */
    @GetMapping(value = "/getRoomList")
    @ApiOperation(value = "机房查询", notes = "机房查询", tags = {"CMDB API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Object listRoom();

    /**
     * 业务系统信息获取
     */
    @GetMapping(value = "/getBizSysList")
    @ApiOperation(value = "业务系统查询", notes = "业务系统查询", tags = {"CMDB API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Object listBizSys();
}
