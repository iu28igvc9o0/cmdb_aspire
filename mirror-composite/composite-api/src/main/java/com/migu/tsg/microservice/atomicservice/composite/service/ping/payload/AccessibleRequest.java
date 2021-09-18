package com.migu.tsg.microservice.atomicservice.composite.service.ping.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AccessibleRequest {

    private String username;

    private String privilege;

    private String namespace;

    private String application;

    @JsonProperty("res_name")
    private String resName;

    private String project;
}
