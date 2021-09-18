package com.aspire.mirror.composite.service.cmdb;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.composite.service.cmdb
 * 类名称:    ICompDeviceStatisticService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:   2018/9/20 10:14
 * 版本:      v1.0
 */

 

@Api(value = "资源查询接口类")
@RequestMapping("/v1/cmdb/instanceSearch")
public interface ICompInstanceSearchService {
	
	 
    @PostMapping(value = "/selectInstanceByPage" ) 
    @ApiOperation(value = "资源查询接口类", notes = "资源查询接口类", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public PageBean<InstanceSearchResp> selectInstanceByPage( @RequestBody InstanceSearchRequest instanceSearchRequest ); 
    
     
   
   
}



