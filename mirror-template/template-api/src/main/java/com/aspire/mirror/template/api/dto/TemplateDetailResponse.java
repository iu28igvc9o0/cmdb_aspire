package com.aspire.mirror.template.api.dto;

import com.aspire.mirror.template.api.dto.model.ItemExt;
import com.aspire.mirror.template.api.dto.model.TemplateDataSyncDTO;
import com.aspire.mirror.template.api.dto.model.TemplateObjectDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 模板详情对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    TemplateDetailResponse.java
 * 类描述:    模板创建响应对象
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@NoArgsConstructor
public class TemplateDetailResponse extends TemplateDateSyncVo implements Serializable {

    private static final long serialVersionUID = -4929022356099274462L;

    /**
     * 模版ID
     */
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 模版名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("create_time")
    private java.util.Date createTime;

    /**
     * 模版类型：
     * 1-硬件
     * 2-网络
     * 3-主机操作系统
     * 4-应用
     */
    private String type;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("update_time")
    private java.util.Date updateTime;

    /**
     * 监控项列表
     */
    @JsonProperty("item_list")
    private List<ItemsDetailResponse> itemList;

    /**
     * 功能类型
     * 1-监控
     * 2-巡检
     */
    @JsonProperty("fun_type")
    private String funType;

    /**
     * 监控类型：
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
    /**
     * 主机数
     */
//    @JsonProperty("device_num")
//    private int deviceNum;

    /**
     * 主机信息
     */
//    private String devices;
    /**
     * 状态
     * 0-临时
     * 1-正式
     */
    private String status;

    @JsonProperty("template_object_list")
    private List<TemplateObjectDTO> templateObjectList;

    private String creater;

    private String updater;
}
