package com.aspire.ums.cmdb.systemManager;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IOrgManagerAPI
 * Author:   zhu.juwang
 * Date:     2019/6/4 19:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/orgManager")
public interface IOrgManagerAPI {

    /**
     * 获取组织列表
     * @param pageNum 当前页数
     * @param pageSize 每页显示数量
     * @param pid 父组织ID
     * @param orgName 组织名称
     * @param orgType 组织类型
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取组织列表", notes = "获取组织列表", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<OrgManager> getAllOrgManagerData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                   @RequestParam(value = "pageSize",required = false) int pageSize,
                                                   @RequestParam(value = "pid",required = false) String pid,
                                                   @RequestParam(value = "orgName",required = false) String orgName,
                                                   @RequestParam(value = "orgType",required = false) String orgType);
    @GetMapping("/getAllOrg")
    @ApiOperation(value = "获取组织列表", notes = "获取组织列表", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<OrgManager> getAllOrg();

    @GetMapping("/getAllEipOrg")
    @ApiOperation(value = "获取EIP组织列表", notes = "获取EIP组织列表", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<OrgManager> getAllEipOrg();

    /**
     * 新增组织信息
     * @param orgManager 组织信息对象
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增组织信息", notes = "新增组织信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String insert(@RequestBody OrgManager orgManager);

    /**
     * 新增组织信息
     * @param orgManager 组织信息对象
     * @return
     */
    @PostMapping("/eip-add")
    @ApiOperation(value = "新增组织信息", notes = "新增组织信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String pureInsert(@RequestBody OrgManager orgManager);

    /**
     * 根据组织ID 查询组织信息
     * @param id 组织ID
     * @return
     */
    @GetMapping("/getById")
    @ApiOperation(value = "根据组织ID 查询组织信息", notes = "根据组织ID 查询组织信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = OrgManager.class),
            @ApiResponse(code = 500, message = "内部错误")})
    OrgManager getById (@RequestParam("id") String id);

    /**
     * 根据父组织ID, 查询组织信息
     * @param pid 父组织ID
     * @return
     */
    @GetMapping("/getByPid")
    @ApiOperation(value = "根据父组织ID, 查询组织信息", notes = "根据父组织ID, 查询组织信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<OrgManager> getByPid (@RequestParam("pid") String pid);

    /**
     * 获取父组织信息
     * @param id 组织ID
     * @return
     */
    @GetMapping("/getParentOrg")
    @ApiOperation(value = "获取父组织信息", notes = "获取父组织信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<OrgManager> getParentOrg(@RequestParam(value = "id",required = false) String id);

    /**
     * 更新组织信息
     * @param orgManager 组织对象
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新组织信息", notes = "更新组织信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "修改成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String update(@RequestBody OrgManager orgManager);

    /**
     * 删除组织信息
     * @param id 组织ID
     * @return
     */
    @DeleteMapping("/deleteOrg")
    @ApiOperation(value = "删除组织信息", notes = "删除组织信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "删除成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String deleteOrg(@RequestParam("id") String id);

    /**
     * 获取组织树列表
     * @return
     */
    @GetMapping("/loadTree")
    @ApiOperation(value = "获取组织树列表", notes = "获取组织树列表", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<OrgManager> loadTree();
    
    @GetMapping("/loadTreeDepBiz")
    @ApiOperation(value = "获取组织业务树", notes = "获取组织业务树", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map> loadTreeDepBiz();

    @GetMapping("/getDepInfo")
    @ApiOperation(value = "依据所属部门名称获取部门信息", notes = "依据所属部门名称获取部门信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    OrgManager getDepInfo(@RequestParam("department1") String department1,
                          @RequestParam(name = "department2",required = false) String department2);
}
