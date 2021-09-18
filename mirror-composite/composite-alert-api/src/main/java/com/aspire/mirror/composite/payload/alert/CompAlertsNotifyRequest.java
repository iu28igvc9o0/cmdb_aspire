package com.aspire.mirror.composite.payload.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 告警通知请求实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsOperationRequest.java
 * 类描述:    告警操作请求实体
 * 创建人:    baiwenping
 * 创建时间:  2020/03/31 11:08
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class CompAlertsNotifyRequest {
    

	//当前用户name
    @JsonProperty("username")
    private String username;
	
    //告警id
    @JsonProperty("alertIds")
    private String alertIds;

    //邮箱地址
    @JsonProperty("emails")
    private List<String> emails;

    //邮箱地址
    @JsonProperty("mobiles")
    private List<String> mobiles;
   
}
