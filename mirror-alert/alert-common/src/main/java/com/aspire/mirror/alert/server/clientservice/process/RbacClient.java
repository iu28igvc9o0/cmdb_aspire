package com.aspire.mirror.alert.server.clientservice.process;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.alert.server.clientservice.payload.AlertUserVO;

import io.swagger.annotations.ApiOperation;

@FeignClient(value = "rbac", url = "http://10.1.203.100:28101")
public interface RbacClient {

    @ApiOperation("获取根账号中的成员信息列表")
    @GetMapping(value = "/v1/user/findByLdapId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    AlertUserVO findByLdapId(@RequestParam("ldap_id") String ldapId);
}
