package com.aspire.ums.cmdb.allocate;

import com.aspire.ums.cmdb.allocate.payload.AllocateIpConfigDetail;
import com.aspire.ums.cmdb.allocate.payload.AllocateIpConfigListReq;
import com.aspire.ums.cmdb.allocate.payload.AllocateIpConfigRes;
import com.aspire.ums.cmdb.allocate.payload.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IAllocateIpConfigAPI
 * Author:   zhu.juwang
 * Date:     2019/5/6 13:06
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "IP分配配置接口类")
@RequestMapping("/cmdb/allocateIpConfig")
public interface IAllocateIpConfigAPI {

    @GetMapping("/list")
    @ApiOperation(value = "查询列表", notes = "查询列表", tags = {"CMDB Allocate Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<AllocateIpConfigRes> getAllocateIpConfigData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                       @RequestParam(value = "startPageNum",required = false) int startPageNum,
                                                       @RequestParam(value = "pageSize",required = false) int pageSize,
                                                       @RequestParam(value = "vpnId",required = false) int vpnId,
                                                       @RequestParam(value = "networkId",required = false) int networkId,
                                                       @RequestParam(value = "bizSystem",required = false) String bizSystem,
                                                       @RequestParam(value = "ip",required = false) String ip,
                                                       @RequestParam(value = "privateIp",required = false) String privateIp,
                                                       @RequestParam(value = "isAdd",required = false) boolean isAdd);

    @PostMapping("/add")
    @ApiOperation(value = "新增配置", notes = "新增配置", tags = {"CMDB Allocate Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String insertAllocateIpConfig(@RequestBody List<AllocateIpConfigDetail> request, @RequestParam(value = "name") String name);

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除配置", notes = "删除配置", tags = {"CMDB Allocate Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String deleteAllocateIpConfigById(@RequestParam(value = "ids") String ids, @RequestParam(value = "name") String name);

    @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出配置", notes = "导出配置", tags = {"CMDB Allocate Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导出配置", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> exportAllocateIpConfig(@RequestBody AllocateIpConfigListReq request);

    @GetMapping("/getVpnData")
    @ApiOperation(value = "查询VPN", notes = "查询VPN", tags = {"CMDB Allocate Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询配置", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getVpnData();

    @GetMapping("/getNetworkById")
    @ApiOperation(value = "根据ID查询网关数据", notes = "根据ID查询网关数据", tags = {"CMDB Allocate Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getNetworkById(@RequestParam(value = "id") long id);
}
