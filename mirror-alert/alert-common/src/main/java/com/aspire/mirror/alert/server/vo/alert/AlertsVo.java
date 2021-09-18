package com.aspire.mirror.alert.server.vo.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto.model
 * 类名称:    AlertsDTO.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:09
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class AlertsVo {
    public static final String AUTO_CLEAR_COUNT_SPLIT = "_A_";    // 手工告警清除次数分隔符
    public static final String MANUAL_CLEAR_COUNT_SPLIT = "_M_";    // 手工告警清除次数分隔符
    public static final String OBJECT_TYPE_DEVICE = "1";
    public static final String OBJECT_TYPE_BIZ = "2";
    public static final String ORDER_STATUS_NOT_READY = "1";
    public static final String ALERT_ACTIVE = "1";        // 告警
    public static final String ALERT_REVOKE = "2";        // 消警

	public static final Map<Integer, String> ALERT_LEVELS = initAlertLevelLables();
	
    /**  */
    @JsonProperty("alert_id")
    private String alertId;
    /**  */
    @JsonProperty("r_alert_id")
    private String rAlertId;

    /**  */
    @JsonProperty("event_id")
    private String eventId;

    /**  */
    @JsonProperty("device_id")
    private String deviceId;

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
    @JsonProperty("cur_moni_time")
    private Date curMoniTime;

    /**
     * 告警开始时间
     */
    @JsonProperty("alert_start_time")
    private Date alertStartTime;

    /**
     * 告警级别
     * 1-提示
     * 2-低
     * 3-中
     * 4-高
     * 5-严重
     */
    @JsonProperty("alert_level")
    private String alertLevel;

    /**
     * 警报操作状态:
     * 0-待确认
     * 1-已确认
     * 2-待解决
     * 3-已处理
     */
    @JsonProperty("operate_status")
    private Integer operateStatus;

    @JsonProperty("alert_type")
    private String alertType;    // 告警  or 消警

    /**  */
    @JsonProperty("item_id")
    private String itemId;

    /**
     * 告警结束时间
     */
    @JsonProperty("alert_end_time")
    private Date alertEndTime;

    /**
     * 备注
     */
    @JsonProperty("remark")
    private String remark;

    /**
     * 1-未派单
     * 2-处理中
     * 3-已完成
     */
    @JsonProperty("order_status")
    private String orderStatus = ORDER_STATUS_NOT_READY;    // 默认未派单

    /**
     * 告警来源
     * MIRROR
     * ZABBIX
     */
    @JsonProperty("source")
    private String source;

    /**
     * 机房/资源池
     */
    @JsonProperty("source_room")
    private String sourceRoom;

    @JsonProperty("object_id")
    private String objectId;
    /**
     * 1-系统
     * 2-业务
     */
    @JsonProperty("object_type")
    private String objectType;

    /**
     * 所属域/资源池
     */
    @JsonProperty("region")
    private String region;

    /**
     * ip
     */
    @JsonProperty("device_ip")
    private String deviceIp;
    /**
     * 工单类型
     * 1告警
     * 2故障
     */
    @JsonProperty("order_type")
    private String orderType;
    /**
     * 工单ID
     */
    @JsonProperty("order_id")
    private String orderId;
    /**
     * 业务系统名称
     */
    @JsonProperty("biz_sys_name")
    private String bizSysName;
    /**
     * 所属位置-资源池
     */
    @JsonProperty("idc_type")
    private String idcType;
    /**
     * 警告数量
     */
    @JsonProperty("alert_count")
    private Integer alertCount;
    /**
     * 更新时候数量是否+1，默认否
     */
    private boolean updateCount = false;
    /**
     * 设备分类
     */
    @JsonProperty("device_class")
    private String deviceClass;
    
    // 1-提示 2-低 3-中 4-高 5-严重
    private static Map<Integer, String> initAlertLevelLables() {
    	Map<Integer, String> map = new HashMap<>();
    	map.put(1, "提示");
    	map.put(2, "低");
    	map.put(3, "中");
    	map.put(4, "高");
    	map.put(5, "严重");
    	return Collections.unmodifiableMap(map);
    }
    /**
     * 通知方式
     * 0:短信
     * 1:邮件
     */
    private Integer reportType;
    /**
     * 通知状态
     * 0:失败
     * 1:成功
     */
    private Integer reportStatus;

    /**
     * 通知时间
     */
    private Date reportTime;
    /**
     * 转派人
     */
    private String transUser;
    /**
     * 转派状态
     * 0:失败
     * 1:成功
     */
    private Integer transStatus;
    /**
     * 转派时间
     */
    private Date transTime;
    /**
     * 待确认人
     */
    private String toConfirmUser;
    /**
     * 确认人
     */
    private String confirmedUser;
    /**
     * 确认时间
     */
    private Date confirmedTime;
    /**
     * 确认内容
     */
    private String confirmedContent;
    /**
     * 派单状态
     * 0:失败
     * 1:成功
     */
    private Integer deliverStatus;
    /**
     * 派单时间
     */
    private Date deliverTime;
    private String oldEventId;
    @JsonProperty("action_id")
    private String actionId;
    /**
     * 告警次数
     */
    private Integer alertTimes;
    /**
     * 告警前缀，用于区分不同zabbix
     */
    @JsonProperty("prefix")
    private String prefix;

    private String messageSend;
    private String messageOpen;
    private String mailSend;
    private String mailOpen;
    /**
     * 设备类型
     */
    @JsonProperty("device_type")
    private String deviceType;
    /**
     * 设备提供商
     */
    @JsonProperty("device_mfrs")
    private String deviceMfrs;
    /**
     * 设备型号
     */
    @JsonProperty("device_model")
    private String deviceModel;
    /**
     * 主机名
     */
    @JsonProperty("host_name")
    private String hostName;
    /**
     * 当前告警收敛数
     */
    @JsonProperty("intelligent_count")
    private Integer intelligentCount;
    /**
     * pod池
     */
    @JsonProperty("pod_name")
    private String podName;
    /**
     * 工程期数
     */
    @JsonProperty("project_name")
    private String projectName;
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

    @JsonProperty("idc_cabinet")
    private String	cabinet;

}
