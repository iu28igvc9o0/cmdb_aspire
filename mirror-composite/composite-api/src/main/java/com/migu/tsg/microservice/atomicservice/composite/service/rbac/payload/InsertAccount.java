package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class InsertAccount {
    /**
     * accounts:新增用户rbac的入参类型CreateOrgAccount的内部属性之一
     * @since JDK 1.6
     */
    //成员用户名
    private String username;

     //成员邮箱
    private String email;    

    //账号类型
    private String type;

    //描述
    private String description;

     // 项目 
     //  例如：B1(包一),B2(包二)
    private List<String> projects;

    private String mobile;
}
