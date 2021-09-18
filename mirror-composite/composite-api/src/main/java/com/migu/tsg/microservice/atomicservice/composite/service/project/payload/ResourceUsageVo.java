package com.migu.tsg.microservice.atomicservice.composite.service.project.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResourceUsageVo {
    @JsonProperty("compute_resource")
    private ComputeResourceVo computeResource;
}
