package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 项目名称:  咪咕微服务运营平台
 * 包:       com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
 * 类名称:    com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CompZoneCreateRequest.java
 * 类描述:
 * 创建人:    zhu.juwang
 * 创建时间:  2018/05/15 14:40
 * 版本:      v1.0
 */
@Data
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class CompZoneCreateRequest {
    @JsonProperty(value = "zone_uuid")
    private String zone_uuid;
    @JsonProperty(value = "zone_name")
    private String zone_name;
    @JsonProperty(value = "endpoint")
    private String endpoint;
    @JsonProperty(value = "description")
    private String description;
    @JsonProperty(value = "meta_info")
    private Map<String,Object> meta_info;
}
