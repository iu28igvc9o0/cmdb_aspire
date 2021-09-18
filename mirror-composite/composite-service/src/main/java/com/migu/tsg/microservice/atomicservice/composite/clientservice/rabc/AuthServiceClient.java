package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.rbac.service.AuthService;

/**
 * 资源鉴权调用客户端
 * Project Name:composite-service
 * File Name:AuthServiceClient.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc
 * ClassName: AuthServiceClient <br/>
 * date: 2017年8月28日 下午5:48:16 <br/>
 * 资源鉴权调用客户端
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@FeignClient(value = "rbac")
public interface AuthServiceClient extends AuthService {

}
