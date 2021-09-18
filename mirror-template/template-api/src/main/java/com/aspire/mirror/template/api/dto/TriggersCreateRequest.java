package com.aspire.mirror.template.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 触发器新增对象类
 * <p>
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto
 * 类名称:   TriggersCreateRequest.java
 * 类描述:   触发器创建请求对象
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TriggersCreateRequest implements Serializable {

    private static final long serialVersionUID = -5978090007878418461L;


    /**
     * 触发器名称
     */
    @NotBlank
    private String name;

    /**
     * 表达式
     */
    @NotBlank
    private String expression;

    /**
     * URL
     */
    private String url;

    /**
     * 状态：
     * ON-启用
     * OFF-禁用
     */
    @NotBlank
    private String status;

    /**
     * 值类型
     * OK
     * PROBLEM
     * UNKNOWN
     */
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
    @NotBlank
    private String priority;

    /**
     * 监控项ID
     */
    @NotBlank
    @JsonProperty("item_id")
    private String itemId;

    /**
     * 脚本类监控触发器的参数值
     */
    private String param;

    /**
     * 函数列表
     */
    @JsonProperty("function_list")
    private List<FunctionsCreateRequest> functionList;

} 
