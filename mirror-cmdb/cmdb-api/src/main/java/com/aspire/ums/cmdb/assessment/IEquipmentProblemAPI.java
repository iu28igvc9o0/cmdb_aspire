package com.aspire.ums.cmdb.assessment;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemPageRequest;
import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemRequest;
import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemResp;
import com.aspire.ums.cmdb.common.PageBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IEquipmentProblemAPI
 * Author:   zhu.juwang
 * Date:     2019/5/6 11:19
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "设备问题接口类")
@RequestMapping("/v1/cmdb/assessment")
public interface IEquipmentProblemAPI {
	
	
	 /**
     *  新增设备问题
     * @return 模型列表
     */
    @PostMapping(value = "/insertEquipmentProblem" )
    @ApiOperation(value = "新增设备问题", notes = "新增设备问题", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String insertEquipmentProblem(@RequestBody EquipmentProblemRequest equipmentProblemRequest) ; 
    
    
    
    /**
     *  查询设备问题通过id
     * @return  
     */
    @GetMapping(value = "/selectEquipmentProblemById" )
    @ApiOperation(value = "查询设备问题通过id", notes = "查询设备问题通过id", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = EquipmentProblemResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public EquipmentProblemResp selectEquipmentProblemById( @RequestParam("id") String id );  
     
    
    
    /**
     *  修改设备问题
     * @return 模型列表
     */
    @PostMapping(value = "/updateEquipmentProblem" )
    @ApiOperation(value = "修改设备问题", notes = "修改设备问题", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String updateEquipmentProblem(@RequestBody EquipmentProblemRequest equipmentProblemRequest);  
    
    
    
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
     *  分页查询设备问题
     * @return 模型列表
     */
    @PostMapping(value = "/seleteEquipmentProblemByPage" ) 
    @ApiOperation(value = "分页查询设备问题", notes = "分页查询设备问题", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public PageBean<EquipmentProblemResp> seleteEquipmentProblemByPage( @RequestBody EquipmentProblemPageRequest equipmentProblemPageRequest ); 
    
    
    
    
    /**
     *  审批设备问题
     * @return 模型列表
     */
    @PostMapping(value = "/approveEquipmentProblem" )
    @ApiOperation(value = "审批设备问题", notes = "审批设备问题", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public String approveEquipmentProblem(@RequestBody EquipmentProblemRequest equipmentProblemRequest);  
    
    
    
    
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
    public List<EquipmentProblemResp> getEquipmentProblemList( @RequestBody EquipmentProblemRequest  equipmentProblemRequest  ) ;
   
    
    

}
