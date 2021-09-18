package com.migu.tsg.microservice.atomicservice.composite.service.envfile.dto.specified;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SpecifiedData {

    private List<List<String>> content;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("created_by")
    private String createdBy;
    
    private String description;
    
    private String name;
    
    private String namespace;
 
    @JsonProperty("resource_actions")
    private List<String> resourceActions;
    
    @JsonProperty("updated_at")
    private String updatedAt;
    
    private String uuid;
    
    
}
