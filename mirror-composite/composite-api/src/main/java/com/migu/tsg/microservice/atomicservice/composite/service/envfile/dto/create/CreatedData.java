package com.migu.tsg.microservice.atomicservice.composite.service.envfile.dto.create;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatedData {

    private List<List<String>> content;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("created_by")
    private String createdBy;
    
    private String description;
    
    private String name;
    
    private String namespace;
    
    private String operation;
    
    private String operator;
    
    @JsonProperty("project_uuid")
    private String projectUuid = "";
     
    @JsonProperty("space_name")
    private String spaceName = "";
    
    @JsonProperty("space_uuid")
    private String spaceUuid = "";
    
    @JsonProperty("updated_at")
    private String updatedAt;
    
    private String uuid;
}
