package com.migu.tsg.microservice.atomicservice.ldap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
* 项目名称: ldap-service <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.config <br>
* 类名称: LdapConfiguration.java <br>
* 类描述: LDAP配置类<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月6日下午3:49:50 <br>
* 版本: v1.0
*/
@Configuration
public class LdapConfiguration {

    /**
     * 注入LdapContextSource对象
     * @return LdapContextSource
     */
    @Bean
    @ConfigurationProperties(prefix = "ldap.contextSource")
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        return contextSource;
    }

    /**
     * 注入LdapTemplate对象
     * @param contextSource 配置
     * @return LdapTemplate
     */
    @Bean
    public LdapTemplate ldapTemplate(ContextSource contextSource) {
        LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
        ldapTemplate.setIgnoreNameNotFoundException(true);
        return ldapTemplate;
    }
}
