package com.migu.tsg.microservice.atomicservice.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: InsertParentRoleRequest.java <br>
 * 类描述: 【RBAC原子层】新增单个父角色的请求对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日下午8:23:33 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertParentRoleRequest {

    /**
     * 父角色UUID
     */
    private String uuid;

    /**
     * 父角色名
     */
    private String name;

}
