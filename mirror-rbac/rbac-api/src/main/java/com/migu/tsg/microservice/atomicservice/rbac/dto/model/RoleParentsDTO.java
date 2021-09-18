package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: RoleParentsDTO.java <br>
 * 类描述: 父角色信息,新增角色时,使用的该类来接受单个父角色信息 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月25日下午4:45:48 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleParentsDTO {

    /**
     * 父角色UUID
     */
    private String uuid;

    /**
     * 父角色名
     */
    private String name;

    /**
     * 分配时间
     */
    @JsonProperty("assigned_at")
    private String assignedAt;

}
