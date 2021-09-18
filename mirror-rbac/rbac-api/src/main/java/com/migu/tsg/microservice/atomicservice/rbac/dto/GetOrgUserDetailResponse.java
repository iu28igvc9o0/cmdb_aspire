package com.migu.tsg.microservice.atomicservice.rbac.dto;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.ldap.enums.LdapUserTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: GetOrgUserDetailResponse.java <br>
 * 类描述: 查询根账号(组织/公司)中单个成员详细信息响应对象<br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年9月13日上午10:02:28 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetOrgUserDetailResponse {

    /**
     * 成员名称
     */
    private String username;
    

    private String description;
    
    private LdapUserTypeEnum userType;

    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    private String createdAt;

    /**
     * logo头像地址路径
     */
    @JsonProperty("logo_file")
    private String logoFile;

    /**
     * 组织名称(空间名称/根账号名称)
     */
    private String name;

    /**
     * 更新时间
     */
    @JsonProperty("updated_at")
    private String updatedAt;

    /**
     * 公司名称
     */
    private String company;
    
    /**
     * 项目(根账号)
     * 例如：B1(包一),B2(包二)
     */
    private List<String> projects;
    
    /**
     * 成员邮箱
     */
    private String email;

    private String mobile;
}
