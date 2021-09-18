package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.List;

import com.migu.tsg.microservice.atomicservice.ldap.enums.LdapUserTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: UpdateOrgResponseDTO.java <br>
 * 类描述: 根账号(公司)详细信息的DTO对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月18日上午11:05:53 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrgDetailDTO {

    /**
     * 成员名称
     */
    private String username;
    

    private String description;
    
    /**
     * 成员电话号码
     */
    private String mobile;
    
    
    private LdapUserTypeEnum userType;
    
    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * logo头像地址路径
     */
    private String logoFile;

    /**
     * 组织名称(空间名称/根账号名称)
     */
    private String name;

    /**
     * 组织下的成员集合
     */
    private List<MemberDTO> members;

    /**
     * 组织ID
     */
    private Integer id;

    /**
     * 更新时间
     */
    private String updatedAt;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 通用货币
     */
    private String currency;

    /**
     * 
     */
    private Integer featureFlags;

    /**
     * 项目
     * 例如：B1(包一),B2(包二)
     */
    private List<String> projects;
    
    /**
     * 根账号邮箱
     */
    private String email;

}
