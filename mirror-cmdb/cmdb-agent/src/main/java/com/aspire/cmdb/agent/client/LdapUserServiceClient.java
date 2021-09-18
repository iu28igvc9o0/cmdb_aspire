package com.aspire.cmdb.agent.client;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.ldap.service.LdapUserService;

/**
 * @ClassName LdapUserServiceClient
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/12/26
 * @Version V1.0
 **/
// @FeignClient(name = "ldap")
@FeignClient(name = "ldap", url = "http://10.1.203.100:28102")
public interface LdapUserServiceClient extends LdapUserService {
}
