package com.aspire.ums.cmdb.deviceStatistic;

import com.aspire.ums.cmdb.deviceStatistic.payload.DeviceStatisticRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.DeviceStatisticResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

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
@Api(value = "设备实例接口类")
@RequestMapping("/v1/cmdb/deviceStatistic")
public interface IDeviceStatisticAPI {
	
	 
   
    /**
     *  各类型设备的数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectDeviceBydeviceType" ) 
    @ApiOperation(value = "各类型设备的数量统计", notes = "各类型设备的数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<DeviceStatisticResp> selectDeviceBydeviceType( @RequestBody DeviceStatisticRequest deviceStatisticRequest ); 
    
    
    
    /**
     *  各类型的各品牌设备数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectDeviceBydeviceTypeDeviceMfrs" ) 
    @ApiOperation(value = "各类型的各品牌设备数量统计", notes = "各类型的各品牌设备数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<DeviceStatisticResp> selectDeviceBydeviceTypeDeviceMfrs( @RequestBody DeviceStatisticRequest deviceStatisticRequest ); 
    
    
    
    /**
     *  各资源池各类型的数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectDeviceByidcTypeDeviceType" ) 
    @ApiOperation(value = "各资源池各类型的数量统计", notes = "各资源池各类型的数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<DeviceStatisticResp> selectDeviceByidcTypeDeviceType( @RequestBody DeviceStatisticRequest deviceStatisticRequest ); 
    
    
    
    /**
     *  各业务系统各分类数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectDeviceBybizSystem" ) 
    @ApiOperation(value = "业务系统各分类数量统计", notes = "业务系统各分类数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<DeviceStatisticResp> selectDeviceBybizSystem( @RequestBody DeviceStatisticRequest deviceStatisticRequest ); 
    
    
    
    /**
     *  各归属部门各分类数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectDeviceByDepartment" ) 
    @ApiOperation(value = "各归属部门各分类数量统计", notes = "各归属部门各分类数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<DeviceStatisticResp> selectDeviceByDepartment( @RequestBody DeviceStatisticRequest deviceStatisticRequest );


    /**
     *  多选资源池的工期统计信息
     * @return 模型列表
     */
    @PostMapping(value = "/selectDeviceByMultiIdcType" )
    @ApiOperation(value = "多选资源池的工期统计信息", notes = "多选资源池的工期统计信息", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> selectDeviceByMultiIdcType( @RequestBody List<String> idcTypes ) ;
    
    /**
     *  下载各类型设备的数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadDeviceBydeviceType" ) 
    @ApiOperation(value = "下载各类型设备的数量统计", notes = "下载各类型设备的数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void downloadDeviceBydeviceType( @RequestBody DeviceStatisticRequest deviceStatisticRequest ) ;


    /**
     *  下载各类型的各品牌设备数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadDeviceBydeviceTypeDeviceMfrs" ) 
    @ApiOperation(value = "下载各类型的各品牌设备数量统计", notes = "下载各类型的各品牌设备数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void downloadDeviceBydeviceTypeDeviceMfrs( @RequestBody  DeviceStatisticRequest deviceStatisticRequest ) ;


    /**
     *  下载各资源池各类型的数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadDeviceByidcTypeDeviceType" ) 
    @ApiOperation(value = "下载各资源池各类型的数量统计", notes = "下载各资源池各类型的数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void downloadDeviceByidcTypeDeviceType( @RequestBody  DeviceStatisticRequest deviceStatisticRequest  ) ;

    
    
    /**
     *  下载各业务系统各分类数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadDeviceBybizSystem" ) 
    @ApiOperation(value = "下载各业务系统各分类数量统计", notes = "下载各业务系统各分类数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void downloadDeviceBybizSystem( @RequestBody  DeviceStatisticRequest deviceStatisticRequest  ) ;

    
    
    /**
     *  下载各归属部门各分类数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadDeviceByDepartment" ) 
    @ApiOperation(value = "下载各归属部门各分类数量统计", notes = "下载各归属部门各分类数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void downloadDeviceByDepartment( @RequestBody  DeviceStatisticRequest deviceStatisticRequest  ) ;

    /**
     *  各资源池业务统计
     * @return 模型列表
     */
    @GetMapping(value = "/idcTypeStatistic" )
    @ApiOperation(value = "各资源池业务统计", notes = "各资源池业务统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> selectIdcTypeStatistic();

    /**
     *  资源池下工期统计
     * @param idcType 所属资源池
     * @return 模型列表
     */
    @GetMapping(value = "/projectStatistic" )
    @ApiOperation(value = "各资源池业务统计", notes = "各资源池业务统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> selectProjectStatistic(@RequestParam("idcType") String idcType);

    /**
     *  指定资源池、工期下的POD池统计
     * @return 模型列表
     */
    @GetMapping(value = "/podStatistic" )
    @ApiOperation(value = "资源池、工期下的POD池统计", notes = "资源池、工期下的POD池统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> selectPodStatistic(@RequestParam("idcType") String idcType,
                                                 @RequestParam("projectName") String projectName);
}
