package com.migu.tsg.microservice.atomicservice.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.common.config <br>
 * 类名称: DruidProperties.java <br>
 * 类描述: 连接池属性对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月24日下午7:01:10 <br>
 * 版本: v1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    /**
     * keycloak的URL
     */
    private String url;
    
    private String username;
    
    private String password;
    
    private String realm;

    private String clientId;

    private String namespace;

    private String tokenUrl;
}
