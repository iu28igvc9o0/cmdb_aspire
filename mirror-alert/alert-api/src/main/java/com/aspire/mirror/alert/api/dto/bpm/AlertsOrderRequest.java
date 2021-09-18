package com.aspire.mirror.alert.api.dto.bpm;

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
public class AlertsOrderRequest {
    

	//当前用户name
    @JsonProperty("namespace")
    private String namespace;

    //当前用户name
    @JsonProperty("username")
    private String username;
	
    //告警id
    @JsonProperty("alert_ids")
    private String alertIds;

    //派单类型 1告警 2故障 3维保
    @JsonProperty("order_type")
    private Integer orderType;
}
