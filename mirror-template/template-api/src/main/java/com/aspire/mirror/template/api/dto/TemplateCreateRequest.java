package com.aspire.mirror.template.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 模板新增对象类
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto
 * 类名称:   TemplateCreateRequest.java
 * 类描述:   模板创建请求对象
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateCreateRequest implements Serializable {
	
	private static final long serialVersionUID = -8218188718864896141L;

    /** 模版名称 */
    @NotBlank
    private String name;

    /** 描述 */
    private String description;

    /** 主机 */
//    private String devices;

    /**
     * 模版类型：
     * 1-硬件
     * 2-网络
     * 3-主机操作系统
     * 4-应用
     */
    @NotBlank
    private String type;

    /**
     * 功能类型
     * 1-监控
     * 2-巡检
     */
    @NotBlank
    @JsonProperty("fun_type")
    private String funType;

    /**
     * 监控类型：
     * 1-系统
     * 2-业务
     */
    @NotBlank
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
