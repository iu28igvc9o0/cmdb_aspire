package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: Members.java <br>
 * 类描述: 成员对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月16日上午10:04:48 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberDTO {

    /**
     * 成员ID
     */
    private String id;

    /**
     * 成员用户名
     */
    private String username;

    /**
     * 成员真实姓名
     */
    private String realname;

    /**
     * 成员邮箱
     */
    private String email;

    /**
     * 成员特权
     */
    private String privilege;

    /**
     * 同步时间
     */
    @JsonProperty("joined_at")
    private String joinedAt;
    
    /**
     * 项目
     * 例如：B1(包一),B2(包二)
     */
    private List<String> projects;

}
