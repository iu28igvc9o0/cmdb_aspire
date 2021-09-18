package com.aspire.mirror.indexadapt.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 触发器详情对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    TriggersDetailResponse.java
 * 类描述:    触发器创建响应对象
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitorTriggerRecord implements Comparable<MonitorTriggerRecord>, Serializable {
	private static final long		serialVersionUID	= -5912865328976918136L;

	public static final String		SIGN_GREATER		= ">";
	public static final String		SIGN_GREATER_EQUAL	= ">=";
	public static final String		SIGN_EQUAL			= "=";
	public static final String		SIGN_LESS_EQUAL		= "<=";
	public static final String		SIGN_LESS			= "<";
	public static final String[]	VALID_SIGNS			= { SIGN_GREATER, SIGN_GREATER_EQUAL, 
															SIGN_EQUAL, SIGN_LESS_EQUAL, SIGN_LESS };
	
	@Override
	public int compareTo(MonitorTriggerRecord other) {
		if (this.priority == null) {
			return 1;
		}
		if (other.priority == null) {
			return -1;
		}
		return Integer.valueOf(other.priority) - Integer.valueOf(this.priority);
	}

    /**
     * 触发器ID
     */
    @JsonProperty("trigger_id")
    private String triggerId;

    /**
     * 触发器名称
     */
    private String name;

    /**
     * 表达式
     */
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
    private String priority;

    /**
     * 监控项ID
     */
    @JsonProperty("item_id")
    private String itemId;

    /**
     * 脚本类监控触发器的参数值
     */
    private String param;
} 
