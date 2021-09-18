package com.migu.tsg.microservice.atomicservice.rbac.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsBulkRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsBulkResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ListActionsResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.service <br>
 * 类名称: AuthService.java <br>
 * 类描述: 所有验证和授权相关方法 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月17日下午6:05:00 <br>
 * 版本: v1.0
 */
@Api(tags = "auth", description = "所有验证和授权相关方法")
public interface AuthService {

    /**
     * 验证给定范围和资源属性是否可以执行特定操作
     * @param request 请求对象
     * @return 响应参数
     */
    @ApiOperation("权限验证")
    @PostMapping(value = "/v1/auth/verify", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AuthVerifyResponse authVerify(@RequestBody AuthVerifyRequest request);

    /**
     * 过滤资源列表,返回用户操作允许的列表数据,以及资源操作集合
     * @param request 请求对象
     * @return 响应对象
     */
    @ApiOperation("过滤资源列表")
    @PostMapping(value = "/v1/auth/filter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AuthFilterResponse> authFilter(@RequestBody AuthFilterRequest request);

    /**
     * 获取给定范围允许资源操作的列表
     * @param request 请求对象
     * @return 响应对象
     */
    @ApiOperation("获取给定范围允许资源操作的列表")
    @PostMapping(value = "/v1/auth/actions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> authActions(@RequestBody AuthActionsRequest request);

    /**
     * 批量添加资源操作的资源列表
     * @param request 请求对象
     * @return 响应对象
     */
    @ApiOperation(value = "批量添加资源操作的资源列表")
    @PostMapping(value = "/v1/auth/actions/bulk", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AuthActionsBulkResponse> authActionsBulk(@RequestBody AuthActionsBulkRequest request);

    /**
     * 筛选混合资源类型和操作的列表
     * @param request 请求对象
     * @return 响应对象
     */
    @ApiOperation("过滤混合资源列表")
    @PostMapping(value = "/v1/auth/filter/mixed", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AuthFilterMixedResponse> authFilterMixed(@RequestBody AuthFilterMixedRequest request);

    /**
     * 获取有权限的资源操作列表
     * @param username 成员名称
     * @param namespace 空间名称/根账号
     * @param isAdmin 是否为管理员
     * @return 响应对象
     */
    @ApiOperation("获取有权限的资源操作列表")
    @GetMapping(value = "/v1/auth/{namespace}/{username}/actions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ListActionsResponse> listActions(@PathVariable("username") final String username,
            @PathVariable("namespace") final String namespace,
            @RequestParam(value = "is_admin", defaultValue = "false") final Boolean isAdmin);
    
    @ApiOperation("获取用户的数据资源权限")
    @PostMapping(value = "/v1/auth/{namespace}/{username}/dataConstraints", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String,Set<String>> dataConstraints(@PathVariable("username") final String username,
            @PathVariable("namespace") final String namespace,
            @RequestParam(value = "roleType",required=false) final String roleType);

}
