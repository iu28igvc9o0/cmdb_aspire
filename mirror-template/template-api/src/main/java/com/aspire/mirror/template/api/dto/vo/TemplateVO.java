package com.aspire.mirror.template.api.dto.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模板视图层对象
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto.vo
 * 类名称:   TemplateVO.java
 * 类描述:   模板视图层属性，属性范围>=表结构属性.
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class TemplateVO implements Serializable {
	
	private static final long serialVersionUID = -4660087585413417029L;

    /** 模版ID */
    @JsonProperty("template_id")
    private String templateId;

    /** 模版名称 */
    private String name;

    /** 描述 */
    private String description;


    /** 模版类型：
1-硬件
2-网络
3-主机操作系统
4-应用
 */
    private String type;



    @JsonProperty("object_type")
    private String objectType;

    @JsonProperty("object_id")
    private String objectId;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("create_time")
    private java.util.Date createTime;


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

} 
