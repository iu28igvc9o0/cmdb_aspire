package com.aspire.mirror.composite.service.theme.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

/**
 * 业务主题创建请求
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto
 * 类名称:    CompBizThemeCreateRequest.java
 * 类描述:    业务主题创建请求
 * 创建人:    JinSu
 * 创建时间:  2018/10/22 17:15
 * 版本:      v1.0
 */
@Data
@ToString
public class CompBizThemeCreateRequest {
    /** 主题编码 */
    @JsonProperty("theme_code")
    private String themeCode;

    /** 关联对象类型
     1-设备ID
     2-业务系统 */
    @NotBlank
    @JsonProperty("object_type")
    private String objectType;

    /** object_id */
    @JsonProperty("object_id")
    private String objectId;

    /** es索引名 */
    @JsonProperty("index_name")
    private String indexName;

    /** 数据类型0：数字1：字符串 */
//    @NotBlank
    @JsonProperty("value_type")
    private String valueType;

    /** 上报类型0：接口接入1：日志接入 */
    @NotBlank
    @JsonProperty("up_type")
    private String upType;


    /** 上报开关0：开启1：关闭 */
//    @NotBlank
    @JsonProperty("up_switch")
    private String upSwitch;

    /** 状态0：正式1：临时 */
//    @NotBlank
    private String status;

    /** 维度集合 */
    @NotEmpty
    @JsonProperty("dim_list")
    private List<CompBizThemeDimVO> dimList;

    /** 主题名称 */
    @NotBlank
    @JsonProperty("theme_name")
    private String themeName;

    /**
     * 日志正则表达式
     */
    @JsonProperty("log_reg")
    private String logReg;

    /**
     * 日志内容
     */
    @JsonProperty("log_content")
    private String logContent;

    /**
     * 主题主键维度编码
     */
    @JsonProperty("theme_key")
    private String themeKey;
}
