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
public class OrgsRolesPermissionResponse {
    
    @JsonProperty("role_uuid")
    private String roleUuid;
    
    @JsonProperty("role_name")
    private String roleName;
    
    @JsonProperty("user")
    private String user;

    private String namespace;
    
    @JsonProperty("assigned_at")
    private String assignedAt;
    
    @JsonProperty("project_name")
    private String projectName;
    
    @JsonProperty("project_uuid")
    private String projectUuid;
    
    @JsonProperty("space_name")
    private String spaceName;
    
    @JsonProperty("space_uuid")
    private String spaceUuid;
    
    @JsonProperty("resource_actions")
    private List<String> resourceActions;
}
