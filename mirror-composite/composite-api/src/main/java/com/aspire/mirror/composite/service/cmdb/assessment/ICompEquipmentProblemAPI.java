package com.aspire.mirror.composite.service.cmdb.assessment;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.payload.CompEquipmentProblemPageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompEquipmentProblemRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompEquipmentProblemResp;
import com.aspire.ums.cmdb.common.PageBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

@Api(value = "设备问题接口类")
@RequestMapping("${version}/cmdb/assessment")
public interface ICompEquipmentProblemAPI {
	
 
    	
	 /**
     *  新增设备问题
     * @return 模型列表
     */
    @PostMapping(value = "/insertEquipmentProblem" )
    @ApiOperation(value = "新增设备问题", notes = "新增设备问题", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String insertEquipmentProblem(@RequestBody CompEquipmentProblemRequest compEquipmentProblemRequest) ; 
    
    
    
    /**
     *  查询设备问题通过id
     * @return  
     */
    @GetMapping(value = "/selectEquipmentProblemById" )
    @ApiOperation(value = "查询设备问题通过id", notes = "查询设备问题通过id", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = CompEquipmentProblemResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public CompEquipmentProblemResp selectEquipmentProblemById( @RequestParam("id") String id );  
     
    
    
    /**
     *  修改设备问题
     * @return 模型列表
     */
    @PostMapping(value = "/updateEquipmentProblem" )
    @ApiOperation(value = "修改设备问题", notes = "修改设备问题", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String updateEquipmentProblem(@RequestBody CompEquipmentProblemRequest compEquipmentProblemRequest);  
    
    
    
    /**
     *  删除设备问题
     * @return 模型列表
     */
    @DeleteMapping(value = "/deleteEquipmentProblem" )
    @ApiOperation(value = "删除设备问题", notes = "删除设备问题", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String deleteEquipmentProblem( @RequestParam("id") String id ) ; 
    
    
    
    /**
     *  审批设备问题
     * @return 模型列表
     */
    @PostMapping(value = "/approveEquipmentProblem" )
    @ApiOperation(value = "审批设备问题", notes = "审批设备问题", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String approveEquipmentProblem(@RequestBody CompEquipmentProblemRequest compEquipmentProblemRequest);  
    
    
    
    /**
     *  分页查询设备问题
     * @return 模型列表
     */
    @PostMapping(value = "/seleteEquipmentProblemByPage" ) 
    @ApiOperation(value = "分页查询设备问题", notes = "分页查询设备问题", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public PageBean<CompEquipmentProblemResp> seleteEquipmentProblemByPage( @RequestBody CompEquipmentProblemPageRequest compequipmentProblemPageRequest ); 
    
    
    
    /**
     *  新增设备问题列表
     */
    @PostMapping(value = "/saveEquipmentProblemList" )
    @ApiOperation(value = "导入设备问题数据", notes = "导入设备问题数据", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
	 public String saveEquipmentProblemList( @RequestBody JSONObject data);
     
    
   
    /**
     *  查询设备问题列表  
     */
    @PostMapping(value = "/getEquipmentProblemList" ) 
    @ApiOperation(value = "查询设备问题列表", notes = "查询设备问题列表", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public List<CompEquipmentProblemResp> getEquipmentProblemList( @RequestBody CompEquipmentProblemRequest  compEquipmentProblemRequest  ) ;
   
    
    
   

}
