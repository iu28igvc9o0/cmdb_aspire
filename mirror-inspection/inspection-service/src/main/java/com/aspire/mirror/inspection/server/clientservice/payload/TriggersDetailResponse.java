package com.aspire.mirror.inspection.server.clientservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class TriggersDetailResponse implements Serializable {

    private static final long serialVersionUID = -5912865328976918136L;

    private static final String[] matchArray = {">=", "<=", "!=", "==", "<", ">"};

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


    public static String getMatch(String exp) {
        for (String match : matchArray) {
            if (exp.startsWith(match)) {
                return exp.substring(0, match.length());
            }
        }
        return "";
    }
    public static String getExpressionValue (String exp) {
        for (String match : matchArray) {
            if (exp.startsWith(match)) {
                return exp.substring(match.length());
            }
        }
        return exp;
    }
} 
