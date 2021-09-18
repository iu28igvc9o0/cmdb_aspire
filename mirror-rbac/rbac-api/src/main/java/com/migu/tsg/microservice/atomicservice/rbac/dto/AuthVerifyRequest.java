package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: AuthVerifyRequest.java <br>
 * 类描述: 权限验证请求对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月17日下午4:20:25 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthVerifyRequest {

    /**
     * 成员名称
     */
    private String username;

    /**
     * 空间名称/根账号
     */
    private String namespace;

    /**
     * 是否为管理员
     */
    @JsonProperty("is_admin")
    private Boolean isAdmin;

    /**
     * 单个资源对象
     * 例子：{"uuid":"4e00bc66-7ef1-4382-8fb1-6341c1e913d5", "name":"test", "res:cluster":"dev", "res:project":"dev"}
     */
    private Map<String, String> resource;

    /**
     * 资源操作
     * 例子：["service:*","service:create","service:update","service:view"]
     */
    private String action;

    /**
     * 资源约束
     * 例子：{"res:cluster":"test", "res:project":"dev"}
     */
    private Map<String, String> context;
}
