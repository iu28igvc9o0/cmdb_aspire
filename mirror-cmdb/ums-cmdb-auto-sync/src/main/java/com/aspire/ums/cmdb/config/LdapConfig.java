package com.aspire.ums.cmdb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * @Author: huanggongrui
 * @Description: ldap配置类
 * @Date: create in 2020/8/17 16:00
 */
@Configuration
public class LdapConfig {

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
