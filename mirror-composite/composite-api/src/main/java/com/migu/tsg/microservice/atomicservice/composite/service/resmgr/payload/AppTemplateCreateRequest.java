package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AppTemplateCreateRequest {
    private String name;
    private String description;
    @JsonProperty("space_name")
    private String spaceName;
    private String knamespace;
    private String template;
}
