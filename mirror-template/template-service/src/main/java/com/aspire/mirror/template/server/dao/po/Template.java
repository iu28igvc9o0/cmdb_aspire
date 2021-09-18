package com.aspire.mirror.template.server.dao.po;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 模板持久对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po
 * 类名称:    Template.java
 * 类描述:    模板持久类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"name", "description"}, callSuper=false)
public class Template implements Serializable {
	
	private static final long serialVersionUID = -6169819816900967040L;

    // 模版ID
    private String templateId;

    // 模版名称
    private String name;

    // 描述
    private String description;

    // 创建时间
    private java.util.Date createTime;

    // 模版类型
    private String type;

    // 更新时间
    private java.util.Date updateTime;

    //功能类型
    private String funType;

    /**
     * 监控类型：
     * 1-系统
     * 2-业务
     */
    private String monType;

    /**
     * 系统类型
     * ZABBIX
     * PROMETHEUS
     * THEME
     * MIRROR
     */
    private String sysType;

    /**
     * 状态
     * 0-临时
     * 1-正式
     */
    private String status;

    private String proxyIdentity;

    private String zabbixTemplateId;

    private String creater;

    private String updater;

} 
