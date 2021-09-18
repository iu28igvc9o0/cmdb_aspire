package com.migu.tsg.microservice.atomicservice.ldap.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.common.config <br>
* 类名称: RedisProperties.java <br>
* 类描述: REDIS配置对象<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年12月8日下午5:26:21 <br>
* 版本: v1.0
*/
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    /**
     * 资源模式集合缓存KEY前缀
     */
    private String redisKeyPrefixValidCodeList = "ldap-service-valid-codes";

    /**
     * 过期时间,单位秒
     */
    private int expiration = 3600;


}
