package com.aspire.mirror.alert.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 告警page请求对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsPageRequset.java
 * 类描述:    告警page请求对象
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:10
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class AlertsPageRequset {

    @NotNull
    @JsonProperty("page_size")
    private Integer pageSize;

    @NotNull
    @JsonProperty("page_no")
    private Integer pageNo;

    @NotEmpty
    @JsonProperty("sort_name")
    private String sortName;

    @NotEmpty
    private String order;

    private String ip;

    @JsonProperty("alert_start_time")
    private String alertStartTime;

    @JsonProperty("alert_end_time")
    private String alertEndTime;

    @JsonProperty("alert_level")
    private String alertLevel;

    @JsonProperty("moni_object")
    private String moniObject;

    @JsonProperty("order_status")
    private String orderStatus;

    @JsonProperty("biz_sys")
    private String bizSys;

    @JsonProperty("object_type")
    private String objectType;
}
