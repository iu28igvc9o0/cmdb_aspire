package com.aspire.mirror.alert.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 告警创建返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsCreateResponse.java
 * 类描述:    告警创建返回
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:07
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class AlertsCreateResponse {
    @JsonProperty("alert_id")
    private String alertId;
}
