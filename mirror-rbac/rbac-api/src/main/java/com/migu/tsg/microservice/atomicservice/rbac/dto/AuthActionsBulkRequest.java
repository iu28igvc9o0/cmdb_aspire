package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: AuthActionsBulkRequest.java <br>
 * 类描述: 批量添加资源操作的资源列表的请求对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月17日下午6:18:41 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthActionsBulkRequest {

    /**
     * 成员名称
     */
    private String username;

    /**
     * 空间名称
     */
    private String namespace;

    /**
     * 是否admin
     */
    @JsonProperty("is_admin")
    private Boolean isAdmin;

    /**
     * 资源类型
     */
    @JsonProperty("resource_type")
    private String resourceType;

    /**
     * 资源约束
     * 例子：{"res:cluster":"test", "res:project":"dev"}
     */
    private Map<String, String> context;

    /**
     * 资源集合
     * 例子：[{"uuid":"4e00bc66-7ef1-4382-8fb1-6341c1e913d5", "name":"test", "res:cluster":"dev", "res:project":"dev"},
     *       {"uuid":"4e00bc66-7ef1-4382-8fb1-6341c1e913d6", "name":"test2", "res:cluster":"dev", "res:project":"dev"}]
     */
    private List<Map<String, String>> resources;
}
