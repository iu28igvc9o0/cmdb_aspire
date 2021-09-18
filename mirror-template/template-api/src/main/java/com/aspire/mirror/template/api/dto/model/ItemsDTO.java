package com.aspire.mirror.template.api.dto.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 监控项持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto.model
 * 类名称:    ItemsDTO.java
 * 类描述:    监控项业务类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@EqualsAndHashCode(of={"itemId", "key", "delay", "name", "value_type", "data_type", "units"}, callSuper=false)
public class ItemsDTO implements Serializable {

    private static final long serialVersionUID = -7899856346116359240L;

    /**
     * 监控项ID
     */
    @ApiModelProperty(value = "监控项ID")
    private String itemId;

    /**
     * 监控项名称
     */
    @ApiModelProperty(value = "监控项名称")
    private String name;

    /**
     * 监控项类型
     * SCRIPT-脚本（现有）
     * INDEX-监控指标（现有）
     */
    @ApiModelProperty(value = "监控项类型")
    private String type;

    /**
     * 模版ID
     */
    @ApiModelProperty(value = "模版ID")
    private String templateId;
    
    @ApiModelProperty(value = "指标原型ID")
    private Long prototypeId;

    /**
     * 监控键值
     */
    @ApiModelProperty(value = "监控键值")
    private String key;

    /**
     * 监控周期，单位分钟
     */
    @ApiModelProperty(value = "监控周期，单位分钟")
    private String delay;

    /**
     * 保留时间，单位天
     */
    @ApiModelProperty(value = "保留时间，单位天")
    private Integer history;

    /**
     * 状态：
     * ON-启用
     * OFF-禁用
     * NONSUPPORT-不支持（启用状态的监控项采集不到数据）（暂时不用）
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 监控项数据格式：
     * 0-浮点数
     * 1-字符串
     * 2-日志
     * 3-整数
     * 5-文本
     */
    @ApiModelProperty(value = "监控项数据格式")
    private String valueType;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String units;

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String error;

    /**
     * 监控项数据类型
     * DECIMAL-十进制
     * OCTAL-八进制
     * HEXADECIMAL-十六进制
     * BOOLEAN-布尔值
     */
    @ApiModelProperty(value = "监控项数据类型")
    private String dataType;

    /**
     * 接入监控系统类型（目前为zabbix）
     * MIRROR
     * ZABBIX
     * NONE
     */
    @ApiModelProperty(value = "接入监控系统类型（目前为zabbix）")
    private String sysType;

    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String templateName;

    /**
     * 采集周期计算类型
     */
    @ApiModelProperty("采集周期计算类型")
    private String calcType;

    /**
     * 对应的搜索引擎数据库索引
     */
    @ApiModelProperty("对应的搜索引擎数据库索引")
    private String bizIndex;

    /**
     * 统计条件
     */
    @ApiModelProperty("对应的搜索引擎数据库索引")
    private String bizCalcObj;

    /**
     * 计算表达式
     */
    @ApiModelProperty("计算表达式")
    private String bizCalcExp;

    /**
     * 主题ID
     */
    @ApiModelProperty("主题ID")
    private String bizThemeId;

    /**
     * 是否监控空值
     */
    @ApiModelProperty("是否监控空值")
    private String bizIsZero;

    /**
     * 计算项分组[code1,code2]
     */
    @ApiModelProperty("计算项分组")
    private String bizGroup;

    /**
     * 指标计算公式[{name:value,exp:value},{name:value,exp:value}]
     */
    @ApiModelProperty("指标计算公式")
    private String bizThemeExp;

    /**
     * 是否根据模板关联对象进行分组0：否1：是
     */
    @ApiModelProperty("是否根据模板关联对象进行分组")
    private String groupFlag;

    private String creater;

    private Date createTime;

    private String itemGroup;

    private String zabbixItemId;

    private String scriptParam;

    private String customizeParam;

    ItemExt itemExt;
}
