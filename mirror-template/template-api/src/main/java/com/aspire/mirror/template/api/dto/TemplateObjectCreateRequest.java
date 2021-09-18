package com.aspire.mirror.template.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_template_device新增对象类
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto
 * 类名称:   TemplateObjectCreateRequest.java
 * 类描述:   monitor_template_device创建请求对象
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateObjectCreateRequest implements Serializable {
	
	private static final long serialVersionUID = -5425200953322222980L;


    /** 模版ID */
    @JsonProperty("template_id")
    private String templateId;

    /** 设备ID */
    @JsonProperty("object_id")
    private String objectId;

    /**
     * 关联对象类型
     * 1：设备ID
     * 2：业务系统
     */
    @JsonProperty("object_type")
    private String objectType;

} 
