package com.aspire.mirror.template.server.dao.po;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 动作持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po
 * 类名称:    Actions.java
 * 类描述:    动作持久类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Actions implements Serializable {

    private static final long serialVersionUID = -8677525616280762086L;

    // 事件ID
    private String actionId;

    // 事件名
    private String name;

    // 事件来源

    private String eventSource;

    // 表示执行action的前提条件的逻辑关系

    private String evalType;

    // 状态
    private String status;

    // 类型
    private String type;

    // 处理程序
    private String dealer;

    // 触发器ID
    private String triggerId;

    // 事件类型
    private Integer eventType;

} 
