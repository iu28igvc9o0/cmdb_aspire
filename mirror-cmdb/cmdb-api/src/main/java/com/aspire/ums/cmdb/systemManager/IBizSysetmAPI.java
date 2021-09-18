package com.aspire.ums.cmdb.systemManager;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.systemManager.payload.BizSystem;
import com.aspire.ums.cmdb.systemManager.payload.Concat;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IBizSysetmAPI
 * Author:   zhu.juwang
 * Date:     2019/6/4 19:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/bizSystem")
public interface IBizSysetmAPI {

    /**
     * 获取业务系统列表
     * @param pageNum 当前页数
     * @param pageSize 每页显示数量
     * @param pid 父业务ID
     * @param bizName 业务系统名称
     * @param isdisable 是否禁用
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取业务系统列表", notes = "获取业务系统列表", tags = {"CMDB BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<BizSystem> getAllBizSystemData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                          @RequestParam(value = "pageSize",required = false) int pageSize,
                                          @RequestParam(value = "pid",required = false) String pid,
                                          @RequestParam(value = "department1",required = false) String department1,
                                          @RequestParam(value = "department2",required = false) String department2,
                                          @RequestParam(value = "bizName",required = false) String bizName,
                                          @RequestParam(value = "isdisable",required = false) String isdisable);

    /**
     * 获取业务系统树列表
     * @return
     */
    @GetMapping("/loadTree")
    @ApiOperation(value = "获取业务系统树列表", notes = "获取业务系统树列表", tags = {"CMDB BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<BizSystem> loadTree();

    /**
     * 新增业务系统信息
     * @param bizSystem 业务系统对象
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增部门信息", notes = "新增部门信息", tags = {"CMDB BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String insert(@RequestBody BizSystem bizSystem);

    /**
     * 根据业务ID获取业务信息
     * @param id 业务ID
     * @return
     */
    @GetMapping("/getById")
    @ApiOperation(value = "根据业务ID获取业务信息", notes = "根据业务ID获取业务信息", tags = {"CMDB BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = BizSystem.class),
            @ApiResponse(code = 500, message = "内部错误")})
    BizSystem getById (@RequestParam("id") String id);

    /**
     * 根据业务ID 查询业务联系人信息
     * @param id 业务ID
     * @return
     */
    @GetMapping("/getConcatsById")
    @ApiOperation(value = "根据业务ID获取业务信息", notes = "根据业务ID获取业务信息", tags = {"CMDB BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Concat> getConcatsById(@RequestParam("id") String id);

    /**
     * 修改业务信息
     * @param bizSystem 业务信息对象
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改业务信息", notes = "修改业务信息", tags = {"CMDB BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "修改成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String update(@RequestBody BizSystem bizSystem);

    /**
     * 删除业务系统
     * @param id 业务系统ID
     * @return
     */
    @DeleteMapping("/deleteBiz")
    @ApiOperation(value = "删除业务信息", notes = "删除业务信息", tags = {"CMDB BizSystem API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "删除成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String deleteBiz(@RequestParam("id") String id);

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
