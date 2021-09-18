package com.aspire.mirror.template.api.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 监控项视图层对象
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto.vo
 * 类名称:   ItemsVO.java
 * 类描述:   monitor_items视图层属性，属性范围>=表结构属性.
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class ItemsVO implements Serializable {
	
	private static final long serialVersionUID = -5318703715993534628L;

    /** 监控项ID */
    @JsonProperty("item_id")
    private String itemId;

    @JsonProperty("zabbix_item_id")
    private String zabbixItemId;

    /** 监控项名称 */
    private String name;

    /** 监控项类型
SCRIPT-脚本（现有）
INDEX-监控指标（现有） */
    private String type;

    /** 模版ID */
    @JsonProperty("template_id")
    private String templateId;

    /** 监控键值 */
    private String key;

    /** 监控周期，单位分钟 */
    private String delay;

    /** 保留时间，单位天 */
    private Integer history;

    /** 状态：
ON-启用
OFF-禁用
NONSUPPORT-不支持（启用状态的监控项采集不到数据）（暂时不用） */
    private String status;

    /** 监控项数据格式：
FLOAT-浮点数
STR-字符串
LOG-日志
UINT64-整数
TEXT-文本 */
    @JsonProperty("value_type")
    private String valueType;

    /** 单位 */
    private String units;

    /** 错误信息 */
    private String error;

    /** 监控项数据类型
DECIMAL-十进制
OCTAL-八进制
HEXADECIMAL-十六进制
BOOLEAN-布尔值
 */
    @JsonProperty("data_type")
    private String dataType;

    /** 接入监控系统类型（目前为zabbix）
MIRROR
ZABBIX
NONE */
    @JsonProperty("sys_type")
    private String sysType;

    /**
     * 采集周期计算类型
     */
    @JsonProperty("calc_type")
    private String calcType;

    /**
     * 对应的搜索引擎数据库索引
     */
    @JsonProperty("biz_index")
    private String bizIndex;

    /**
     * 统计条件
     */
    @JsonProperty("biz_calc_obj")
    private String bizCalcObj;

    /**
     * 计算表达式
     */
    @JsonProperty("biz_calc_exp")
    private String bizCalcExp;

    /**
     * 主题ID
     */
    @JsonProperty("biz_theme_id")
    private String bizThemeId;

    /**
     * 是否监控空值
     */
    @JsonProperty("biz_is_zero")
    private String bizIsZero;

    /**
     * 计算项分组[code1,code2]
     */
    @JsonProperty("biz_group")
    private String bizGroup;

    /**
     * 指标计算公式[{name:value,exp:value},{name:value,exp:value}]
     */
    @JsonProperty("biz_theme_exp")
    private String bizThemeExp;

    /**
     * 是否根据模板关联对象进行分组0：否1：是
     */
    @JsonProperty("group_flag")
    private String groupFlag;
}
