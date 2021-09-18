package com.aspire.mirror.log.api.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  日志基础信息dto
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: LogInfoDTO.java
 * 类描述: 日志基础信息dto
 * 创建人: jiangfuyi
 * 创建时间: 2017年8月11日 上午11:11:21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogInfoDTO {
    // 日志的收集到的时间戳
    private long time;

    // 容器所在机器的hostname
    private String machine;

    // 服务的id
    @JsonProperty(value = "app_id")
    @SerializedName("app_id")
    private String appId;

    // 服务对应的实例的id
    @JsonProperty(value = "instance_id")
    @SerializedName("instance_id")
    private String instanceId;

    // 容器名
    @JsonProperty(value = "container_name")
    @SerializedName("container_name")
    private String containerName;

    // 日志内容
    @JsonProperty(value = "log_data")
    @SerializedName("log_data")
    private String logData;

    // 日志类型(stdout, stderr file ...)
    @JsonProperty(value = "log_type")
    @SerializedName("log_type")
    private String logType;

    // 日志级别
    @JsonProperty(value = "log_level")
    @SerializedName("log_level")
    private int logLevel;

    // 日志文件名
    @JsonProperty(value = "filename")
    @SerializedName("filename")
    private String fileName;

    // 服务名称
    @JsonProperty(value = "app_id")
    @SerializedName("service_name")
    private String serviceName;

    // 集群名称
    @JsonProperty(value = "app_id")
    @SerializedName("region_name")
    private String regionName;

    // 集群ID
    @JsonProperty(value = "region_id")
    @SerializedName("region_id")
    private String regionId;

    // 实例名的前8位，用于日志面板查询
    @JsonProperty(value = "instance_id8")
    @SerializedName("instance_id8")
    private String instanceId8;

    // 日志收集时间(标准的时间格式)
    @JsonProperty(value = "@timestamp")
    @SerializedName("@timestamp")
    private String timestamp;

    // 暂时保留，与页面联调完成且确认没有用后删除
    @JsonProperty(value = "log_detail")
    @SerializedName("log_detail")
    private String logDetail;

    @JsonProperty(value = "app_name")
    @SerializedName("app_name")
    private String appName;

    @JsonProperty(value = "service_full_name")
    @SerializedName("service_full_name")
    private String serviceFullName;

    private String namespace;

    @JsonProperty(value = "create_at")
    @SerializedName("create_at")
    private Long createAt;

    private String paths;
}
