package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: ListAccountRolesResponse.java <br>
 * 类描述: 查询成员已关联的角色列表的响应对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月22日下午6:51:37 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListAccountRolesResponse {

    /**
     * 角色UUID
     */
    @JsonProperty("role_uuid")
    private String roleUuid;

    /**
     * 角色名称
     */
    @JsonProperty("role_name")
    private String roleName;

    /**
     * 用户名称
     */
    private String user;

    /**
     * 空间名称
     */
    private String namespace;

    /**
     * 分配时间
     */
    @JsonProperty("assigned_at")
    private String assignedAt;

    /**
     * 项目名称
     */
    @JsonProperty("project_name")
    private String projectName;

    /**
     * 项目UUID
     */
    @JsonProperty("project_uuid")
    private String projectUuid;

    /**
     * 资源空间名称
     */
    @JsonProperty("space_name")
    private String spaceName;

    /**
     * 资源空间UUID
     */
    @JsonProperty("space_uuid")
    private String spaceUuid;

    /**
     * 角色对应的资源操作,jakiro层封装此字段,暂时保留
     * 例子：["role:assign","role:create","role:delete","role:revoke","role:update","role:view"]
     */
    @JsonProperty("resource_actions")
    private List<String> resourceActions;
}
