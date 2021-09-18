package com.aspire.mirror.log.api.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 业务列表查询响应对象
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto
 * 类名称: BusinessLogResponse.java
 * 类描述: 业务列表查询响应对象
 * 创建人: sunke
 * 创建时间: 2017年8月10日 上午10:13:31
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BusinessLogDTO {

    private Long time;
    @SerializedName("biz_date")
    @JsonProperty("biz_date")
    private String createDate;
    @SerializedName("biz_id")
    @JsonProperty("biz_id")
    private String traceId;
    @SerializedName("biz_level")
    @JsonProperty("biz_level")
    private String logLevel;
    @SerializedName("biz_code")
    @JsonProperty("biz_code")
    private String logCode;
    @SerializedName("biz_content")
    @JsonProperty("biz_content")
    private String logContent;
    @SerializedName("filename")
    @JsonProperty("filename")
    private String path;
    @SerializedName("app_id")
    @JsonProperty("app_id")
    private String appId;
    // 业务日志的原始报文
    @SerializedName("log_data")
    @JsonProperty("log_data")
    private String logData;
    // 容器id
    @SerializedName("container_id")
    @JsonProperty("container_id")
    private String containerId;
    // 主机信息
    private String machine;
    // 实例id
    @SerializedName("instance_id")
    @JsonProperty("instance_id")
    private String instanceId;
    // 服务名称
    @SerializedName("service_name")
    @JsonProperty("service_name")
    private String serviceName;
    // 集群名称
    @SerializedName("region_name")
    @JsonProperty("region_name")
    private String regionName;
    // 集群名称
    @SerializedName("region_id")
    @JsonProperty("region_id")
    private String regionId;
}
