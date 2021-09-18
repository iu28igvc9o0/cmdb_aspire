package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 触发器创建请求VO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompTriggerCreateRequest.java
 * 类描述:    触发器创建请求
 * 创建人:    JinSu
 * 创建时间:  2018/8/6 11:22
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompTriggerCreateRequest implements Serializable {
    private static final long serialVersionUID = 8177475321586877420L;
    /**
     * 表达式
     */
    @NotBlank
    private String expression;

    /**
     * 监控项ID
     */
    @NotBlank
    @JsonProperty("item_id")
    private String itemId;

    /**
     * 状态：
     * ON-启用
     * OFF-禁用
     */
    private String status;

    /**
     * 优先级
     * 0-Not classified
     * 1 -Information
     * 2-Warning
     * 3-Average
     * 4-High
     * 5-Disaster
     */
    private String priority;

    /**
     * 触发器名称
     */
    private String name;

    /**
     * 脚本类监控触发器的参数值
     */
    private String param;
}
