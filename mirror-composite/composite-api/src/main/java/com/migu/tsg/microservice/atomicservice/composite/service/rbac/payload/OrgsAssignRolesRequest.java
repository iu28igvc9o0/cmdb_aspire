package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrgsAssignRolesRequest {
   
    @JsonProperty("name")
    private String roleName;
    

    @JsonProperty("role_id")
    private String roleId;
}
