package com.aspire.mirror.zabbixintegrate.daoAlert.po;

import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.daoAlert.po
 * @Author: baiwenping
 * @CreateTime: 2020-04-15 09:56
 * @Description: ${Description}
 */
@Data
public class KpiConfigManage {
    /**
     *
     */
    private String id;
    /**
     * 指标配置名称
     */
    private String kpiName;
    /**
     * 指标类型，1-hour，2-day，3-month，4-自定义
     */
    private String kpiType;
    /**
     * 统计来源，zabbix、db、es、api
     */
    private String dateSource;
    /**
     * cron表达式
     */
    private String cron;

    /**
     * 索引类型,如果为db则表示数据库
     */
    private String insertObjectType;

    /**
     * 录入对象，如果按时间分按照{yyyyMMdd}格式
     */
    private String insertObject;

    /**
     * 关联ci字段
     */
    private String relationCiFields;
    /**
     * 执行对象，db、zabbix为表，es为索引，api则为请求地址
     */
    private String execObject;

    /**
     * exec_format
     */
    private String execFormat;

    /**
     * 执行类型，1-sql，2-dsl，3-自定义表达式，4-内置公式
     */
    private String execFormatType;

    /**
     * 过滤条件
     */
    private String execFilter;
    /**
     * 状态，1-启用，2-停用
     */
    private String status;

    /**
     * 是否删除，0-未删除，1-删除
     */
    private String isDelete;
    /**
     * 历史开始时间
     */
    private Date startTime;
    /**
     * 时间周期，单位：小时
     */
    private Integer duration;
    /**
     * 历史结束时间
     */
    private Date endTime;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private String updater;
    /**
     * 更新时间
     */
    private Date updateTime;
}
