package com.migu.tsg.microservice.atomicservice.ldap.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.ldap.enums.LdapUserTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: ldap-api <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.dto <br>
* 类名称: GetLdapUserResponse.java <br>
* 类描述: 获取LDAP用户响应对象<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月6日下午4:04:15 <br>
* 版本: v1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLdapUserResponse {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 手机号码
     */
    private String mobile;
    
    /**
     * 邮箱
     */
    private String mail;
    
    /**
     * 公司名称
     */
    private String company;
    
    /**
     * 部门
     */
    private String dept;
    
    /**
     * 头像信息
     */
    @JsonProperty("jpeg_photo")
    private String jpegPhoto;
    
    /**
     * 所属项目列表
     */
    private List<String> projects;
    
    /**
     * 全部项目列表
     */
    @JsonProperty("all_projects")
    private List<String> allProjects;

    /**
     * 用户类型, root表示超级用户;admin表示根帐号;user表示普通用户
     */
    @JsonProperty("user_type")
    private LdapUserTypeEnum userType;
    
    /**
     * (命名空间)根帐号UID
     * 只有成员有该值,表示该成员所属命名空间
     */
    private String namespace;
    
    /**
     * 描述
     */
	@JsonProperty("description")
    private String description;
    
    /**
     * 创建时间
     */
    @JsonProperty("create_time")
    private String createTime;
    
    /**
     * 修改时间
     */
    @JsonProperty("update_time")
    private String updateTime;
}
