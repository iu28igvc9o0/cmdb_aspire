package com.aspire.mirror.composite.service.cmdb;

import com.aspire.mirror.composite.service.inspection.payload.BizSystem;
import com.aspire.mirror.composite.service.inspection.payload.Concat;
import com.aspire.mirror.composite.service.inspection.payload.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.mirror.composite.service.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/23 17:29
 * 版本: v1.0
 */
@Api(value = "业务系统管理")
@RequestMapping("${version}/cmdb/bizSystem")
public interface IBizSystemService {
    @GetMapping(value = "/list")
    @ApiOperation(value = "业务系统管理列表", notes = "业务系统管理列表", tags = {"BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<BizSystem> getAllBizSystemData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                          @RequestParam(value = "pageSize",required = false) int pageSize,
                                          @RequestParam(value = "pid",required = false) String pid,
                                          @RequestParam(value = "department1",required = false) String department1,
                                          @RequestParam(value = "department2",required = false) String department2,
                                          @RequestParam(value = "bizName",required = false) String bizName,
                                          @RequestParam(value = "isdisable",required = false) String isdisable);
    
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加业务系统", notes = "添加业务系统", tags = {"BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    String insert(@RequestBody BizSystem bizSystem);
    
    @GetMapping(value = "/getById")
    @ApiOperation(value = "根据ID查询业务系统", notes = "根据ID查询业务系统", tags = {"BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    BizSystem getById (@RequestParam("id") String id);
    
    @PostMapping(value = "/update")
    @ApiOperation(value = "更新业务系统", notes = "更新业务系统", tags = {"BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    String update(@RequestBody BizSystem bizSystem);
    
    @DeleteMapping(value = "/deleteBiz")
    @ApiOperation(value = "下线业务系统", notes = "下线业务系统", tags = {"BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    String deleteBiz(@RequestParam("id") String id);
    
    @GetMapping(value = "/loadTree")
    @ApiOperation(value = "查询业务系统树", notes = "查询业务系统树", tags = {"BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<BizSystem> loadTree();
    
    @GetMapping(value = "/getConcatsById")
    @ApiOperation(value = "根据ID查询联系人", notes = "根据ID查询联系人", tags = {"BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Concat> getConcatsById(@RequestParam("id") String id);

    /**
     * 根据多个业务系统ID获取业务系统列表
     * @param ids
     * @return
     */
    @RequestMapping(value = "/getBizSystemByIds", method = RequestMethod.GET)
    @ApiOperation(value = "根据多个业务系统ID获取业务系统列表", notes = "根据多个业务系统ID获取业务系统列表", tags = {"CMDB Instance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getBizSystemByIds(@RequestParam("ids") String ids);
}
