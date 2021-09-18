package com.migu.tsg.microservice.atomicservice.composite;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public abstract class AbstractPrivilegeResource {
    @JsonProperty("resource_actions")
    private List<String> resourceActions;

    public abstract String getPrivResUuid();

    public abstract void setPrivResUuid(String uuid);
}
