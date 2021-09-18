package com.aspire.mirror.composite.service.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.composite.service.cmdb.payload.CompDeviceStatisticRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompInvalidResourceRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompInvalidResourceResp;
 

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.composite.service.cmdb
 * 类名称:    ICompInvalidResourceService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:   2018/9/20 10:14
 * 版本:      v1.0
 */

@Api(value = "低效无效资源接口类")
@RequestMapping("${version}/cmdb/invalidResource")
public interface ICompInvalidResourceService {
	

    /**
     *  低效无效资源统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectInvalidResource" ) 
    @ApiOperation(value = "低效无效资源统计", notes = "低效无效资源统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public List<CompInvalidResourceResp> selectInvalidResource( @RequestBody CompInvalidResourceRequest  compInvalidResourceRequest ); 
    
        
    
    /**
     *  下载低效无效资源统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadInvalidResource" ) 
    @ApiOperation(value = "下载低效无效资源统计", notes = "下载低效无效资源统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	public void downloadInvalidResource( @RequestBody CompInvalidResourceRequest  compInvalidResourceRequest, @RequestParam("model") String model ) ;

        
        
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
