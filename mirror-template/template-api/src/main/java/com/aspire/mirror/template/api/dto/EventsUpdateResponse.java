package com.aspire.mirror.template.api.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_events修改对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    EventsUpdateResponse.java
 * 类描述:    monitor_events修改响应对象
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:09
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class EventsUpdateResponse implements Serializable {
	
	private static final long serialVersionUID = -7308880122730370915L;

    /** 序列号 */
    @JsonProperty("event_id")
    private String eventId ;

    /** 事件来源：
TRIGGERS-触发器
DISCOVERY-新发现
REGISTRATION-自动注册（agent/proxy）
 */
    private String source ;

    /** TRIGGER-触发器 */
    private String object ;

    /** object类型对应的object的ID（触发器ID） */
    @JsonProperty("object_id")
    private String objectId ;

    /** 事件类型
1-异常
0-正常 */
    private String value ;

    /** 确认标识：
0-未确认
1-已确认 */
    private String acknowledged ;

    /** 事件产生时间 */
    private Integer clock ;

    /** 纳秒
小于秒的部分 */
    private Integer ns ;

    /** 事件产生对象 */
    @JsonProperty("device_id")
    private String deviceId ;

} 
