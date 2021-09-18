package com.aspire.mirror.template.api.dto.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_functions持久对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto.model
 * 类名称:    FunctionsDTO.java
 * 类描述:    monitor_functions业务类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class FunctionsDTO implements Serializable {
	
	private static final long serialVersionUID = -6457572857797467965L;

    /** 函数ID */
    @ApiModelProperty(value = "函数ID")
    private String functionId;

    /** 监控项ID */
    @ApiModelProperty(value = "监控项ID")
    private String itemId;

    /** 触发器ID */
    @ApiModelProperty(value = "触发器ID")
    private String triggerId;

    /** 函数 */
    @ApiModelProperty(value = "函数")
    private String function;

    /** 参数 */
    @ApiModelProperty(value = "参数")
    private String parameter;

} 
