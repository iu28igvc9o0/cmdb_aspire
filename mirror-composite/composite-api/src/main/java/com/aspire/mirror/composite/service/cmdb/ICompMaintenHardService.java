package com.aspire.mirror.composite.service.cmdb;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardPageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardPageResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardwareRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardwareResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.composite.service.cmdb
 * 类名称:    ICompMaintenHardService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/20 10:14
 * 版本:      v1.0
 */

@Api(value = "硬件维保信息")
@RequestMapping("${version}/cmdb/maintenhard")
public interface ICompMaintenHardService {
	
	
	/**
     * 新增硬件维保
     * @return String 返回  
     */
    @PostMapping(value = "/insertMaintenHardware")
    @ApiOperation(value = "新增硬件维保", notes = "新增硬件维保", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public String insertMaintenHardware(@RequestBody CompMaintenHardwareRequest compMaintenHardwareRequest);
    
    
    
    /**
     * 修改硬件维保
     * @return String 返回  
     */
    @PostMapping(value = "/updateMaintenHardware")
    @ApiOperation(value = "修改硬件维保", notes = "修改硬件维保", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public String updateMaintenHardware(@RequestBody CompMaintenHardwareRequest compMaintenHardwareRequest);
    
    
    
    /**
     *  批量更新硬件维保
     * @return 模型列表
     */
    @PostMapping(value = "/batchUpdateMaintenHardware" )
    @ApiOperation(value = "批量更新硬件维保", notes = "批量更新硬件维保", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String batchUpdateMaintenHardware(@RequestBody CompMaintenHardwareRequest compMaintenHardwareRequest) ; 
    
    
    
    /**
     *  查询硬件维保通过id
     * @return 硬件维保
     */
    @GetMapping(value = "/selectMaintenHardwareById" )
    @ApiOperation(value = "查询硬件维保", notes = "查询硬件维保通过id", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public CompMaintenHardwareResp selectMaintenHardwareById( @RequestParam("id") String id );
    
   
    
    /**
     *  查询硬件维保通过设备型号
     * @return  
     */
    @GetMapping(value = "/selectMaintenHardwareByDeviceModel" )
    @ApiOperation(value = "查询硬件维保", notes = "查询硬件维保通过名称", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public CompMaintenHardwareResp selectMaintenHardwareByDeviceModel( @RequestParam("mainten_factory") String maintenFactory,
														            @RequestParam("device_model") String deviceModel,
														            @RequestParam("warranty_date") String warrantyDate );
    
    
    
    /**
     *  删除硬件维保
     * @return  
     */
    @DeleteMapping(value = "/deleteMaintenHardware" )
    @ApiOperation(value = "删除硬件维保", notes = "删除硬件维保", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public String deleteMaintenHardware( @RequestParam("ids") String ids );
    
    
    
    /**
     * 分页查询硬件维保数据
     * @return  
     */
    @PostMapping(value = "/listMaintenHardwareByPage" )  
    @ApiOperation(value = "查询分页数据", notes = "查询分页数据", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public PageResponse<CompMaintenHardPageResp> selectMaintenHardByPage( @RequestBody  CompMaintenHardPageRequest  compMaintenHardPageRequest ) ;
    
    
    
    /**
     *  导出硬件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/downloadMaintenHardware" )
    @ApiOperation(value = "导出硬件维保", notes = "导出硬件维保", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public void downloadMaintenHardware( @RequestBody CompMaintenHardPageRequest  compMaintenHardPageRequest );
    
    
    /**
     *  导入硬件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/uploadMaintenHardware" )
    @ApiOperation(value = "导入硬件维保", notes = "导入硬件维保", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
	public Map<String, Object> uploadMaintenHardware(@RequestParam("file") MultipartFile file ); 
    
    
}
