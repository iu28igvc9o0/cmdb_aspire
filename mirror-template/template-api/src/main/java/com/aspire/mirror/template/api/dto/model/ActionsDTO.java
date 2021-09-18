package com.aspire.mirror.template.api.dto.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 动作持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto.model
 * 类名称:    ActionsDTO.java
 * 类描述:    动作业务类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ActionsDTO implements Serializable {

    private static final long serialVersionUID = -7328138112612503386L;

    /**
     * 事件ID
     */
    @ApiModelProperty(value = "事件ID")
    private String actionId;

    /**
     * 事件名
     */
    @ApiModelProperty(value = "事件名")
    private String name;

    /**
     * 事件来源
     * 0-指来源为触发器trigger
     * 1-指来源为自动发现descover
     * 2-指来源为自动登记auto_register
     * 3-为网络发现产生的事件源
     */
    @ApiModelProperty(value = "事件来源")
    private String eventSource;

    /**
     * 表示执行action的前提条件的逻辑关系
     * 0表示and/or
     * 1表示and
     * 2表示or
     */
    @ApiModelProperty(value = "表示执行action的前提条件的逻辑关系")
    private String evalType;

    /**
     * 状态
     * ON-启动
     * OFF-禁用
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 类型
     * 1-回调url
     * 2-函数
     */
    @ApiModelProperty(value = "类型")
    private String type;

    /**
     * 处理程序
     * 如果type为1，则为url
     * 如果type为2，则为类名.方法名
     * 入参包含事件、指标、触发器信息，需定义
     */
    @ApiModelProperty(value = "处理程序")
    private String dealer;

    /**
     * 触发器ID
     */
    @ApiModelProperty(value = "触发器ID")
    private String triggerId;

    /**
     * 事件类型：
     * 1-异常事件
     * 2-正常事件
     * 3-通用事件
     */
    @ApiModelProperty(value = "事件类型")
    private Integer eventType;

} 
