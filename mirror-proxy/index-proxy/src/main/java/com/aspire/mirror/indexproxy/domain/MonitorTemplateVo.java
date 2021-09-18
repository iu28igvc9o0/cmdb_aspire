package com.aspire.mirror.indexproxy.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 自监控模板VO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    MonitorTemplateVo
 * 类描述:    自监控模板VO
 * 创建人:    JinSu
 * 创建时间:  2020/10/28 16:06
 * 版本:      v1.0
 */
@Data
public class MonitorTemplateVo {
    @JsonProperty("proxy_identity")
    private String proxyIdentity;

    /** 模版名称 */
    private String name;

    /** 描述 */
    private String description;

    @JsonProperty("zabbix_template_id")
    private String zabbixTemplateId;

    /** 主机 */
//    private String devices;

    /**
     * 模版类型：
     * 1-硬件
     * 2-网络
     * 3-主机操作系统
     * 4-应用
     */
    private String type;

    /**
     * 功能类型
     * 1-监控
     * 2-巡检
     */
    @JsonProperty("fun_type")
    private String funType;

    /**
     * 监控类型：
     * 1-系统
     * 2-业务
     * 3-自监控系统
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

    /**
     * 状态
     * 0-临时
     * 1-正式
     */
    private String status;
}
