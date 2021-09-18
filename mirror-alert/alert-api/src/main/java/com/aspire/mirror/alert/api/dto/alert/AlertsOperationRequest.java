package com.aspire.mirror.alert.api.dto.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 告警操作请求实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsOperationRequest.java
 * 类描述:    告警操作请求实体
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:08
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class AlertsOperationRequest {
    

	//当前用户name
    @JsonProperty("namespace")
    private String namespace;

	
    //告警id
    @JsonProperty("alert_ids")
    private String alertIds;

    //派单类型 1告警 2故障 3维保 4调优
    @JsonProperty("order_type")
    private Integer orderType;

    //转派用户id
    @JsonProperty("user_ids")
    private String userIds;

    //转派用户名
    @JsonProperty("user_names") 
    String userNames;
    
   
    //评论
    @JsonProperty("content")
    private String content;
    
    
    //邮箱地址
    @JsonProperty("destination")
    private String[] destination;
    
     
    //邮件内容
    @JsonProperty("message")
    private String message;
    
    
   //操作状态
    @JsonProperty("status")
    private String status;

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
