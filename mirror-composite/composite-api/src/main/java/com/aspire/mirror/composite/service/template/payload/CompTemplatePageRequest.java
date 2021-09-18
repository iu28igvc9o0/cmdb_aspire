package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 模板列表请求
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompTemplatePageRequest.java
 * 类描述:    模板列表请求
 * 创建人:    JinSu
 * 创建时间:  2018/8/8 11:04
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompTemplatePageRequest implements Serializable {
    private static final long serialVersionUID = 208243511575739249L;
    /**
     * 模版名称
     */
    private String name;

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
}
