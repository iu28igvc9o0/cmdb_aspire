package com.aspire.mirror.composite.service.alert;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.composite.service.alert.payload.ComTenantRateRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author baiwp
 * @title: IItemService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:52
 */
@RequestMapping("${version}/alerts/resourceScreen")
public interface IComResourceRateScreenService {


	
    @GetMapping(value = "/idcTypRate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "一级it云资源利用率", notes = "一级it云资源利用率", tags = {"IResourceRateScreenService API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = PageResult.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> queryData(@RequestParam(value = "timeRange", required = false)String timeRange
			 ,@RequestParam(value = "idcType", required = false)String idcType
			 ,@RequestParam(value = "deviceType", required = false)String deviceType
			 ,@RequestParam(value = "col", required = false)String col
			 ,@RequestParam(value = "colValue", required = false)String colValue
			 ,@RequestParam(value = "granularity", required = false)String granularity )throws ParseException;

    @PostMapping(value = "/departmentRate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "租户资源利用率", notes = "租户资源利用率", tags = {"IResourceRateScreenService API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = PageResult.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> queryDepartmentData(@RequestBody ComTenantRateRequest request) throws ParseException;
  
}
