package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class OrgsUpdateAccountsRequest {
    //角色名
    @JsonProperty("roles")
    private List<OrgsRoles> roles;
    //密码
    
    @JsonProperty("newPassword")
    private String password;

    //账号类型
    @JsonProperty("employeeType")
    private String type;

    //描述
    @JsonProperty("description")
    private String description;
    /**
     * 成员邮箱
     */
    private List<String> emailList;
//    /**
//     * 项目
//     * 例如：B1(包一),B2(包二)
//     */
//    private List<String> projects;
    /**
     * 项目名
     * 例如：B1(包一),B2(包二)
     */
    private List<String> projectNames;

    /** 用户名-用户批量授权. */
    @JsonProperty("userNames")
    private List<String> userNames;
}
