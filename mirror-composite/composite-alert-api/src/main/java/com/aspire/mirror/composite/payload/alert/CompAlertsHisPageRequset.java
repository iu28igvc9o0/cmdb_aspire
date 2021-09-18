package com.aspire.mirror.composite.payload.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 告警Page请求
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.alert.payload
 * 类名称:    CompAlertsHisPageRequset.java
 * 类描述:    告警Page请求
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 14:54
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class CompAlertsHisPageRequset {

    @NotNull
    @JsonProperty("page_size")
    private Integer pageSize;

    @NotNull
    @JsonProperty("page_no")
    private Integer pageNo;

    @NotBlank
    @JsonProperty("sort_name")
    private String sortName;

    @NotBlank
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

    @JsonProperty("item_id")
    private String itemId;

    /**
     * 告警来源
     */
    private String source;

    private String curMoniTimeFrom;

    private String curMoniTimeTo;
}
