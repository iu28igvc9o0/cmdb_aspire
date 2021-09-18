package com.aspire.mirror.inspection.api.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.collect.api.payload
 * 类名称:    SelfHealingItem.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/20 16:48
 * 版本:      v1.0
 */
@Data
public class SelfHealingItem {
    /**
     * 资源池
     */
    @JsonProperty("idc_type")
    private String idcType;

    /**
     * 设备IP
     */
    @JsonProperty("device_ip")
    private String deviceIp;

    /**
     * 指标来源 xj: 巡检 gj: 告警
     */
    @JsonProperty("source_mark")
    private String sourceMark;

    /**
     * 指标类型
     */
    @JsonProperty("moni_object")
    private String moniObject;

    /**
     * 指标
     */
    @JsonProperty("item_key")
    private String itemKey;

    /**
     * 告警ID
     */
    @JsonProperty("alert_id")
    private String alertId;

    /**
     * 告警值
     */
    @JsonProperty("cur_moni_value")
    private String curMoniValue;

    /**
     * 告警描述
     */
    @JsonProperty("moni_index")
    private String moniIndex;

}
