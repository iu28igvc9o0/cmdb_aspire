package com.migu.tsg.microservice.atomicservice.composite.service.project.payload;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CompNamespaceCreateRequest {
    private String name;
    @JsonProperty("project_name")
    private String projectName;
    private List<Map<String, Object>> labels;
    private QuoteVo quota;
}
