package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.dto.model;

import com.alibaba.fastjson.annotation.JSONField;
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
    @JSONField(name = "biz_date")
    private String createDate;
    //鑷畾涔夋绱㈠��
    @SerializedName("biz_id")
    @JsonProperty("biz_id")
    @JSONField(name = "biz_id")
    private String traceId;
    @SerializedName("biz_level")
    @JsonProperty("biz_level")
    @JSONField(name = "biz_level")
    private String logLevel;
    @SerializedName("biz_code")
    @JsonProperty("biz_code")
    @JSONField(name = "biz_code")
    private String logCode;
    @SerializedName("biz_content")
    @JsonProperty("biz_content")
    @JSONField(name = "biz_content")
    private String logContent;
    @SerializedName("filename")
    @JsonProperty("filename")
    @JSONField(name = "filename")
    private String path;
    @SerializedName("app_id")
    @JsonProperty("app_id")
    private String appId;
}
