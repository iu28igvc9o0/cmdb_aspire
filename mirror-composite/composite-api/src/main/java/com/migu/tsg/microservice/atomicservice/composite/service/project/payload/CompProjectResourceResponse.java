package com.migu.tsg.microservice.atomicservice.composite.service.project.payload;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CompProjectResourceResponse{
    private List<CompProjectResource> knamespaces;
    @JsonProperty("registry_projects")
    private List<CompProjectResource> registryProjects;
}