package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: RolesResponse.java <br>
 * 类描述: 【RBAC原子层】查询对于给定的UUID的角色列表响应对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日上午11:02:40 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RolesResponse {

    /**
     * 角色UUID
     */
	@JsonProperty("uuid")
    private String uuid;

    /**
     * 角色名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 是否为管理员角色
     */
    @JsonProperty("admin_role")
    private Boolean adminRole = false;

    /**
     * 空间名称
     */
    @JsonProperty("namespace")
    private String namespace;

    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    private String createdAt;

    /**
     * 更新时间
     */
    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("role_type")
    private Integer roleType;
    
    
    @JsonProperty("describe")
    private String describe;

}
