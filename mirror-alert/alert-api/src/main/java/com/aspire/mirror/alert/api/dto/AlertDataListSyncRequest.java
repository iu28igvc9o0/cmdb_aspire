package com.aspire.mirror.alert.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 告警数据同步请求封装对象   <br/>
 * Project Name:alert-api
 * File Name:AlertDataListSyncRequest.java
 * Package Name:com.aspire.mirror.alert.api.dto
 * ClassName: AlertDataListSyncRequest <br/>
 * date: 2018年9月20日 下午4:12:36 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Data
@JsonInclude(Include.NON_NULL)
public class AlertDataListSyncRequest {
    @JsonProperty
    private String roomId;            // 机房

    @JsonProperty
    private String source;            // 告警来源

    @JsonProperty
    private List<Object> alertDataList;    // 告警数据列表, 不同的告警来源, 可以有不同的数据上报格式

    @JsonProperty
    private Object extendObj;        // 扩展对象
}
