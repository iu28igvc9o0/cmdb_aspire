package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: CreateOrgAccountDTO.java <br>
 * 类描述: 新增成员DTO对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月18日上午11:48:42 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrgAccountDTO {

    /**
     * 成员用户名
     */
    private String username;

    /**
     * 成员密码
     */
    private String password;

    /**
     * 成员邮箱
     */
    private String email;

    /**
     * 项目
     * 例如：B1(包一),B2(包二)
     */
    private List<String> projects;

}
