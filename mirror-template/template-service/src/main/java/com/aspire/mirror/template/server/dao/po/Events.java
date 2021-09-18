package com.aspire.mirror.template.server.dao.po;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件持久对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po
 * 类名称:    Events.java
 * 类描述:    事件持久类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Events implements Serializable {
	
	private static final long serialVersionUID = -7607543225439188494L;

    // 序列号
    private String eventId;

    // 事件来源：

    private String source;

    // TRIGGER-触发器
    private String object;

    // object类型对应的object的ID（触发器ID）
    private String objectId;

    // 事件类型
    private String value;

    // 确认标识
    private String acknowledged;

    // 事件产生时间
    private Integer clock;

    // 纳秒
    private Integer ns;

    // 事件产生对象
    private String deviceId;

} 
