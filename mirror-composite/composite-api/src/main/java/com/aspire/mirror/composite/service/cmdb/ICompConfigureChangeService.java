package com.aspire.mirror.composite.service.cmdb;

import com.aspire.mirror.composite.service.cmdb.payload.CompConfigureChangeRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.ConfigureChangeResp;
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
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.composite.service.cmdb
 * 类名称:    ICompConfigureChangeService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:   2018/9/20 10:14
 * 版本:      v1.0
 */

@Api(value = "配置项变更接口类")
@RequestMapping("${version}/cmdb/configureChange")
public interface ICompConfigureChangeService {
	
	
	/**
     *  配置项变更统计
     * @return 模型列表
     */
	@PostMapping(value = "/selectConfigureChange" ) 
    @ApiOperation(value = "配置项变更统计", notes = "配置项变更统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> selectConfigureChange(@RequestBody CompConfigureChangeRequest  compConfigureChangeRequest );
    
    
    
	/**
     *  下载配置项变更统计
     * @return 模型列表
     */
	@PostMapping(value = "/downloadConfigureChange" ) 
    @ApiOperation(value = "下载配置项变更统计", notes = "下载配置项变更统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void downloadConfigureChange( @RequestBody CompConfigureChangeRequest  compConfigureChangeRequest  ) ;

    
	 
}
