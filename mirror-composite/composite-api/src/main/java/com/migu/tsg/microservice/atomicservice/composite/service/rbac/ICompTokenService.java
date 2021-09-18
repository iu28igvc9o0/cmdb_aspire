package com.migu.tsg.microservice.atomicservice.composite.service.rbac;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.CommonResult;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserLoginForUmsVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.service.rbac
 * @Author: baiwenping
 * @CreateTime: 2020-03-18 17:59
 * @Description: ${Description}
 */
@Api(tags = "token", description = "验证keycloak的token")
public interface ICompTokenService {
    /**
     * 获取keycloak登录token
     * @param type
     * @param token
     * @return
     */
    @ApiOperation("获取keycloak登录token")
    @GetMapping(value = "/sso/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<String, Object> token(@RequestParam("type") String type, @RequestParam("token") String token);

    /**
     * @Author huanggongrui
     * @CreateTime 2020-07-21 16:23:12
     * 获取keycloak登录token
     * @param request
     * @return
     */
    @ApiOperation("科管部统一认证获取keycloak登录token")
    @GetMapping(value = "/ums/sso")
    RedirectView tokenKg(HttpServletRequest request);
    
    /**
     * 深圳内部 UMS登录认证
     * @param type
     * @param token
     * @return
     */
    @ApiOperation("深圳内部 UMS登录认证")
    @PostMapping(value = "/ums/auth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<String, Object> umsAuth(@RequestBody UserLoginForUmsVo param);
    
    /**
     * 深圳内部UMS 获取加密Key
     * @param loginKey
     * @return
     */
    @PostMapping(value = "/getSignkey", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "深圳内部 获取签名密钥", httpMethod = "POST", notes = "获取签名密钥")
    public CommonResult<String> getSignkey(@ApiParam(required = true, name = "loginKey", value = "loginKey") @RequestParam("loginKey") String loginKey);

}
