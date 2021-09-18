package com.migu.tsg.microservice.atomicservice.rbac.service;

import com.migu.tsg.microservice.atomicservice.rbac.dto.SysMenuReq;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysMenuResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SysMenuService
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/11/25
 * @Version V1.0
 **/
@Api(tags = "sys_menu", description = "菜单管理")
@RequestMapping("/v1/sys_menu")
public interface SysMenuService {
    /**
     *
     * @return
     */
    @ApiOperation("查询系统下所有菜单")
    @GetMapping(value = "/list/{sys_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<SysMenuResp> listAll (@PathVariable("sys_id") String sysId) ;

    /**
     *
     * @return
     */
    @ApiOperation("查询系统下菜单路由")
    @GetMapping(value = "/menu/{sys_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map listBySysName (@PathVariable("sys_name") String sysName) ;

    /**
     *
     * @param sysMenuReq
     * @return
     */
    @ApiOperation("新增")
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    SysMenuResp insert (@RequestBody SysMenuReq sysMenuReq);

    /**
     *
     * @param sysMenuReq
     * @return
     */
    @ApiOperation("新增")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    SysMenuResp update (@RequestBody SysMenuReq sysMenuReq);

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    void delete(@PathVariable("id") String id);

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail/{id}")
    @ApiOperation(value = "查看详情", notes = "查看详情")
    SysMenuResp selectById(@PathVariable("id") String id);
}
