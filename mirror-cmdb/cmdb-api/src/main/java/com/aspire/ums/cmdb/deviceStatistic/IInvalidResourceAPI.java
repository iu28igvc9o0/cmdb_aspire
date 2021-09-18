package com.aspire.ums.cmdb.deviceStatistic;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.ums.cmdb.deviceStatistic.payload.InvalidResourceRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.InvalidResourceResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IInvalidResourceAPI
 * Author:   zhu.juwang
 * Date:     2019/5/6 11:19
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "低效无效资源接口类") 
@RequestMapping("/v1/cmdb/invalidResource")
public interface IInvalidResourceAPI {
	
	
    /**
     *  低效无效资源统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectInvalidResource" ) 
    @ApiOperation(value = "低效无效资源统计", notes = "低效无效资源统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public List<InvalidResourceResp> selectInvalidResource( @RequestBody InvalidResourceRequest  invalidResourceRequest ); 
    
   
    /**
     *  保存低效无效资源统计
     * @return 模型列表
     */
    @PostMapping(value = "/insertInvalidResource" ) 
    @ApiOperation(value = "保存低效无效资源统计", notes = "保存低效无效资源统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String insertInvalidResource( @RequestBody List<InvalidResourceResp>  invalidResourceList); 
    
    
    
    
    /**
     *  下载低效无效资源统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadInvalidResource" ) 
    @ApiOperation(value = "下载低效无效资源统计", notes = "下载低效无效资源统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	public void downloadInvalidResource( @RequestBody InvalidResourceRequest  invalidResourceRequest, @RequestParam("model") String model ) ;

    
    
    /**
     *  上传低效无效资源统计
     * @return 模型列表
     */
    @PostMapping(value = "/uploadInvalidResource" ) 
    @ApiOperation(value = "上传低效无效资源统计", notes = "上传低效无效资源统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	public Map<String, Object> uploadInvalidResource( @RequestParam("file")  MultipartFile file ) ;

    
 

}
