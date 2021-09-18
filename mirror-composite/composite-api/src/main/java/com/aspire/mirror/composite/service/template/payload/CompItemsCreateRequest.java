package com.aspire.mirror.composite.service.template.payload;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 创建监控项对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompItemsCreateRequest.java
 * 类描述:    创建监控项对象
 * 创建人:    JinSu
 * 创建时间:  2018/8/8 14:57
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
@ToString
public class CompItemsCreateRequest implements Serializable {

    private static final long serialVersionUID = 4549978770071467155L;
    @JsonProperty("item_id")
    private String itemId;
    /**
     * 监控项名称
     */
    @NotBlank
    private String name;

    /**
     * 监控项类型
     * SCRIPT-脚本（现有）
     * INDEX-监控指标（现有）
     */
    private String type;

    /**
     * 模版ID
     */
    @NotBlank
    @JsonProperty("template_id")
    private String templateId;
    
    @JsonProperty("prototype_id")
    private Long prototypeId;

    /**
     * 监控键值
     */
    @NotBlank
    private String key;

    /**
     * 监控周期，单位分钟
     */
    private String delay;

    /**
     * 保留时间，单位天
     */
    private Integer history;

    /**
     * 状态：
     * ON-启用
     * OFF-禁用
     * NONSUPPORT-不支持（启用状态的监控项采集不到数据）（暂时不用）
     */
    private String status;

    /**
     * 监控项数据格式：
     * FLOAT-浮点数
     * STR-字符串
     * LOG-日志
     * UINT64-整数
     * TEXT-文本
     */
    @NotBlank
    @JsonProperty("value_type")
    private String valueType;

    /**
     * 单位
     */
    private String units;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 监控项数据类型
     * DECIMAL-十进制
     * OCTAL-八进制
     * HEXADECIMAL-十六进制
     * BOOLEAN-布尔值
     */
    @JsonProperty("data_type")
    private String dataType;

    /**
     * 接入监控系统类型（目前为zabbix）
     * MIRROR
     * ZABBIX
     * NONE
     */
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

    private String expression;

    private String match;

    @JsonProperty("item_group")
    private String itemGroup;

    @JsonProperty("item_ext")
    ItemExt itemExt;
}
