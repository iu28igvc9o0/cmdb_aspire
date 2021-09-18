package com.migu.tsg.microservice.atomicservice.common.client;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.ldap.service.LdapUserService;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.client <br>
 * 类名称: LdapServiceClient.java <br>
 * 类描述: LDAP接口客户端 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月24日下午5:44:52 <br>
 * 版本: v1.0
 */
@FeignClient(name = "ldap", url = "http://10.1.203.100:28102")
public interface LdapServiceClient extends LdapUserService {

}
