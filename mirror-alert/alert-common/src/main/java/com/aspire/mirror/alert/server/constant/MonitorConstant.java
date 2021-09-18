package com.aspire.mirror.alert.server.constant;

/**
 * Created by Administrator on 2017/11/16.
 */
public class MonitorConstant {

    /**
     * 根据请求的URL判断是否为proxy中主机端口监控
     */
    public static final String PROXY_PORT_MONITOR = "httpRequestProxy/portMonitor";

    /**
     * 认证模式状态：未设置
     */
    public static final String AUTHENTICATION_NOT_SET = "N";
    /**
     * 认证模式状态：已设置
     */
    public static final String AUTHENTICATION_BEEN_SET = "Y";
    /**
     * 告警通知
     */
    public static final String EMAIL_ALERT = "【咪咕运维】【告警】HTTP服务监控告警-";
    /**
     * http端口监控上一次未完成通知
     */
    public static final String EMAIL_PROMPT = "【咪咕运维】【提示】HTTP服务主机端口监控-";
    /**
     * 告警通知
     */
    public static final String EMAIL_ALERT_RESTORE = "【咪咕运维】【恢复】HTTP服务监控告警-";
    /**
     * WEB服务监控返回结果判定
     */
    public static final String CONCLUSION_NORMAL = "正常";

    /**
     * WEB服务监控返回结果判定
     */
    public static final String CONCLUSION_STATUS = "异常:状态码校验失败";

    /**
     * WEB服务监控返回结果判定
     */
    public static final String CONCLUSION_ABNORMAL = "异常";

    /**
     * WEB服务监控返回结果判定(json校验失败)
     */
    public static final String JSON_CONCLUSION_ABNORMAL = "异常:json校验失败";

    /**
     * WEB服务监控返回结果判定(json校验失败)
     */
    public static final String HTML_CONCLUSION_ABNORMAL = "异常:html校验失败";

    /**
     * WEB服务监控返回结果判定(url连接失败)
     */
    public static final String URL_CONCLUSION_ABNORMAL = "异常:地址请求失败";

    /**
     * WEB服务监控返回结果判定(异常，响应超时)
     */
    public static final String URL_TIME_OUT = "异常:响应超时";

    /**
     * WEB服务监控返回结果判定(异常，响应超时)
     */
    public static final String URL_UNKNOWN_ERROR = "异常:主机连接异常";

    /**
     * WEB服务监控返回结果判定(异常，响应超时)
     */
    public static final String URL_CONN_ERROR = "异常:服务器连接错误";

    /**
     * WEB服务监控返回结果判定(非法url地址格式)
     */
    public static final String URL_MALFORMEDURL_ABNORMAL = "异常:非法地址格式";

    /**
     * WEB服务监控JSON比较方式>=
     */
    public static final String JSON_COMPARE_TYPE_LARGE = "1";

    /**
     * WEB服务监控JSON比较方式<
     */
    public static final String JSON_COMPARE_TYPE_LARGE_ETC = "2";

    /**
     * WEB服务监控JSON比较方式<=
     */
    public static final String JSON_COMPARE_TYPE_SMALL = "3";

    /**
     * WEB服务监控JSON比较方式=
     */
    public static final String JSON_COMPARE_TYPE_SMALL_ETC = "4";

    /**
     * WEB服务监控JSON比较方式“包含”
     */
    public static final String JSON_COMPARE_TYPE_SMALL_CONTAIN = "5";

    /**
     * 发送请求返回状态码
     */
    public static final Integer HTTP_STATE = 200;

    /**
     * 定时触发器毫秒单位
     */
    public static final int TRIGGER_TIME = 1000;
    /**
     * 定时任务分组名
     */
    public static final String MONITOR_JOB_GROUP_NAME = "MONITOR_HTTP_JOBGROUP_NAME";
    /**
     * 触发器分组名
     */
    public static final String MONITOR_TRIGGER_GROUP_NAME = "MONITOR_HTTP_TRIGGERGROUP_NAME";

    /**
     * 修改状态码
     */
    public static final String UPDATE_JOB = "修改";

    /**
     * 新增状态码
     */
    public static final String ADD_JOB = "新增";

    /**
     * token码
     */
    public static final String TOKEN_CODE = "eyJpc3MiOiJKb2huIFd1IEpXVCIsImlhdCI6MTQ0MTU5MzUwMiwiZXhwIjoxNDQxNTk0"
            + "NzIyLCJhdWQiOiJ3d3cuZXhhbXBsZS5jb20iLCJzdWIiOiJqcm9ja2V0QGV4YW1wbGUuY29tIiwiZnJvbV91c2VyIjoiQiIsInRhcmdldF91c2VyIjoiQSJ9";

    /**
     * ip使用类型(管理ip)
     */
    public static final Short MANAGER_IP = 1;

    /**
     	* 外网
     */
    public static final Short EXTRANET = 0;
    //内网
    public static final Short INTRANET = 1;
}
