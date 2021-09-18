package com.aspire.mirror.template.api.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_template_device新增对象类
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto
 * 类名称:   TemplateDeviceCreateResponse.java
 * 类描述:   monitor_template_device创建响应对象
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDeviceCreateResponse implements Serializable {
	
	private static final long serialVersionUID = -5229504498872687288L;

    /** 模版设备关系ID */
    @JsonProperty("template_device_id")
    private String templateDeviceId;

    /** 模版ID */
    @JsonProperty("template_id")
    private String templateId;

    /** 设备ID */
    @JsonProperty("device_id")
    private String deviceId;

} 
