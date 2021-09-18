package com.migu.tsg.microservice.atomicservice.composite.service.project.payload;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CompProjectCreateRequest {
    private String name;
    private String template;
    private List<Map<String, Object>> roles;
    private List<Map<String, Object>> resources;
}
