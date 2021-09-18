package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class OrgsCreateSubAccountsRequest {
    //用户名
    @JsonProperty("usernames")
    private List<String> usernames;
    //角色名
    @JsonProperty("roles")
    private List<OrgsRoles> roles;
    //密码
    private String password;

    //账号类型
    private String type;

    //描述
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
}
