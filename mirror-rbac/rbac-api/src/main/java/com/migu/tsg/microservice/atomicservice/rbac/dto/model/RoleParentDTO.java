package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: RoleParentsDTO.java <br>
 * 类描述: 子角色和父角色对应的中间表的DTO实体类 ,查询一个角色详细信息时,使用该类来封装单个父角色信息 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月22日下午4:38:39<br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleParentDTO {

    /**
     * 子角色UUID
     */
    @JsonProperty("role_uuid")
    private String roleUuid;

    /**
     * 父角色UUID
     */
    @JsonProperty("parent_uuid")
    private String parentUuid;

    /**
     * 分配时间
     */
    @JsonProperty("assigned_at")
    private String assignedAt;

    /**
     * 角色对应的资源操作,Jackiro层调用RBAC层接口封装此字段,暂时保留
     * 例子：["role:assign", "role:create", "role:delete", "role:revoke", "role:update", "role:view"]
     */
    @JsonProperty("resource_actions")
    private List<String> resourceActions;

}
