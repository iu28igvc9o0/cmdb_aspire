package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 模板修改请求VO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompTemplateUpdateRequest.java
 * 类描述:    模板修改请求
 * 创建人:    JinSu
 * 创建时间:  2018/8/6 11:04
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompTemplateUpdateRequest implements Serializable {
    private static final long serialVersionUID = -4209150322751578510L;
//    /**
//     * 模板ID
//     */
//    private String templateId;
    /**
     * 模板名称
     */
    private String name;

    /**
     * 模版类型：
     * 1-硬件
     * 2-网络
     * 3-主机操作系统
     * 4-应用
     */
    private String type;

    /**
     * 功能类型
     * 1-监控
     * 2-巡检
     */
    @JsonProperty("fun_type")
     private String funType;

    /**
     * 监控类型
     * 1-系统
     * 2-业务
     */
    @JsonProperty("mon_type")
    private String monType;

    /**
     * 系统类型
     * ZABBIX
     * PROMETHEUS
     * THEME
     * MIRROR
     */
    @JsonProperty("sys_type")
    private String sysType;

    private String status;

    /**
     * 触发器信息
     */
    @JsonProperty("trigger_list")
    private List<CompTriggerCreateRequest> triggerList;


    /**
     * 脚本类监控触发器的参数值
     */
    private String param;

    /** 描述 */
    private String description;

    /**
     * 设备ID集合
     */
    @JsonProperty("object_ids")
    private String objectIds;

    @JsonProperty("item_list")
    private List<CompItemsCreateRequest> listItem;
}
