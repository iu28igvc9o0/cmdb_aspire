package com.migu.tsg.microservice.atomicservice.composite.service.project.payload;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CompProjectResponse {
    private String uuid;
    private String name;
    private String status;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("created_by")
    private String createdBy;
    private List<String> resource_actions;
    
}
