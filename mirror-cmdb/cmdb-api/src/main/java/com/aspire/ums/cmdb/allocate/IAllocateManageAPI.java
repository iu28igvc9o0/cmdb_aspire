package com.aspire.ums.cmdb.allocate;

import com.aspire.ums.cmdb.allocate.payload.*;
import com.aspire.ums.cmdb.common.PageBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IAllocateManageAPI
 * Author:   zhu.juwang
 * Date:     2019/5/6 11:19
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "IP分配接口类")
@RequestMapping("/v1/cmdb/allocate")
public interface IAllocateManageAPI {
    /**
     *  插入域名
     * @return 模型列表
     */
    @PostMapping(value = "/insertDomain" )
    @ApiOperation(value = "新增域名", notes = "新增域名", tags = {"CMDB Allocate Manager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String insertDomain(@RequestBody AllocateDomainRequest allocateDomainRequest);

    /**
     *  查询域名通过id
     * @return 域名
     */
    @GetMapping(value = "/selectDomainById" )
    @ApiOperation(value = "查询域名", notes = "查询域名通过id", tags = {"CMDB Allocate Manager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = AllocateDomainResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    AllocateDomainResp selectAllocateDomainById(@RequestParam("id") String id );

    /**
     *  查询单个域名通过名称
     * @return 域名
     */
    @GetMapping(value = "/selectDomainByName" )
    @ApiOperation(value = "查询域名", notes = "查询单个域名通过名称", tags = {"CMDB Allocate Manager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = AllocateDomainResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    AllocateDomainResp selectAllocateDomainByName( @RequestParam("hostnet") String hostnet, @RequestParam("domain") String domain );

    /**
     *  查询单个网段通过名称
     * @return 域名
     */
    @GetMapping(value = "/selectNetsegmentByName" )
    @ApiOperation(value = "查询域名", notes = "查询单个网段通过名称", tags = {"CMDB Allocate Manager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = AllocateNetSegmentResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    AllocateNetSegmentResp selectAllocateNetsegmentByName(@RequestParam("hostnet") String hostnet, @RequestParam("netseg") String netseg );

    /**
     *  修改域名
     * @return 模型列表
     */
    @PostMapping(value = "/updateDomain" )
    @ApiOperation(value = "修改域名", notes = "修改域名", tags = {"CMDB Allocate Manager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "修改成功", response = AllocateDomainResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String updateDomain(@RequestBody AllocateDomainRequest allocateDomainRequest);

    /**
     *  删除域名
     * @return 模型列表
     */
    @DeleteMapping(value = "/deleteDomain" )
    @ApiOperation(value = "删除域名", notes = "删除域名", tags = {"CMDB Allocate Manager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String deleteDomain( @RequestParam("id") String id );

    /**
     *  分页查询域名数据
     * @return 模型列表
     */
    @PostMapping(value = "/listDomaineByPage" )
    @ApiOperation(value = "查询域名", notes = "分页查询域名数据", tags = {"CMDB Allocate Manager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = PageBean.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageBean<AllocateManagePageResp> selectAllocateManageByPage( @RequestBody AllocateManagePageRequest allocateManagePageRequest );

    /**
     * 查询菜单
     * @return
     */
    @GetMapping(value = "/selectAllocateMenu" )
    @ApiOperation(value = "查询菜单", notes = "查询菜单", tags = {"CMDB Allocate Manager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = PageBean.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Menu> selectMenu();
}
