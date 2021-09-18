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
public class OrgsSubAccountsListResponse {
    @JsonProperty("count")
    private Integer totalCount;
    
    @JsonProperty("page_size")
    private Integer pageSize;
    
    @JsonProperty("num_pages")
    private Integer numPages;
    
    @JsonProperty("results")
    private List<Result> results;
    
    //resource_actions改成result

    
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @JsonInclude(Include.NON_NULL)
    public static class Result {
    	
    	@JsonProperty("userInfo")
    	private UserResponse userInfo;
        
        private List<RbacRoleListItem> roles;
        
    	@JsonProperty("username")
        private String username;
    	
    	@JsonProperty("description")
        private String description;
        
        // organizations.Account if is subaccount, organizations.LDAPAccount if it is LDAP synced
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("created_at")
        private String createTime;
       
        @JsonProperty("updated_at")
        private String updateTime;
        //此处为对比公网后添加的resource_actions
        @JsonProperty("resource_actions")
        private  List<String> resultActions;
        
        // 项目 ：例如：B1(包一),B2(包二)，取自RBAC原子层
        private List<String> projects;
        // 项目名称 ：例如：B1(包一),B2(包二)，取自yml文件
        private List<String> projectNames;
    }
}
