package com.aspire.mirror.template.server.dao.po;

import java.io.Serializable;
import java.util.Date;


import com.aspire.mirror.template.api.dto.model.ItemExt;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 监控项持久对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po
 * 类名称:    Items.java
 * 类描述:    监控项持久类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Items implements Serializable {
	
	private static final long serialVersionUID = -8110842984465544671L;

    // 监控项ID
    private String itemId;

    // 监控项名称
    private String name;

    // 监控项类型
    private String type;

    // 模版ID
    private String templateId;
    
    private Long prototypeId;
    
    @JsonIgnore
    private String[] templateIdArr;

    // 监控键值
    private String key;

    // 监控周期，单位分钟
    private String delay;

    // 保留时间，单位天
    private Integer history;

    // 状态
    private String status;

    // 监控项数据格式
    private String valueType;

    // 单位
    private String units;

    // 错误信息
    private String error;

    // 监控项数据类型

    private String dataType;

    // 接入监控系统类型（目前为zabbix）
    private String sysType;

    //模板名称
    private String templateName;

    /**
     * 采集周期计算类型
     */
    private String calcType;

    /**
     * 对应的搜索引擎数据库索引
     */
    private String bizIndex;

    /**
     * 统计条件
     */
    private String bizCalcObj;

    /**
     * 计算表达式
     */
    private String bizCalcExp;

    /**
     * 主题ID
     */
    private String bizThemeId;

    /**
     * 是否监控空值
     */
    private String bizIsZero;

    /**
     * 计算项分组[code1,code2]
     */
    private String bizGroup;

    /**
     * 指标计算公式[{name:value,exp:value},{name:value,exp:value}]
     */
    private String bizThemeExp;
    /**
     * 是否根据模板关联对象进行分组0：否1：是
     */
    private String groupFlag;

    private String creater;

    private Date createTime;

    private String itemGroup;

    private String zabbixItemId;

    private String scriptParam;

    private String customizeParam;

    private ItemExt itemExt;
}
