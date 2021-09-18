package com.migu.tsg.microservice.atomicservice.rbac.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: UpdateSubAccountPasswordRequest.java <br>
 * 类描述: 成员更新密码请求对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月14日下午2:36:28 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateSubAccountPasswordRequest {
    /**
     * 成员新密码
     */
    private String password;

    /**
     * 成员旧密码
     */
    @JsonProperty("old_password")
    private String oldPassword;
}
