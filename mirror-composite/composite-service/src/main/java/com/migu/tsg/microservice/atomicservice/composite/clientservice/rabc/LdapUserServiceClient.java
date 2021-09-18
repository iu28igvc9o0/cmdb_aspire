package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.ldap.service.LdapUserService;

/**
 * @ClassName LdapUserServiceClient
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/12/26
 * @Version V1.0
 **/
@FeignClient(name = "ldap", url = "http://10.1.203.100:28102")
public interface LdapUserServiceClient extends LdapUserService {
}
