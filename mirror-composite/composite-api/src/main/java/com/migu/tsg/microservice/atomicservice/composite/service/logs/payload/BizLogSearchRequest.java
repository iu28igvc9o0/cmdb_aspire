package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * 业务日志搜索请求类 Project Name:composite-api File Name:BizLogSearchRequest.java Package
 * Name:com.migu.tsg.microservice.atomicservice.composite.service.logs.payload ClassName: BizLogSearchRequest <br/>
 * date: 2017年12月27日 上午11:12:15 <br/>
 * 
 * @author jiangfuyi
 * @version 1.0
 * @since JDK 1.8
 */
//@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BizLogSearchRequest {
    // 命名空间
    private String namespace;
    // 集群
    private String clusters;
    // 开始时间
    @JsonProperty("start_time")
    @SerializedName("start_time")
    private Long startTime;
    // 结束时间
    @JsonProperty("end_time")
    @SerializedName("end_time")
    private Long endTime;
    // 实例ID
    private String instances;
    // 服务所在主机ip
    private String nodes;
    // 页数
    private String pageno;
    // 日志级别
    @JsonProperty("biz_level")
    @SerializedName("biz_level")
    private String logLevel;
    // 查询语句
    @JsonProperty("query_string")
    @SerializedName("query_string")
    private String queryString;
    // 服务名
    private String services;
    // 显示行数
    private String size;
    // 项目名称
    @JsonProperty("project_name")
    @SerializedName("project_name")
    private String projectName;
    // 追踪ID
    @JsonProperty("biz_id")
    @SerializedName("biz_id")
    private String traceId;
    // appid
    @JsonProperty("app_id")
    @SerializedName("app_id")
    private String appId;
    //下载文件名
    private String fileName;
    @JsonProperty("region_id")
    @SerializedName("region_id")
    private String regionId;
}
