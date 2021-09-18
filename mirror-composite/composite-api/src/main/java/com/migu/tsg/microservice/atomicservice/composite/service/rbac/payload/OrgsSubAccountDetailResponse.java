package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* Project Name:composite-api
* File Name:OrgsSubAccountDetailResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload
* ClassName: OrgsSubAccountDetailResponse <br/>
* date: 2017年11月6日 下午4:11:32 <br/>
* @author yangshilei
* @version 
* @since JDK 1.6
*/
    
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class OrgsSubAccountDetailResponse {
    //namespace判断子账号还是跟账号
    private String name;
    
    private String username;
    
    @JsonProperty("created_at")
    private String createTime;
    
    @JsonProperty("updated_at")
    private String updateTime;
    
    @JsonProperty("Resource_actions")
    private List<String> Resource_actions;
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
