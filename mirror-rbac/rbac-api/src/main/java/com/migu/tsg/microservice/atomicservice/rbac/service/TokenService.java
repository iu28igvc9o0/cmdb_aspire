package com.migu.tsg.microservice.atomicservice.rbac.service;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.CommonResult;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserLoginForUmsVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @BelongsProject: mirror-rbac
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.rbac.service
 * @Author: baiwenping
 * @CreateTime: 2020-03-18 15:40
 * @Description: ${Description}
 */
@Api(tags = "token", description = "验证keycloak的token")
public interface TokenService {
    /**
     * 获取keycloak登录token
     * @param username
     * @return
     */
    @ApiOperation("获取keycloak登录token")
    @GetMapping(value = "/sso/token/{username}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<String, Object> token(@PathVariable("username") final String username);

    /**
     * 获取keycloak的用户信息，依据token
     * @return
     */
    @ApiOperation("获取keycloak的用户信息，依据token")
    @GetMapping(value = "/sso/userinfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<String, Object> userinfo(@RequestHeader(value = "Authorization") String authorization);
    
    /**
     * 深圳内部 UMS登录认证
     * @param type
     * @param token
     * @return
     */
    @ApiOperation("深圳内部UMS登录认证")
    @PostMapping(value = "/ums/auth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<String, Object> umsAuth(@RequestBody UserLoginForUmsVo param);
    
    /**
     * 深圳内部UMS 获取加密Key
     * @param loginKey
     * @return
     */
    @ApiOperation(value = "深圳内部获取签名密钥")
    @PostMapping(value = "/getSignkey", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult<String> getSignkey(@RequestParam("loginKey") String loginKey);
}
