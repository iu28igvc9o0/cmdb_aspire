package com.aspire.mirror.composite.service.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.aspire.mirror.composite.service.cmdb.payload.CompDeviceStatisticRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompDeviceStatisticResp;

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

@Api(value = "设备实例接口类")
@RequestMapping("${version}/cmdb/deviceStatistic")
public interface ICompDeviceStatisticService {

    /**
     *  各类型设备的数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectDeviceBydeviceType" ) 
    @ApiOperation(value = "各类型设备的数量统计", notes = "各类型设备的数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CompDeviceStatisticResp> selectDeviceBydeviceType( @RequestBody CompDeviceStatisticRequest  compDeviceStatisticRequest ); 

    /**
     *  各类型的各品牌设备数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectDeviceBydeviceTypeDeviceMfrs" ) 
    @ApiOperation(value = "各类型的各品牌设备数量统计", notes = "各类型的各品牌设备数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CompDeviceStatisticResp> selectDeviceBydeviceTypeDeviceMfrs( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest ); 

    /**
     *  各资源池各类型的数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectDeviceByidcTypeDeviceType" ) 
    @ApiOperation(value = "各资源池各类型的数量统计", notes = "各资源池各类型的数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CompDeviceStatisticResp> selectDeviceByidcTypeDeviceType( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest ); 
    
    /**
     *  各业务系统各分类数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectDeviceBybizSystem" ) 
    @ApiOperation(value = "业务系统各分类数量统计", notes = "业务系统各分类数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CompDeviceStatisticResp> selectDeviceBybizSystem( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest ); 

    /**
     *  各归属部门各分类数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/selectDeviceByDepartment" ) 
    @ApiOperation(value = "各归属部门各分类数量统计", notes = "各归属部门各分类数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CompDeviceStatisticResp> selectDeviceByDepartment( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest ); 

    /**
     *  下载各类型设备的数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadDeviceBydeviceType" ) 
    @ApiOperation(value = "下载各类型设备的数量统计", notes = "下载各类型设备的数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void downloadDeviceBydeviceType( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest ) ;

    /**
     *  下载多选资源池后的工期统计数据
     * @return 模型列表
     */
    @PostMapping(value = "/downloadByIdcTypes" )
    @ApiOperation(value = "下载多选资源池后的工期统计数据", notes = "下载多选资源池后的工期统计数据", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    void downloadByIdcTypes( @RequestBody List<String> idcTypes ) ;

    /**
     *  下载各类型的各品牌设备数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadDeviceBydeviceTypeDeviceMfrs" ) 
    @ApiOperation(value = "下载各类型的各品牌设备数量统计", notes = "下载各类型的各品牌设备数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void downloadDeviceBydeviceTypeDeviceMfrs( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest ) ;

    /**
     *  下载各资源池各类型的数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadDeviceByidcTypeDeviceType" ) 
    @ApiOperation(value = "下载各资源池各类型的数量统计", notes = "下载各资源池各类型的数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void downloadDeviceByidcTypeDeviceType( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest ) ;

    /**
     *  下载各业务系统各分类数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadDeviceBybizSystem" ) 
    @ApiOperation(value = "下载各业务系统各分类数量统计", notes = "下载各业务系统各分类数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void downloadDeviceBybizSystem( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest ) ;

    /**
     *  下载各归属部门各分类数量统计
     * @return 模型列表
     */
    @PostMapping(value = "/downloadDeviceByDepartment" ) 
    @ApiOperation(value = "下载各归属部门各分类数量统计", notes = "下载各归属部门各分类数量统计", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void downloadDeviceByDepartment( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest ) ;

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
