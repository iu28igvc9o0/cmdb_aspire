package com.aspire.mirror.log.api.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* 时间查询DTO
* 项目名称:  微服务
* 包:        com.migu.tsg.microservice.monitor.log.dto
* 类名称:    EventLogListRequest.java
* 类描述:    事件查询请求参数实体类
* 创建人:    jiangfuyi
* 创建时间:  2017年8月7日
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventLogListRequest {

    // 查询开始时间 日志查询开始时间的时间戳
    @JsonProperty("start_time")
    @SerializedName("start_time")
    private long startTime;

    // 查询结束时间 日志查询结束时间的时间戳
    @JsonProperty("end_time")
    @SerializedName("end_time")
    private long endTime;

    // 当前页 查询列表当前页
    @JsonProperty("pageno")
    @SerializedName("pageno")
    private String pageNo;

    // 每页显示记录 每页显示多少条记录
    private String size;

    // 名称空间
    private String namespace;

    // 查询语句
    @JsonProperty("query_string")
    @SerializedName("query_string")
    private String queryString;

    // 事件查询资源类型与具体资源查询接口属性
    // 资源类型
    @JsonProperty("resource_type")
    @SerializedName("resource_type")
    private String resourceType;

    // 资源UUID
    @JsonProperty("resource_id")
    @SerializedName("resource_id")
    private String resourceUuid;
    
    //所属项目:为事件而添加的属性2017-12-13
    @JsonProperty("project_name")
    @SerializedName("project_name")
    private String projectName;
}
