package com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetricCatalogResponse {
    private Integer status_code;
    private Object data;
}
