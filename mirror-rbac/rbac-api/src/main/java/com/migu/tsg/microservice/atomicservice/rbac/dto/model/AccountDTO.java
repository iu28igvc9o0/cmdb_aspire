package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.ldap.enums.LdapUserTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: AccountDTO.java <br>
 * 类描述: 成员对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月16日上午10:04:48 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDTO {
    /**
     * 成员用户名
     */
	@JsonProperty("username")
    private String username;

    /**
     * 成员类型
     */
	@JsonProperty("type")
    private LdapUserTypeEnum type;

    /**
     * 成员手机号码
     */
    private String mobile;
    /**
     * 描述
     */
	@JsonProperty("description")
    private String description;

    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    private String createdAt;

    /**
     * 更新时间
     */
    @JsonProperty("updated_at")
    private String updatedAt;

    private String name;

    /**
     * 项目
     * 例如：B1(包一),B2(包二)
     */
    private List<String> projects;

}
