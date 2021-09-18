package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 模板信息创建请求VO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.service.template.payload
 * 类名称:    CompTemplateCreateRequest.java
 * 类描述:    模板信息创建请求VO
 * 创建人:    JinSu
 * 创建时间:  2018/8/2 15:29
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompTemplateCreateRequest implements Serializable {
    private static final long serialVersionUID = 2986293836672531653L;
    /**
     * 模板名称
     */
    @NotBlank
    private String name;

    /** 描述 */
    private String description;

    /** 主机 */
//    private String devices;

    /**
     * 模版类型：
     * 1-硬件
     * 2-网络
     * 3-主机操作系统
     * 4-应用
     */
    @NotBlank
    private String type;

    /**
     * 功能类型
     * 1-监控
     * 2-巡检
     */
    @NotBlank
    @JsonProperty("fun_type")
    private String funType;

    /**
     * 监控类型：
     * 1-系统
     * 2-业务
     */
    @NotBlank
    @JsonProperty("mon_type")
    private String monType;
    /**
     * 系统类型
     * ZABBIX
     * PROMETHEUS
     * THEME
     * MIRROR
     */
    @NotBlank
    @JsonProperty("sys_type")
    private String sysType;

    /**
     * 状态
     * 0-临时
     * 1-正式
     */
    private String status;

    /**
     * 设备ID集合
     */
    @JsonProperty("object_ids")
    private String objectIds;

    @JsonProperty("item_list")
    private List<CompItemsCreateRequest> listItem;
}
