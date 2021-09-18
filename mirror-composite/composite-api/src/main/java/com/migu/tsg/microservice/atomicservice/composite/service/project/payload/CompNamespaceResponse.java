package com.migu.tsg.microservice.atomicservice.composite.service.project.payload;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CompNamespaceResponse {
    private String uuid;
    private String name;
    private String status;
    @JsonProperty("created_at")
    private String createdAt;
    private List<Map<String, Object>> labels;
    private QuoteVo quota;
    @JsonProperty("resource_usage")
    private ResourceUsageVo resourceUsage;
    @JsonProperty("resource_actions")
    private List<String> resourceActions;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("created_by")
    private String createdBy;
}
