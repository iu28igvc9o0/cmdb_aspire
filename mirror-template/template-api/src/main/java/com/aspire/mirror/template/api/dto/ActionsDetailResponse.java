package com.aspire.mirror.template.api.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_actions详情对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    ActionsDetailResponse.java
 * 类描述:    monitor_actions创建响应对象
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionsDetailResponse extends TemplateDateSyncVo implements Serializable {

    private static final long serialVersionUID = -9096648659737146369L;

    /**
     * 事件ID
     */
    @JsonProperty("action_id")
    private String actionId;

    /**
     * 事件名
     */
    private String name;

    /**
     * 事件来源
     * 0-指来源为触发器trigger
     * 1-指来源为自动发现descover
     * 2-指来源为自动登记auto_register
     * 3-为网络发现产生的事件源
     */
    @JsonProperty("event_source")
    private String eventSource;

    /**
     * 表示执行action的前提条件的逻辑关系
     * 0表示and/or
     * 1表示and
     * 2表示or
     */
    @JsonProperty("eval_type")
    private String evalType;

    /**
     * 状态
     * ON-启动
     * OFF-禁用
     */
    private String status;

    /**
     * 类型
     * 1-回调url
     * 2-函数
     */
    private String type;

    /**
     * 处理程序
     * 如果type为1，则为url
     * 如果type为2，则为类名.方法名
     * 入参包含事件、指标、触发器信息，需定义
     */
    private String dealer;

    /**
     * 触发器ID
     */
    @JsonProperty("trigger_id")
    private String triggerId;

    /**
     * 事件类型：
     * 1-异常事件
     * 2-正常事件
     * 3-通用事件
     */
    @JsonProperty("event_type")
    private Integer eventType;

} 
