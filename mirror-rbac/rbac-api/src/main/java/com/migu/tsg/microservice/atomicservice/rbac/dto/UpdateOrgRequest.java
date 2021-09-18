package com.migu.tsg.microservice.atomicservice.rbac.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: UpdateOrgRequest.java <br>
 * 类描述: 修改公司信息请求对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月16日上午9:45:29 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrgRequest {

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
