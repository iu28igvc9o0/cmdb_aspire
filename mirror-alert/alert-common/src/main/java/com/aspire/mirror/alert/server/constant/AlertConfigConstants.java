package com.aspire.mirror.alert.server.constant;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.constant
 * @Author: baiwenping
 * @CreateTime: 2020-02-21 16:30
 * @Description: ${Description}
 */
public class AlertConfigConstants {
    /**
     * 告警模型表名，redis的key
     */
    public static final String REDIS_MODEL_ALERT = "alert_alerts";

    /**
     * 告警历史模型表名，redis的key
     */
    public static final String REDIS_MODEL_ALERT_HIS = "alert_alerts_his";

    /**
     * 衍生告警记录模型表名，redis的key
     */
    public static final String REDIS_MODEL_DERIVE_ALERT = "alert_derive_alerts";

    /**
     * 衍生告警记录模型表名，redis的key
     */
    public static final String REDIS_MODEL_DERIVE_ALERT_HIS = "alert_derive_alerts_his";

    /**
     * 屏蔽告警记录模型表名，redis的key
     */
    public static final String REDIS_MODEL_ISOLATE_ALERT = "alert_isolate_alerts";
    /**
     * 屏蔽告警记录模型表名，redis的key
     */
    public static final String REDIS_MODEL_ISOLATE_ALERT_HIS = "alert_isolate_alerts_his";
    /**
     * 设备模型表名，redis的key
     */
    public static final String REDIS_MODEL_DEVICE_INSTANCE = "cmdb_instance";

    /**
     * 主次告警记录模型表名，redis的key
     */
    public static final String REDIS_MODEL_PRIMARY_SECONDARY_ALERT = "alert_primary_secondary_alerts";

    public static final String REDIS_ALERT_ORDER_LOCK = "ALERT:ORDER_LOCK";

    /**
     * 自动通知任务锁
     */
    public static final String REDIS_ALERT_AUTO_NOTIFY_LOCK = "ALERT:AUTO_NOTIFY_LOCK";

    /**
     * 自动通知时间轴类型
     */
    public static final String ALERT_AUTO_NOTIFY_TYPE = "ALERT_AUTO_NOTIFY_TYPE";

    /**
     * 自动通知时间轴类型描述
     */
    public static final String ALERT_AUTO_NOTIFY_TYPE_DESC = "自动通知时间轴";

    /**
     * 自动通知任务锁
     */
    public static final String REDIS_ALERT_AUTO_NOTIFY_LOCK_RESEND = "ALERT:AUTO_NOTIFY_LOCK_RESEND";

    public static final String REDIS_ALERT_AUTO_SUBSCRIBE_LOCK_RESEND = "ALERT:AUTO_SUBSCRIBE_LOCK_RESEND";

    public static final String YES = "1";

    public static final String NO = "0";

    public static final String ALERT_ID = "alert_id";

    public static final String QUERY_TYPE_LIKE = "like";

    public static final String QUERY_TYPE_IN = "in";

    public static final String QUERY_TYPE_AND = "and";

    public static final String QUERY_TYPE_IN_AND = "in_and";

    public static final String QUERY_TYPE_IN_DATE = "date";

    public static final String QUERY_TYPE_IN_DATETIME = "datetime";

    public static final String QUERY_TYPE_TREE_IN = "tree_in";

    public static final String QUERY_TYPE_NULL = "null";

    public static final String QUERY_TRUE = "true";

    public static final String QUERY_FALSE = "false";

    public static final String MESSAGE_TYPE_SMS = "sms";

    public static final String MESSAGE_TYPE_EMAIL = "email";

    public static final String MESSAGE_TEMPLATE_ALERT_TEMPLATE = "alert_template";

    /**
     * 业务告警关联告警id前缀
     */
    public static final String BUSINESS_ALERT_PREFIX = "business_";
    /**
     * 异常信息关联告警id前缀
     */
    public static final String EXCEPTION_ALERT_PREFIX = "exception_";


    public static final String BRANCH_IT_YUN = "it_yun";

    public static final String START = "START";

    public static final String END = "END";

    /**
     * 查询cmdb模式，如果是base则查询本地表
     */
    public static final String CMDB_MODEL_BASE = "base";

    public static final String SOURCE_DERIVE = "衍生告警";
}
