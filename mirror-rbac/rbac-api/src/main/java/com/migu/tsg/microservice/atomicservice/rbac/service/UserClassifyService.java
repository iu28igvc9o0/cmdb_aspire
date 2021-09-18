package com.migu.tsg.microservice.atomicservice.rbac.service;

import com.migu.tsg.microservice.atomicservice.rbac.dto.UserClassifyReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author menglinjie
 */
@Api(tags = "userClassify", description = "人员分类管理")
@RequestMapping("/v1/userClassify")
public interface UserClassifyService {

    /**
     * 新增/编辑人员分类
     * @param req
     * @return
     */
    @ApiOperation("新增/编辑人员分类")
    @PostMapping(value = "/saveUserClassify", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Object> saveUserClassify(@RequestBody UserClassifyReq req);


    /**
     * 删除人员分类
     * @param req
     * @return
     */
    @ApiOperation("删除人员分类")
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Object> delete(@RequestBody UserClassifyReq req);

    /**
     * 根据系统查询人员分类列表
     * @param systemId
     * @return
     */
    @ApiOperation("根据系统查询人员分类列表")
    @GetMapping(value = "/findListBySystemId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Object> findListBySystemId(@RequestParam(name = "systemId") String systemId);

    /**
     * 根据账号查询所属分类的定制化首页
     * @param ldapId
     * @return
     */
    @ApiOperation("根据账号查询所属分类的定制化首页")
    @GetMapping(value = "/findListByLdapId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<UserClassifyReq> findListByLdapId(@RequestParam(name = "ldapId") String ldapId);


}
