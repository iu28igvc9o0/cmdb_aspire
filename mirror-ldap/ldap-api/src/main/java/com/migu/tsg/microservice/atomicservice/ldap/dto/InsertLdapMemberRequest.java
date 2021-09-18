package com.migu.tsg.microservice.atomicservice.ldap.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: ldap-api <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.dto <br>
* 类名称: InsertLdapMemberRequest.java <br>
* 类描述: 新增命名空间(根账号)中的单个成员信息请求对象<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月6日下午4:04:15 <br>
* 版本: v1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertLdapMemberRequest {

    /**
     * 成员账号(成员用户名,LDAP全组织唯一)
     */
    private String username;

    /**
     * 成员手机号码
     */
    private String mobile;
    

    //账号类型
    private String type;

    //描述
    private String description;

    /**
     * 成员邮箱
     */
    private String mail;

    /**
     * 成员登录密码
     */
    private String password;

    /**
     * 成员真实姓名
     */
    private String name;

    /**
     * 成员所属部门
     */
    private String dept;

    /**
     * 成员所属项目
     */
    private List<String> projects;
}
