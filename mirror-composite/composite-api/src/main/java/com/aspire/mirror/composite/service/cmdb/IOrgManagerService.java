package com.aspire.mirror.composite.service.cmdb;

import com.aspire.mirror.composite.service.inspection.payload.Result;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
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
 * 创建时间: 2019/5/28 09:09
 * 版本: v1.0
 */
@Api(value = "组织管理")
@RequestMapping("${version}/cmdb/orgManager")
public interface IOrgManagerService {
    
    @GetMapping(value = "/list")
    @ApiOperation(value = "组织管理列表", notes = "组织管理列表", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<OrgManager> getAllOrgManagerData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                   @RequestParam(value = "pageSize",required = false) int pageSize,
                                                   @RequestParam(value = "pid",required = false) String pid,
                                                   @RequestParam(value = "orgName",required = false) String orgName,
                                                   @RequestParam(value = "orgType",required = false) String orgType);
    
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加组织", notes = "添加组织", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    String insert(@RequestBody OrgManager orgManager);
    
    @GetMapping(value = "/getById")
    @ApiOperation(value = "根据ID查询组织", notes = "根据ID查询组织", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    OrgManager getById (@RequestParam("id") String id);
    
    @GetMapping(value = "/getByPid")
    @ApiOperation(value = "根据父ID查询组织", notes = "根据父ID查询组织", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<OrgManager> getByPid (@RequestParam("pid") String pid);
    
    @PostMapping(value = "/update")
    @ApiOperation(value = "更新组织", notes = "更新组织", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    String update(@RequestBody OrgManager orgManager);
    
    @DeleteMapping(value = "/deleteOrg")
    @ApiOperation(value = "删除组织", notes = "删除组织", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    String deleteOrg(@RequestParam("id") String id);
    
    @GetMapping(value = "/loadTree")
    @ApiOperation(value = "查询组织树", notes = "查询组织树", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<OrgManager> loadTree();
    
    @GetMapping(value = "/loadTreeDepBiz")
    @ApiOperation(value = "查询组织业务树", notes = "查询组织业务树", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map> loadTreeDepBiz();

    @GetMapping("/getAllOrg")
    @ApiOperation(value = "获取组织列表", notes = "获取组织列表", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<OrgManager> getAllOrg();

    @GetMapping("/getDepInfo")
    @ApiOperation(value = "依据所属部门名称获取部门信息", notes = "依据所属部门名称获取部门信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = OrgManager.class),
            @ApiResponse(code = 500, message = "内部错误")})
    OrgManager getDepInfo(@RequestParam("department1") String department1,
                          @RequestParam(name = "department2",required = false) String department2);
}
