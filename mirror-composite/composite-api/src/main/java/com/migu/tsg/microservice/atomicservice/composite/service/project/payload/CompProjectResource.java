package com.migu.tsg.microservice.atomicservice.composite.service.project.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CompProjectResource{
	private String id;
    private String type;
    private String uuid;
    private String name;
    private String status = "success";
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("project_uuid")
    private String projectUuid = "";
    @JsonProperty("namespace_name")
    private String namespaceName;
    @JsonProperty("parent_name")
    private String parentName;
    private boolean checked = true;
}