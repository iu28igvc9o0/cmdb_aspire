package com.aspire.mirror.template.api.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件视图层对象
 * <p>
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto.vo
 * 类名称:   EventsVO.java
 * 类描述:   monitor_events视图层属性，属性范围>=表结构属性.
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:09
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class EventsVO implements Serializable {

    private static final long serialVersionUID = -8175305721915208757L;

    /**
     * 序列号
     */
    @JsonProperty("event_id")
    private String eventId;

    /**
     * 事件来源：
     * TRIGGERS-触发器
     * DISCOVERY-新发现
     * REGISTRATION-自动注册（agent/proxy）
     */
    private String source;

    /**
     * TRIGGER-触发器
     */
    private String object;

    /**
     * object类型对应的object的ID（触发器ID）
     */
    @JsonProperty("object_id")
    private String objectId;

    /**
     * 事件类型
     * 1-异常
     * 0-正常
     */
    private String value;

    /**
     * 确认标识：
     * 0-未确认
     * 1-已确认
     */
    private String acknowledged;

    /**
     * 事件产生时间
     */
    private Integer clock;

    /**
     * 纳秒
     * 小于秒的部分
     */
    private Integer ns;

    /**
     * 事件产生对象
     */
    @JsonProperty("device_id")
    private String deviceId;

} 
