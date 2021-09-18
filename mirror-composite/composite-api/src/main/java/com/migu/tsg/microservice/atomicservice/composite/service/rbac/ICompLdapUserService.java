package com.migu.tsg.microservice.atomicservice.composite.service.rbac;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @ClassName ICompLdapUserService
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/12/26
 * @Version V1.0
 **/
@Api(value = "${version}/ldap", description = "Retrieving the actions of the resources.")
public interface ICompLdapUserService {
    /**
     * 获取验证码
     */
    @ApiOperation("获取验证码")
    @GetMapping(value = "${version}/ldap/validCode")
    Map getValidCode();

    /**
     * 校验验证码
     * @param checkCode 输入数据
     */
    @ApiOperation("校验验证码")
    @GetMapping(value="/v1/ldap/checkValidCode")
    Map checkValidCode(@RequestParam(value = "checkCode") String checkCode);
}
