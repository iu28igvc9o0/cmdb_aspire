package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 模板列表查询返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompTemplatePageResponse.java
 * 类描述:    模板列表查询返回
 * 创建人:    JinSu
 * 创建时间:  2018/8/8 11:04
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
@ToString
public class CompTemplatePageResponse implements Serializable {
    private static final long serialVersionUID = 542000626480368454L;
    /**
     * 模版ID
     */
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 模版名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("create_time")
    private java.util.Date createTime;

    /**
     * 模版类型：
     * 1-硬件
     * 2-网络
     * 3-主机操作系统
     * 4-应用
     */
    private String type;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("update_time")
    private java.util.Date updateTime;

    /**
     * 监控项列表
     */
    private String items;

    /**
     * 功能类型
     * 1-监控
     * 2-巡检
     */
    @JsonProperty("fun_type")
    private String funType;

    /**
     * 监控类型
     * 1-系统
     * 2-业务
     */
    @JsonProperty("mon_type")
    private String monType;

    /**
     * 系统类型
     * ZABBIX
     * PROMETHEUS
     * THEME
     * MIRROR
     */
    @JsonProperty("sys_type")
    private String sysType;

    private String status;
    /**
     * 任务数量
     */
    @JsonProperty("task_num")
    private int taskNum;
}
