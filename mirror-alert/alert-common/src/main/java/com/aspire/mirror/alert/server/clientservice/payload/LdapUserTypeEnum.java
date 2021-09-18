package com.aspire.mirror.alert.server.clientservice.payload;

/**
* 项目名称: ldap-api <br>
* 包: com.migu.tsg.msp.microservice.atomicservice.ldap.enums <br>
* 类名称: LdapUserTypeEnum.java <br>
* 类描述: LDAP用户类型<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月13日上午9:22:21 <br>
* 版本: v1.0
*/
public enum LdapUserTypeEnum {
    /**
     * 普通帐号(成员)
     */
    user,

    /**
     * 根账号
     */
    admin,
    
    donghuan,

    /**
     * 超级管理员
     */
    root;
}
