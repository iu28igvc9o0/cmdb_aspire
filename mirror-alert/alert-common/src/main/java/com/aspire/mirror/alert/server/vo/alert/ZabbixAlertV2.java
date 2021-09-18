package com.aspire.mirror.alert.server.vo.alert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.domain
 * @Author: baiwenping
 * @CreateTime: 2020-02-21 16:33
 * @Description: ${Description}
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixAlertV2 {
    public static final String	SOURCE_ZABBIX				= "ZABBIX";
    public static final String	BIZ_MONITOR_ITEM_KEY_PREFIX	= "YWZB_";

    private String	moniResult;		// 指标监测结果 1: 新增告警 2: 解除告警

    @JsonProperty("alert_id")
    private String alertId;

    @JsonProperty("z_alert_Id")
    private String	zbxAlertId;		// zabbix中告警id

    //	private String	monitorSource;	// 第三方监控系统编码, 在第三方的告警中，当作proxyName
    private String	deviceIP;		// 所属设备
    private String	servSystem;		// 所属业务系统
    private String	monitorObject;	// 监控对象
    private String	monitorIndex;	// 监控指标
    private String	moniIndexValue;
    private String	alertLevel;		// 告警级别
    private String	alertDesc;		// 告警描述
    private String	curMoniTime;	// 当前监测时间 yyyy-MM-dd HH:mm:ss
    private String	curMoniValue;	// 当前监测值
    private String	businessSystem;	// 在Zabbix系统告警中，当作proxyName

    @JsonProperty("z_itemId")
    private String	zbxItemId;
    private String objectType;
    /**
     * 告警开始时间
     */
    private String alertStartTime;
    /**
     * 告警前缀，用于区分不同zabbix
     */
    private String source;
    /**
     * 监控对象
     */
    private String	itemKey;

    /**
     * 监控对象描述/告警标题
     */
    private String	keyComment;

    private Map<String, Object> ext;
}
