package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.List;

import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AccountRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertAccountDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: CreateOrgAccountRequest.java <br>
 * 类描述: 新增成员请求对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月16日上午10:25:46 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrgAccountRequest {
	
	private String userId;

    /**
     * 成员用户名集合
     */
    private List<InsertAccountDTO> accounts;

    /**
     * 成员对应的角色集合
     */
    private List<AccountRoleDTO> roles;

    /**
     * 成员密码
     */
    private String password;

}
