package com.aspire.mirror.log.api.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * 事件日志信息
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: EventInfoDTO.java
 * 类描述:  事件日志信息
 * 创建人: sunke
 * 创建时间: 2017年8月11日 上午10:45:29
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventInfoDTO {

    private Long time;

    @JsonProperty(value = "created_at")
    @SerializedName("created_at")
    private String createdAt;

    private EventDetailDTO detail;

    @JsonProperty(value = "log_level")
    @SerializedName("log_level")
    private int logLevel;

    @JsonProperty(value = "template_id")
    @SerializedName("template_id")
    private String templateId;

    @JsonProperty(value = "resource_type")
    @SerializedName("resource_type")
    private String resourceType;

    @JsonProperty(value = "resource_name")
    @SerializedName("resource_name")
    private String resourceName;

    @JsonProperty(value = "resource_id")
    @SerializedName("resource_id")
    private String resourceId;

    private String namespace;

}
