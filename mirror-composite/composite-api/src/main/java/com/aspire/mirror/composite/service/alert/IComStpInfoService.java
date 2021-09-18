package com.aspire.mirror.composite.service.alert;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.composite.service.alert.payload.ComRouteDeviceInfo;
import com.aspire.mirror.composite.service.alert.payload.ComRouteQueryRequest;
import com.aspire.mirror.composite.service.alert.payload.ComStpDeviceInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author longfeng
 * @title: IComStpInfoService
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/7/2910:09
 */
@RequestMapping("${version}/alerts/network")
public interface IComStpInfoService {

    /**
     * 查询负载均衡网略策略列表
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getStpInfoList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询stp信息", notes = "查询stp信息", tags = {"network stp api"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = PageResult.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ComStpDeviceInfo getStpInfoList(@RequestParam(value = "ip") String ip
    		, @RequestParam(value = "idcType") String idcType,@RequestParam(value = "indexDate",required=false) String indexDate);

    @PostMapping(value = "/getRouteInfoList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询route信息", notes = "查询route信息", tags = {"network stp api"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = PageResult.class),
            @ApiResponse(code = 500, message = "内部错误")})
	ComRouteDeviceInfo getRouteInfoList(@RequestBody ComRouteQueryRequest queryRequest);

    @PostMapping(value = "/exportRouteInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "导出路由信息表数据", notes = "导出路由信息表数据", tags = {"network stp api"})
	void exportRouteInfo(@RequestBody ComRouteQueryRequest queryReq, HttpServletResponse response) throws Exception;
	}
