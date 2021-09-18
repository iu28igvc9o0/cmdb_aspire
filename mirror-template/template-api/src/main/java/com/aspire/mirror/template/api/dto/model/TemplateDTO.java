package com.aspire.mirror.template.api.dto.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模板持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto.model
 * 类名称:    TemplateDTO.java
 * 类描述:    模板业务类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TemplateDTO implements Serializable {

    private static final long serialVersionUID = -8373601616545175867L;

    /**
     * 模版ID
     */
    @ApiModelProperty(value = "模版ID")
    private String templateId;

    /**
     * 模版名称
     */
    @ApiModelProperty(value = "模版名称")
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    /**
     * 模版类型：
     * 1-硬件
     * 2-网络
     * 3-主机操作系统
     * 4-应用
     */
    @ApiModelProperty(value = "模版类型")
    private String type;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;

    /**
     * 主机
     */
    @ApiModelProperty(value = "指标列表")
    private List<ItemsDTO> itemList;

    /**
     * 功能类型
     * 1-监控
     * 2-巡检
     */
    @ApiModelProperty(value = "功能类型")
    private String funType;

    /**
     * 监控类型：
     * 1-系统
     * 2-业务
     */
    @ApiModelProperty(value = "监控类型")
    private String monType;
    /**
     * 系统类型
     * ZABBIX
     * PROMETHEUS
     * THEME
     * MIRROR
     */
    @ApiModelProperty(value = "系统类型")
    private String sysType;

    /**
     * 状态
     * 0-临时
     * 1-正式
     */
    private String status;

    private String proxyIdentity;

    private String zabbixTemplateId;

    @ApiModelProperty(value = "模板对象列表")
    private List<TemplateObjectDTO> templateObjectList;

    private String creater;

    private String updater;
}
