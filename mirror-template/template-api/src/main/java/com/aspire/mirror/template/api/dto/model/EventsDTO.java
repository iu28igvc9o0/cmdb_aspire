package com.aspire.mirror.template.api.dto.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件持久对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto.model
 * 类名称:    EventsDTO.java
 * 类描述:    事件业务类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class EventsDTO implements Serializable {
	
	private static final long serialVersionUID = -5651980884762487531L;

    /** 序列号 */
    @ApiModelProperty(value = "序列号")
    private String eventId;

    /** 事件来源：
TRIGGERS-触发器
DISCOVERY-新发现
REGISTRATION-自动注册（agent/proxy）
 */
    @ApiModelProperty(value = "事件来源")
    private String source;

    /** TRIGGER-触发器 */
    @ApiModelProperty(value = "TRIGGER-触发器")
    private String object;

    /** object类型对应的object的ID（触发器ID） */
    @ApiModelProperty(value = "object类型对应的object的ID（触发器ID）")
    private String objectId;

    /** 事件类型
1-异常
0-正常 */
    @ApiModelProperty(value = "事件类型")
    private String value;

    /** 确认标识：
0-未确认
1-已确认 */
    @ApiModelProperty(value = "确认标识")
    private String acknowledged;

    /** 事件产生时间 */
    @ApiModelProperty(value = "事件产生时间")
    private Integer clock;

    /** 纳秒
小于秒的部分 */
    @ApiModelProperty(value = "纳秒")
    private Integer ns;

    /** 事件产生对象 */
    @ApiModelProperty(value = "事件产生对象")
    private String deviceId;

} 
