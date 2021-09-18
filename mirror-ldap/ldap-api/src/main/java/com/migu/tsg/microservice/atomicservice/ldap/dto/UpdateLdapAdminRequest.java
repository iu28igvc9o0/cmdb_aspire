package com.migu.tsg.microservice.atomicservice.ldap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: ldap-api <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.dto <br>
* 类名称: UpdateLdapAdminRequest.java <br>
* 类描述: 修改根账号信息请求对象<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月6日下午4:04:15 <br>
* 版本: v1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLdapAdminRequest {

    /**
     * 公司名称
     */
    private String company;
    
    /**
     * 公司邮箱
     */
    private String mail;

    /**
     * 公司头像(Base64字符串),可选
     */
    @JsonProperty("jpeg_photo")
    private String jpegPhoto;
    
    /**
     * 登录密码
     */
    @JsonProperty("new_password")
    private String newPassword;
    
    /**
     * 登录旧密码
     */
    @JsonProperty("old_password")
    private String oldPassword;

}
