package com.aspire.mirror.template.api.dto.model;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 触发器持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto.model
 * 类名称:    TriggersDTO.java
 * 类描述:    触发器业务类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TriggersDTO implements Serializable {

    private static final long serialVersionUID = -5004409898041750570L;

    /**
     * 触发器ID
     */
    @ApiModelProperty(value = "触发器ID")
    private String triggerId;

    /**
     * 触发器名称
     */
    @ApiModelProperty(value = "触发器名称")
    private String name;

    /**
     * 表达式
     */
    @ApiModelProperty(value = "表达式")
    private String expression;

    /**
     * URL
     */
    @ApiModelProperty(value = "URL")
    private String url;

    /**
     * 状态：
     * ON-启用
     * OFF-禁用
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 值类型
     * OK
     * PROBLEM
     * UNKNOWN
     */
    @ApiModelProperty(value = "值类型")
    private String value;

    /**
     * 优先级
     * 0-Not classified
     * 1 -Information
     * 2-Warning
     * 3-Average
     * 4-High
     * 5-Disaster
     */
    @ApiModelProperty(value = "优先级")
    private String priority;

    /**
     * 监控项ID
     */
    @ApiModelProperty(value = "监控项ID")
    private String itemId;

    /**
     * 脚本类监控触发器的参数值
     */
    @ApiModelProperty(value = "脚本类监控触发器的参数值")
    private String param;

    /**
     * 函数列表
     */
    @ApiModelProperty(value = "函数列表")
    private List<FunctionsDTO> functionList;

    private String type;
} 
