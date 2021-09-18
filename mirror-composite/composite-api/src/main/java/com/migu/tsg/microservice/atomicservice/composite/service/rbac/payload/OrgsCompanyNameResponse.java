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
public class OrgsCompanyNameResponse {
    
    private Integer id;
    
    private String name;
    
    private String company;
    /**
     * 公司邮箱
     */
    private String email;

    
    @JsonProperty("created_at")
    private String createTime;
    
    @JsonProperty("updated_at")
    private String updateTime;
    
    @JsonProperty("logo_file")
    private String logoFile;
    
    private String currency;
    
    private List<Object> members;
    
    @JsonProperty("feature_flags")
    private String featureFlags;
}
