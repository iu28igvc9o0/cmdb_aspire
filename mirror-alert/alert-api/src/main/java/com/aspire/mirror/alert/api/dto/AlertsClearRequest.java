package com.aspire.mirror.alert.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * comp告警操作请求实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsOperationRequest.java
 * 类描述:    comp告警操作请求实体
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:08
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class AlertsClearRequest {
    

	//当前用户name
    @JsonProperty("namespace")
    private String namespace;

    //当前用户name
    @JsonProperty("username")
    private String username;
	
    //告警id
    @JsonProperty("alert_ids")
    private String alertIds;

    //评论
    @JsonProperty("content")
    private String content;

    //类型
    @JsonProperty("auto_type")
    private Integer autoType;

    // 开始时间
    @JsonProperty("start_time")
    private String startTime;

    // 结束时间
    @JsonProperty("end_time")
    private String endTime;
}
