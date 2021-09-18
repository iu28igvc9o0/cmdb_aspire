package com.migu.tsg.microservice.atomicservice.composite.service.ping.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddExecLogRequest {

    private String namespace;

    @JsonProperty("resource_id")
    private String resourceId;

    @JsonProperty("resource_name")
    private String resourceName;

    private Detail detail;

    @JsonProperty("resource_type")
    private String resourceType;

}
