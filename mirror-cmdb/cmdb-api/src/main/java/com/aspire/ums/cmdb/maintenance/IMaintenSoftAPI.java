package com.aspire.ums.cmdb.maintenance;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftPageRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftPageResp;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftwareRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftwareResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


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
@Api(value = "软件维保接口类")
@RequestMapping("/v1/cmdb/maintensoft")
public interface IMaintenSoftAPI {
	
	 /**
     *  新增软件维保
     * @return 模型列表.3
     */
    @PostMapping(value = "/insertMaintenSoftware" )
    @ApiOperation(value = "新增软件维保", notes = "新增软件维保", tags = {"CMDB MaintenSoft API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> insertMaintenSoftware(@RequestBody MaintenSoftwareRequest maintenSoftwareRequest);


    /**
     * 查询软件维保根据软件名称
     * @param company
     * @param project
     * @param softwareName
     * @return
     */
    @GetMapping(value = "/selectMaintenSoftwareBySoftNmae" )
    @ApiOperation(value = "查询软件维保根据软件名称", notes = "查询软件维保根据软件名称", tags = {"CMDB MaintenSoft API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    MaintenSoftwareResp selectMaintenSoftwareBySoftNmae(@RequestParam("project") String project ,
                                                        @RequestParam("softwareName") String softwareName);

//    /**
//     *  查询软件维保详情
//     * @return
//     */
//    @GetMapping(value = "/selectMaintenSoftwareById" )
//    @ApiOperation(value = "查询软件维保通过id", notes = "查询软件维保通过id", tags = {"CMDB MaintenSoft API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
//            @ApiResponse(code = 500, message = "内部错误")})
//    MaintenSoftwareResp selectMaintenSoftwareById( @RequestParam("id") String id );
//
//

//    /**
//     *  批量更新软件维保
//     * @return 模型列表
//     */
//    @PostMapping(value = "/batchUpdateMaintenSoftware" )
//    @ApiOperation(value = "批量更新软件维保", notes = "批量更新软件维保", tags = {"CMDB MaintenSoft API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
//            @ApiResponse(code = 500, message = "内部错误")})
//    String batchUpdateMaintenSoftware(@RequestBody MaintenSoftwareRequest maintenSoftwareRequest) ;
     
    
    /**
     *  删除软件维保
     * @return 模型列表
     */
    @DeleteMapping(value = "/deleteMaintenSoftware" )
    @ApiOperation(value = "删除软件维保", notes = "删除软件维保", tags = {"CMDB MaintenSoft API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> deleteMaintenSoftware(@RequestParam("ids") String ids) ;
    

   
    /**
     *  分页查询软件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/listMaintenSoftwareByPage" )
    @ApiOperation(value = "分页查询软件维保", notes = "分页查询软件维保", tags = {"CMDB MaintenSoft API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageBean<MaintenSoftPageResp> selectMaintenSoftwareByPage( @RequestBody MaintenSoftPageRequest maintenSoftPageRequest ) ;
    
    
//    /**
//     *  查询软件维保列表
//     * @return 模型列表
//     */
//    @PostMapping(value = "/getMaintenSoftwareList" )
//    @ApiOperation(value = "查询软件维保列表", notes = "查询软件维保列表", tags = {"CMDB MaintenSoft API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
//            @ApiResponse(code = 500, message = "内部错误")})
//    List<MaintenSoftPageResp> getMaintenSoftwareList( @RequestBody MaintenSoftPageRequest maintenSoftPageRequest  ) ;
//
    
    /**
     *  批量保存软件维保
     * @return 模型列表
     */
    @PostMapping(value = "/insertMaintenSoftwareList" )
    @ApiOperation(value = "批量保存软件维保", notes = "批量保存软件维保", tags = {"CMDB MaintenSoft API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> insertMaintenSoftwareList( @RequestBody List<MaintenSoftwareRequest>  maintenSoftwareRequestList )  ;
    
    
    /**
     *  导出软件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/downloadMaintenSoftware" )
    @ApiOperation(value = "导出软件维保", notes = "导出软件维保", tags = {"CMDB MaintenSoft API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    void downloadMaintenSoftware( @RequestBody MaintenSoftPageRequest compMaintenSoftPageRequest ) ;

}
