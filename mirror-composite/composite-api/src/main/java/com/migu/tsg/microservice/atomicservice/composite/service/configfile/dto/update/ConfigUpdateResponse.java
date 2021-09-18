package com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.update;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.list.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class ConfigUpdateResponse {

    @JsonProperty("commit_message")
    private String commitMessage;
    
    private List<Item> content;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("created_by")
    private String createdBy;
    
    @JsonProperty("current_version")
    private String currentVersion;
        
    private String description;

    private String id;

    private String name;
    
    private String namespace;    
    
    @JsonProperty("updated_at")
    private String updatedAt;
    
    @JsonProperty("updated_by")
    private String updatedBy;
    
    @JsonProperty("resource_actions")
    private List<String> resourceActions;
}
