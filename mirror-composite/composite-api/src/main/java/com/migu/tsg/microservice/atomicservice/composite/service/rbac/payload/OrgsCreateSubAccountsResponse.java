package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class OrgsCreateSubAccountsResponse {
    
    private String username;
    
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
    /**
     * 项目名
     * 例如：B1(包一),B2(包二)
     */
    private List<String> projectNames;
    
}


