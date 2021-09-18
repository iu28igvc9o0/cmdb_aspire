package com.aspire.mirror.composite.service.template.payload;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 监控项详情返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompItemsDetailResponse.java
 * 类描述:    监控项详情返回
 * 创建人:    JinSu
 * 创建时间:  2018/8/7 20:17
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompItemsDetailResponse implements Serializable {
    private static final long serialVersionUID = -6643552318936177730L;
    /**
     * 监控项ID
     */
    @JsonProperty("item_id")
    private String itemId;

    /**
     * 监控项类型
     * SCRIPT-脚本（现有）
     * INDEX-监控指标（现有）
     */
    private String type;

    /**
     * 模版ID
     */
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 监控键值
     */
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


    @JsonProperty("template_name")
    private String templateName;



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

    /**
     * 监控项名称
     */
    @Excel(name = "监控项名称", width = 30)
    private String name;

    @Excel(name = "脚本名称", width = 30)
    @JsonProperty("script_name")
    private String scriptName;


    @Excel(name = "脚本类型", replace = {"sh_1", "bat_2", "python_3"})
    @JsonProperty("content_type")
    private Integer contentType;  // 脚本内容类型    1 sh 2 bat 3 python  参考ScriptContentTypeEnum

    /**
     * 计算项分组[code1,code2]
     */
    @Excel(name = "分类", width = 20)
    @JsonProperty("biz_group")
    private String bizGroup;

    @Excel(name = "指标分组", replace = {"低_1", "中_2", "高_3"})
    @JsonProperty("item_group")
    private String itemGroup;
    /**
     * 表达式信息
     */
    @Excel(name = "匹配规则", width = 50)
    private String expression;

    @Excel(name = "创建人", width = 15)
    private String creater;

    @Excel(name = "创建时间", width = 20,  format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_time")
    private Date createTime;

    @JsonProperty("item_ext")
    private ItemExt itemExt;

    @JsonProperty("script_param")
    private String scriptParam;

    @JsonProperty("customize_param")
    private String customizeParam;
}
