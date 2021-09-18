package com.aspire.mirror.template.server.dao.po;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_template_device持久对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po
 * 类名称:    TemplateDevice.java
 * 类描述:    monitor_template_device持久类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDevice implements Serializable {
	
	private static final long serialVersionUID = -4916957859358818027L;

    // 模版设备关系ID
    private String templateDeviceId;

    // 模版ID
    private String templateId;

    // 设备ID
    private String deviceId;

} 
