package com.aspire.mirror.template.api.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_template_device视图层对象
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto.vo
 * 类名称:   TemplateDeviceVO.java
 * 类描述:   monitor_template_device视图层属性，属性范围>=表结构属性.
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class TemplateDeviceVO implements Serializable {
	
	private static final long serialVersionUID = -8266528211862570187L;

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
