package com.aspire.mirror.alert.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 告警批量创建
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsBatchCreateRequest.java
 * 类描述:    告警批量创建
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:15
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class AlertsBatchCreateRequest {
    @JsonProperty("alert_list")
    private List<AlertsCreateRequest> alertList;
}
