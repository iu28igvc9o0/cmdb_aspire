package com.aspire.mirror.template.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 模板page请求对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.api.dto
 * 类名称:    TemplatePageRequest.java
 * 类描述:    模板page请求对象
 * 创建人:    JinSu
 * 创建时间:  2018/7/28 15:48
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class TemplatePageRequest {
    /**
     * 模版名称
     */
    private String name;

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
     * 模版分类
     */
    private String type;

    /**
     * 功能分类
     */
    @JsonProperty("fun_type")
    private String funType;
    /**
     * 创建开始时间
     */
    @JsonProperty("create_time_start")
    private String createTimeStart;
    /**
     * 创建结束时间
     */
    @JsonProperty("create_time_end")
    private String createTimeEnd;
    /**
     * 每页显示记录条数
     */
    @JsonProperty("page_size")
    private Integer pageSize;
    /**
     * 第几页
     */
    @JsonProperty("page_no")
    private Integer pageNo;

    private String status;
}
