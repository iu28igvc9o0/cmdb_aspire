package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RolePermissionRequest {
    @JsonProperty("resource")
    private List<String> resourceList;
    
    @JsonProperty("actions")
    private List<String> resTypeActionList;
    
    @JsonProperty("constraints")
    private List<Map<String, String>> constraints;
}
