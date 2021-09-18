package com.aspire.mirror.alert.server.dao.inspectionDaily.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 历史告警详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsHisDetailResponse.java
 * 类描述:    历史告警详情
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:18
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class AlertDeviceTypeConfig {
	@JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;
    
    @JsonProperty("code")
    private String code;
    
}
