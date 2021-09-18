package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComHalcyonUpRequest {
    @JsonProperty("ip_address")
    private String ipAddress;
    @JsonProperty("resource_uuid")
    private String resourceUuid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("identifier")
    private Map<String, Object> identifier;
    @JsonProperty("configuration")
    private Map<String, Object> configuration;
    @JsonProperty("incarnation")
    private Integer incarnation;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "zh")
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "zh")
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("env_uuid_id")
    private String envUuid;
}
