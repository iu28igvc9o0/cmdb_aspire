package com.aspire.ums.cmdb.maintenance;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.maintenance.payload.MaintenHardPageRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenHardPageResp;
import com.aspire.ums.cmdb.maintenance.payload.MaintenHardwareRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenHardwareResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IMaintenHardAPI
 * Author:   zhu.juwang
 * Date:     2019/5/6 11:19
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "硬件维保接口类")
@RequestMapping("/v1/cmdb/maintenhard")
public interface IMaintenHardAPI {
	
	 /**
     *  新增硬件维保
     * @return 模型列表
     */
    @PostMapping(value = "/insertMaintenHardware" )
    @ApiOperation(value = "新增硬件维保", notes = "新增硬件维保", tags = {"CMDB MaintenHard API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String insertMaintenHardware(@RequestBody MaintenHardwareRequest maintenHardwareRequest) ; 
    
    
    /**
     *  查询硬件维保通过id
     * @return  
     */
    @GetMapping(value = "/selectMaintenHardwareById" )
    @ApiOperation(value = "查询硬件维保通过id", notes = "查询硬件维保通过id", tags = {"CMDB MaintenHard API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = MaintenHardwareResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public MaintenHardwareResp selectMaintenHardwareById( @RequestParam("id") String id );  
     
     
    /**
     *  查询硬件维保通过设备型号
     * @return  
     */
    @GetMapping(value = "/selectMaintenHardwareByDeviceModel" )
    @ApiOperation(value = "查询硬件维保设备型号", notes = "查询硬件维保设备型号", tags = {"CMDB MaintenHard API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public MaintenHardwareResp selectMaintenHardwareByDeviceModel( @RequestParam("mainten_factory") String maintenFactory,
    		                                                       @RequestParam("device_model") String deviceModel,
    		                                                       @RequestParam("warranty_date") String warrantyDate) ; 
    
    
    
    /**
     *  修改硬件维保
     * @return 模型列表
     */
    @PostMapping(value = "/updateMaintenHardware" )
    @ApiOperation(value = "修改硬件维保", notes = "修改硬件维保", tags = {"CMDB MaintenHard API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String updateMaintenHardware(@RequestBody MaintenHardwareRequest maintenHardwareRequest);  
    
    
    /**
     *  批量更新硬件维保
     * @return 模型列表
     */
    @PostMapping(value = "/batchUpdateMaintenHardware" )
    @ApiOperation(value = "批量更新硬件维保", notes = "批量更新硬件维保", tags = {"CMDB MaintenHard API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String batchUpdateMaintenHardware(@RequestBody MaintenHardwareRequest maintenHardwareRequest) ; 
    
     
    
    /**
     *  删除硬件维保
     * @return 模型列表
     */
    @DeleteMapping(value = "/deleteMaintenHardware" )
    @ApiOperation(value = "删除硬件维保", notes = "删除硬件维保", tags = {"CMDB MaintenHard API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String deleteMaintenHardware( @RequestParam("ids") String ids ) ; 
    

   
    /**
     *  分页查询硬件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/listMaintenHardwareByPage" ) 
    @ApiOperation(value = "查询硬件维保数据", notes = "查询硬件维保数据", tags = {"CMDB MaintenHard API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public PageBean<MaintenHardPageResp> selectMaintenHardwareByPage( @RequestBody MaintenHardPageRequest maintenHardPageRequest ); 
    
    /**
     *  查询硬件维保列表
     * @return 模型列表
     */
    @PostMapping(value = "/getMaintenHardwareList" ) 
    @ApiOperation(value = "导出硬件维保数据", notes = "导出硬件维保数据", tags = {"CMDB MaintenHard API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public List<MaintenHardPageResp> getMaintenHardwareList( @RequestBody MaintenHardPageRequest maintenHardPageRequest  ) ;
    /**
     *  保存硬件维保列表
     * @return 模型列表
     */
    @PostMapping(value = "/insertMaintenHardwareList" )
    @ApiOperation(value = "导入硬件维保数据", notes = "导入硬件维保数据", tags = {"CMDB MaintenHard API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	 public String insertMaintenHardwareList( @RequestBody List<MaintenHardwareRequest>  maintenHardwareRequestList );  
    
    
    
    /**
     *  导出硬件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/downloadMaintenHardware" ) 
    @ApiOperation(value = "导出硬件维保", notes = "导出硬件维保", tags = {"CMDB MaintenHard API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	public void downloadMaintenHardware( @RequestBody MaintenHardPageRequest compMaintenManagePageRequest ) ;

    /**
     *  导入硬件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/uploadMaintenHardware" )
    @ApiOperation(value = "导入硬件维保", notes = "导入硬件维保", tags = {"CMDB MaintenHard API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	public Map<String, Object> uploadMaintenHardware( @RequestParam("file")  MultipartFile file) ; 
    
	

}
