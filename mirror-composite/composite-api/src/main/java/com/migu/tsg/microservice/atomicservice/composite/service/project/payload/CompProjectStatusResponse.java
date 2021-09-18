package com.migu.tsg.microservice.atomicservice.composite.service.project.payload;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CompProjectStatusResponse{
    private String uuid;
    private String name;
    private String status;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    private List<String> resource_actions;
    private List<Map<String, Object>> roles;
    private List<CompProjectResource> resources;
}