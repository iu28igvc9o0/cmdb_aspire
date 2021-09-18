package com.aspire.mirror.indexproxy.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 模板详情对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    TemplateDetailResponse.java
 * 类描述:    模板创建响应对象
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of={"templateId"}, callSuper=false)
public class MonitorTemplateRecord extends BasicDataOperateAware implements Serializable {
	private static final long	serialVersionUID	= -1955601193847671031L;
	public static final String	FUN_TYPE_MONITOR	= "1";
	public static final String	FUN_TYPE_INSPECTION	= "2";

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
     * 功能类型
     * 1-监控
     * 2-巡检
     */
    @JsonProperty("fun_type")
    private String funType;

    @JsonProperty("sys_type")
    private String sysType;

    /**
     * 监控类型：
     * 1-系统
     * 2-业务
     * 3-自监控系统
     */
    @JsonProperty("mon_type")
    private String monType;

    /**
     * 状态
     * 0-临时
     * 1-正式
     */
    private String status;

    @JsonProperty("proxy_identity")
    private String proxyIdentity;

    @JsonProperty("zabbix_template_id")
    private String zabbixTemplateId;

    @JsonProperty("template_object_list")
    private List<TemplateObjectRecord> templateObjectList;


    private List<MonitorItemRecord> items;

    private List<MonitorHost> hosts;

    @JsonProperty("index_type")
    private String indexType;
} 
