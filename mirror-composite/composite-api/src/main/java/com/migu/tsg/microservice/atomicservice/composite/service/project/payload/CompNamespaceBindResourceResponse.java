package com.migu.tsg.microservice.atomicservice.composite.service.project.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CompNamespaceBindResourceResponse {
    private String uuid;
    private String name;
    private String type;
    @JsonProperty("created_at")
    private String createdAt;
}
