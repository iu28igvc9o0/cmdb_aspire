package com.aspire.mirror.composite.service.cmdb;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftPageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftPageResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftwareRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftwareResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.composite.service.cmdb
 * 类名称:    ICompMaintenSoftService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/20 10:14
 * 版本:      v1.0
 */

@Api(value = "软件维保信息")
@RequestMapping("${version}/cmdb/maintensoft")
public interface ICompMaintenSoftService {
	
	
	/**
     * 新增软件维保
     * @return String 返回  
     */
    @PostMapping(value = "/insertMaintenSoftware")
    @ApiOperation(value = "新增软件维保", notes = "新增软件维保", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    Map<String, Object> insertMaintenSoftware(@RequestBody CompMaintenSoftwareRequest compMaintenSoftwareRequest);
    
    
    
//    /**
//     * 修改软件维保
//     * @return String 返回
//     */
//    @PostMapping(value = "/updateMaintenSoftware")
//    @ApiOperation(value = "修改软件维保", notes = "修改软件维保", tags = {"maintenance API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
//    Map<String, Object> updateMaintenSoftware(@RequestBody CompMaintenSoftwareRequest compMaintenSoftwareRequest);
    
   
//    /**
//     * 批量更新软件维保
//     * @return String 返回
//     */
//    @PostMapping(value = "/batchUpdateMaintenSoftware")
//    @ApiOperation(value = "批量更新软件维保", notes = "批量更新软件维保", tags = {"maintenance API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
//    Map<String, Object> batchUpdateMaintenSoftware(@RequestBody CompMaintenSoftwareRequest compMaintenSoftwareRequest);
    
    
    
//    /**
//     *  查询软件维保通过id
//     * @return 软件维保
//     */
//    @GetMapping(value = "/selectMaintenSoftwareById" )
//    @ApiOperation(value = "查询软件维保", notes = "查询软件维保通过id", tags = {"maintenance API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
//    CompMaintenSoftwareResp selectMaintenSoftwareById( @RequestParam("id") String id );
//
   
    /**
     *  查询软件维保通过软件名称
     * @return
     */
    @GetMapping(value = "/selectMaintenSoftwareBySoftNmae" )
    @ApiOperation(value = "查询软件维保", notes = "查询软件维保通过软件名称", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    CompMaintenSoftwareResp selectMaintenSoftwareBySoftNmae(@RequestParam("project") String project , @RequestParam("softwareName") String softwareName );
    
     
    /**
     *  删除软件维保
     * @return  
     */
    @DeleteMapping(value = "/deleteMaintenSoftware" )
    @ApiOperation(value = "删除软件维保", notes = "删除软件维保", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    Map<String, Object> deleteMaintenSoftware( @RequestParam("ids") String ids );
    
    
    
    /**
     * 分页查询软件维保数据
     * @return  
     */
    @PostMapping(value = "/listMaintenSoftwareByPage" )  
    @ApiOperation(value = "查询分页数据", notes = "查询分页数据", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    PageResponse<CompMaintenSoftPageResp> selectMaintenSoftByPage( @RequestBody  CompMaintenSoftPageRequest  compMaintenSoftPageRequest ) ;
    
    
    
    /**
     *  导出软件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/downloadMaintenSoftware" )
    @ApiOperation(value = "导出软件维保", notes = "导出软件维保", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    void downloadMaintenSoftware( @RequestBody CompMaintenSoftPageRequest  compMaintenSoftPageRequest,  HttpServletResponse response);


    /**
     *  导出软件维保模版
     * @return 模型列表
     */
    @PostMapping(value = "/downloadTemplate" )
    @ApiOperation(value = "导出软件维保模版", notes = "导出软件维保模版", tags = {"maintenance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    void downloadTemplate(HttpServletResponse response);

    
}
