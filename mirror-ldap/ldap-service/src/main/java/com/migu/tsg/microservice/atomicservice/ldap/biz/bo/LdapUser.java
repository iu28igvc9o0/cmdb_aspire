package com.migu.tsg.microservice.atomicservice.ldap.biz.bo;

import java.util.List;

import com.migu.tsg.microservice.atomicservice.ldap.enums.LdapUserTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: LDAP-service <br>
* 包: com.migu.tsg.microservice.atomicservice.LDAP.biz.bo <br>
* 类名称: LdapUser.java <br>
* 类描述: LDAP用户实体类<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月6日下午4:26:29 <br>
* 版本: v1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LdapUser {

    /**
     * 用户名,用于登录
     * 对应LDAP中的uid字段和cn字段
     */
    private String username;
    

    /**
     * 描述
     */
    private String description;

    /**
     * 密码
     * 对应LDAP中的userPassword字段
     */
    private String password;

    /**
     * 用户类型,root表示超级用户,admin表示根帐号,user表示普通用户
     * 对应LDAP中的employeeType字段
     */
    private LdapUserTypeEnum userType;

    /**
     * (命名空间)根帐号UID
     * 只有成员有该值,表示该成员所属命名空间
     * 对应LDAP中的ou字段
     */
    private String namespace;

    /**
     * 真实姓名
     * 对应LDAP中的sn字段
     */
    private String name;

    /**
     * 手机号,对应LDAP中的mobile字段
     */
    private String mobile;

    /**
     * 邮箱,对应LDAP中的mail字段
     */
    private String mail;

    /**
     * 公司,对应LDAP中的o字段
     */
    private String company;

    /**
     * 部门,对应LDAP中的departmentNumber字段
     */
    private String dept;

    /**
     * 头像信息,对应LDAP中的jpegPhoto字段
     */
    private String jpegPhoto;

    /**
     * 创建时间,对应LDAP中的自定义字段createTime
     */
    private String createTime;

   /**
    * 更新时间,对应LDAP中的自定义字段updateTime
    */
    private String updateTime;

    /**
     * 所属项目,对应LDAP中的自定义字段project
     */
    private List<String> projects;
}
