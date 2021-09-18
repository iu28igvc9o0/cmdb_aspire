package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: ParentRoleDTO.java <br>
 * 类描述: 【RBAC原子层】查询一个角色详细信息时,使用该类来封装单个父角色信息 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月25日下午2:59:16 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParentRoleDTO {

    /**
     * 父角色UUID
     */
    private String uuid;

    /**
     * 父角色name
     */
    private String name;

    /**
     * 分配时间
     */
    @JsonProperty("assigned_at")
    private String assignedAt;

}
