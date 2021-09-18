package com.aspire.mirror.alert.server.dao.primarySecondary.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertPrimarySecondaryAlertsV2 {

    /**
     *
     */
    @JsonProperty("alert_id")
    private String alertId;
    /**
     * 主次ID
     */
    @JsonProperty("primary_secondary_id")
    private Long primarySecondaryId;
    private String primarySecondaryName;
    /**
     * 主次告警ID
     */
    @JsonProperty("primary_secondary_alert_id")
    private String primarySecondaryAlertId;
    /**
     *
     */
    @JsonProperty("r_alert_id")
    private String rAlertId;
    /**
     * 业务系统
     */
    @JsonProperty("biz_sys")
    private String bizSys;
    /**
     * 监控指标/内容，关联触发器name
     */
    @JsonProperty("moni_index")
    private String moniIndex;
    /**
     * 监控对象
     */
    @JsonProperty("moni_object")
    private String moniObject;
    /**
     * 当前监控值
     */
    @JsonProperty("cur_moni_value")
    private String curMoniValue;
    /**
     * 当前监控时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("cur_moni_time")
    private Date curMoniTime;
    /**
     * 告警级别
     * 2-低
     * 3-中
     * 4-高
     * 5-严重
     */
    @JsonProperty("alert_level")
    private String alertLevel;
    /**
     *
     */
    @JsonProperty("item_id")
    private String itemId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 告警来源
     * MIRROR
     * ZABBIX
     */
    @JsonProperty("source")
    private String source;
    /**
     * 所属位置-资源池
     */
    @JsonProperty("idc_type")
    private String idcType;

    /**
     * 告警类型
     * 1-系统
     * 2-业务
     */
    @JsonProperty("object_type")
    private String objectType;
    /**
     *
     */
    @JsonProperty("device_ip")
    private String deviceIp;
    /**
     * 告警开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("alert_start_time")
    private Date alertStartTime;
    /**
     *
     */
    @JsonProperty("alert_count")
    private Integer alertCount;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("create_time")
    private Date createTime;
    /**
     * 告警结果，1-告警，2-消警
     */
    @JsonProperty("alert_type")
    private String alertType;

    /**
     * 监控项
     */
    @JsonProperty("item_key")
    private String	itemKey;

    /**
     * 监控项
     */
    @JsonProperty("key_comment")
    private String	keyComment;
}