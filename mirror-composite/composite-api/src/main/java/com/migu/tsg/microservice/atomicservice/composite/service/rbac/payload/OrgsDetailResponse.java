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
public class OrgsDetailResponse {
    
    private Integer id;
    
    private String name;
    

    @JsonProperty("username")
    private String username;
    
    private String company;
    
    @JsonProperty("created_at")
    private String createTime;
    
    @JsonProperty("updated_at")
    private String updateTime;
    
    @JsonProperty("logo_file")
    private String logoFile;
    
    @JsonProperty("")
    private String currency;
    
    @JsonProperty("members")
    private Object memberList;
    
    @JsonProperty("feature_flags")
    private String featureFlags;
    
	@JsonProperty("userInfo")
	private UserResponse userInfo;
    
    private List<RbacRoleListItem> roles;

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
    /**
     * 邮箱（根账号）
     */
    private String email;
    
    
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class Member{
        private Integer id;
        
        private String username;
        
        private String realname;
        
        private String email;
        
        private String privilege;
        
        @JsonProperty("joined_at")
        private String joinedTime;
    }
    
    
}
