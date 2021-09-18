package com.migu.tsg.microservice.atomicservice.composite.service.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//张庆
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class EventInfo {
    private String time;

//    @JsonProperty("created_at")
//    private String createdAt;
    
    private EventDetail detail;
    
    @JsonProperty("log_level")
    private int logLevel;
    
    @JsonProperty("template_id")
    private String templateId;

    @JsonProperty("resource_type")
    private String resourceType;

    @JsonProperty("resource_name")
    private String resourceName;
//获取事件显示不了resource_uuid，为匹配前端改成resource_id
    @JsonProperty("resource_id")
    private String resourceUuid;

    private String namespace;
    
//    private String operator;
//    
//    private String operation;
}
