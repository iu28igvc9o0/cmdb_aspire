package com.aspire.mirror.template.server.dao.po;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_functions持久对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po
 * 类名称:    Functions.java
 * 类描述:    monitor_functions持久类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Functions implements Serializable {
	
	private static final long serialVersionUID = -5202979605593275965L;

    // 函数ID
    private String functionId;

    // 监控项ID
    private String itemId;

    // 触发器ID
    private String triggerId;

    // 函数
    private String function;

    // 参数
    private String parameter;

} 
