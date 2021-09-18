package com.aspire.ums.cmdb.deviceStatistic;

import com.aspire.ums.cmdb.deviceStatistic.payload.ConfigureChangeRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;


/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IDeviceStatisticAPI
 * Author:   zhu.juwang
 * Date:     2019/5/6 11:19
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "配置项变更接口类")
@RequestMapping("/v1/cmdb/configureChange")
public interface IConfigureChangeAPI {
	
	 
   
    /**
     *  配置项变更统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectConfigureChange" ) 
    @ApiOperation(value = "配置项变更统计", notes = "配置项变更统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> selectConfigureChange(@RequestBody ConfigureChangeRequest configureChangeRequest );
    
     
    
    /**
     *  下载配置项变更统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadConfigureChange" ) 
    @ApiOperation(value = "下载配置项变更统计", notes = "下载配置项变更统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	public void downloadConfigureChange( @RequestBody ConfigureChangeRequest configureChangeRequest ) ;

     
   
}
