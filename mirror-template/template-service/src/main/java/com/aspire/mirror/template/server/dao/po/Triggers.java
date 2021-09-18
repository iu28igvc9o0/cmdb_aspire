package com.aspire.mirror.template.server.dao.po;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 触发器持久对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po
 * 类名称:    Triggers.java
 * 类描述:    触发器持久类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Triggers implements Serializable {
	
	private static final long serialVersionUID = -7667960291932645605L;

    // 触发器ID
    private String triggerId;

    // 触发器名称
    private String name;

    // 表达式
    private String expression;

    // URL
    private String url;

    // 状态
    private String status;

    // 值类型
    private String value;

    // 优先级

    private String priority;

    // 监控项ID
    private String itemId;

    // 脚本类监控触发器的参数值
    private String param;

    // 1为普通 2为动态阈值类型
    private String type;

} 
