package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: AccountRoleDTO.java <br>
 * 类描述: 新增成员时对应的角色集合中的角色对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月27日下午3:34:49 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountRoleDTO {

    /**
     * 角色UUID
     */
    private String uuid;

    /**
     * 角色名
     */
    private String name;

}
