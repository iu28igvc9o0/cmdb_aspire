package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

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
public class CompRegionConfigCreateRequest {
    private String config_id;
    private String config_type;
    private String config_key;
    private Object config_value;
    private String config_description;
}
