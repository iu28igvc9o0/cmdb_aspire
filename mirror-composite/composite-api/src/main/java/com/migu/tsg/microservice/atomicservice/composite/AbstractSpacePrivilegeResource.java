package com.migu.tsg.microservice.atomicservice.composite;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractSpacePrivilegeResource extends AbstractPrivilegeResource {
    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("project_uuid")
    private String projectUuid;

    @JsonProperty("space_name")
    private String spaceName;

    @JsonProperty("space_uuid")
    private String spaceUuid;
}
