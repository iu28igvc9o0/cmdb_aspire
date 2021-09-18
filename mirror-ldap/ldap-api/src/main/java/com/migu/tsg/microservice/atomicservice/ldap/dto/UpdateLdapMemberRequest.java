package com.migu.tsg.microservice.atomicservice.ldap.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: ldap-api <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.dto <br>
* 类名称: UpdateLdapMemberRequest.java <br>
* 类描述: 修改命名空间(根账号)中的单个成员信息请求对象<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月6日下午4:04:15 <br>
* 版本: v1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLdapMemberRequest {

    /**
     * 成员手机号码
     */
	@JsonProperty("mobile")
    private String mobile;

    /**
     * 成员邮箱
     */
    @JsonProperty("mail")
    private String mail;
    
    @JsonProperty("employeeType")
    private String employeeType;
    
    @JsonProperty("description")
    private String description;

    /**
     * 成员登录密码
     */
    @JsonProperty("newPassword")
    private String newPassword;
    
    /**
     * 成员登录旧密码
     */
    @JsonProperty("old_password")
    private String oldPassword;

    @JsonProperty("enable")
    private Boolean enable;
    
    /**
     * 成员真实姓名
     */
    @JsonProperty("name")
    private String name;

    /**
     * 成员所属部门
     */
    @JsonProperty("new_password")
    private String dept;

    /**
     * 成员所属项目
     */
    private List<String> projects;
    
    @JsonProperty("isUpdateTime")
    boolean isUpdateTime=false;
}
