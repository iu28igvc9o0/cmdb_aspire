package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrgsCompanyRequest {
    /**
     * 公司名称
     */
    private String company;
    /**
     * 公司邮箱
     */
    private String email;
    /**
     * 登录密码
     */
    @JsonProperty("password")
    private String newPassword;
    
    /**
     * 登录旧密码
     */
    @JsonProperty("old_password")
    private String oldPassword;
}
