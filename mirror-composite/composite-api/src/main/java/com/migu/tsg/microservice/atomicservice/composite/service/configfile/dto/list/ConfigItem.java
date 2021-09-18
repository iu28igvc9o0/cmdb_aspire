package com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.list;

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
public class ConfigItem {
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("created_by")
    private String createdBy;
    
    @JsonProperty("current_version")
    private String currentVersion;
        
    private String description;

    private String id;
    
    @JsonProperty("commit_message")
    private String commitMessage;
    
    private String name;
    
    private String namespace;
    
    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("project_uuid")
    private String projectUuid;
    
    @JsonProperty("space_name")
    private String spaceName;

    @JsonProperty("space_uuid")
    private String spaceUuid;
    
    @JsonProperty("resource_actions")
    private List<String> resActionList;
    
    @JsonProperty("updated_at")
    private String updatedAt;
    
    private List<Item> content;
}
