package com.migu.tsg.microservice.atomicservice.composite.service.logs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.gson.annotations.SerializedName;

/**
 * 日志基础信息dto 项目名称: 微服务运维平台（log-api 模块） 包: com.migu.tsg.microservice.monitor.log.dto.model 
 * 类名称: LogInfoDTO.java 
 * 类描述: 日志基础信息dto 
 * 创建人: jiangfuyi 
 * 创建时间: 2017年12月28日 上午11:11:21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BizLogInfoDTO {
    // 日志的收集到的时间戳
    private String time;

    // 容器所在机器的hostname
    private String machine;

    // 服务的id
    @SerializedName("app_id")
    private String appId;

    // 服务对应的实例的id
    @SerializedName("instance_id")
    private String instanceId;

    // 日志内容
    @SerializedName("biz_content")
    private String logData;

    // 日志级别
    @SerializedName("biz_level")
    private String logLevel;

    // 日志文件名
    @SerializedName("filename")
    private String fileName;

    // 服务名称
    @SerializedName("service_name")
    private String serviceName;

    // 集群名称
    @SerializedName("region_name")
    private String regionName;

    // 集群ID
    @SerializedName("region_id")
    private String regionId;

    // 实例名的前8位，用于日志面板查询
    @SerializedName("instance_id8")
    private String instanceId8;

    // 日志收集时间(标准的时间格式)
    @SerializedName("@timestamp")
    private String timestamp;

    private String namespace;

    @SerializedName("biz_date")
    private String bizDate;

    // 追踪ID
    @SerializedName("biz_id")
    private String traceId;
    
    // bizcode
    @SerializedName("biz_code")
    private String bizCode;
}
