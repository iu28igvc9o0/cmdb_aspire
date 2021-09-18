package com.migu.tsg.microservice.atomicservice.composite.service.rbac;

import com.migu.tsg.microservice.atomicservice.rbac.dto.UserClassifyReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * @author menglinjie
 */
@RequestMapping(value = "${version}/userClassify", produces = "application/json;charset=UTF-8")
@Api(value = "Composite userClassify service", description = "Composite userClassify service")
public interface IUserClassifyService {

    /**
     * saveUserClassify
     * @param req
     * @return
     */
    @PostMapping(value = "/saveUserClassify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增/编辑人员分类", notes = "新增/编辑人员分类")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),@ApiResponse(code = 500, message = "Unexpected error")})
    Object saveUserClassify(@RequestBody UserClassifyReq req);

    /**
     * delete
     * @param req
     * @return
     */
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除人员分类", notes = "删除人员分类")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),@ApiResponse(code = 500, message = "Unexpected error")})
    Object delete(@RequestBody UserClassifyReq req);

    /**
     * 根据系统查询人员分类列表
     * @param systemId
     * @return
     */
    @GetMapping(value = "/findListBySystemId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据系统查询人员分类列表", notes = "根据系统查询人员分类列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),@ApiResponse(code = 500, message = "Unexpected error")})
    Object findListBySystemId(@RequestParam(name = "systemId") String systemId);

    /**
     * 根据账号查询所属分类的定制化首页
     * @param ldapId
     * @return
     */
    @GetMapping(value = "/findListByLdapId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据账号查询所属分类的定制化首页", notes = "根据账号查询所属分类的定制化首页")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),@ApiResponse(code = 500, message = "Unexpected error")})
    Object findListByLdapId(@RequestParam(name = "ldapId") String ldapId);
}
