package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateOrgAccount {

    /**
     * accounts:新增用户rbac的入参类型VO
     * @since JDK 1.6
     */
    
    //成员用户信息集合（用户名、邮箱、项目）     
    private List<InsertAccount> accounts;
    
    //成员角色
    private List<OrgsRoles> roles;
    
    //用户密码
    private String password;
}
