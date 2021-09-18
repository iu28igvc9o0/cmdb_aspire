package com.migu.tsg.microservice.atomicservice.composite.service.rbac;

import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.CompSysManageReq;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.CompSysManageResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SysManageService
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/11/25
 * @Version V1.0
 **/
@Api(tags = "sys_manage", description = "系统列表管理")
@RequestMapping("/v1/sys_manage")
public interface ICompSysManageService {

    /**
     *
     * @return
     */
    @ApiOperation("查询所有")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<CompSysManageResp> listAll() ;

    /**
     *
     * @param sysManageReq
     * @return
     */
    @ApiOperation("新增")
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    CompSysManageResp insert(@RequestBody CompSysManageReq sysManageReq);

    /**
     *
     * @param name
     * @return
     */
    @ApiOperation("校验名称是否可用")
    @GetMapping(value = "/check/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<String, Object> checkName(@PathVariable("name") String name);

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    void delete(@PathVariable("id") String id);
}
