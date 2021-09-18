package com.aspire.mirror.template.api.dto.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模板关联对象持久对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto.model
 * 类名称:    TemplateObjectDTO.java
 * 类描述:    monitor_template_device业务类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TemplateObjectDTO implements Serializable {
	
	private static final long serialVersionUID = -5690537069778285563L;

    /** 模版设备关系ID */
    @ApiModelProperty(value = "模版关联对象ID")
    private String templateObjectId;

    /** 模版ID */
    @ApiModelProperty(value = "模版ID")
    private String templateId;

    /** 设备ID */
    @ApiModelProperty(value = "对象ID")
    private String objectId;

    /**
     * 对象类型
     * 1：设备ID
     * 2: 业务系统
     */
    @ApiModelProperty(value = "对象类型")
    private String objectType;

} 
