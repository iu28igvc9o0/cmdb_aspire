package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

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
public class CompZoneUpdateRequest {
    private String zone_uuid;
    private String zone_name;
    private String endpoint;
    private String description;
    private Map<String,Object> meta_info;
}
