package com.aspire.mirror.theme.api.dto;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 主题搜索请求对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto
 * 类名称:    BizThemeSearchRequest.java
 * 类描述:    主题搜索请求对象
 * 创建人:    JinSu
 * 创建时间:  2018/10/23 16:43
 * 版本:      v1.0
 */
@Data
@ToString
public class BizThemeSearchRequest  extends GeneralAuthConstraintsAware implements Serializable {
    private static final long serialVersionUID = 8152922939617855885L;

    /** 主题ID */
    @JsonProperty("theme_id")
    private String themeId;

    /** 主题编码 */
    @JsonProperty("theme_code")
    private String themeCode;
    /** 关联对象类型
     1-设备ID
     2-业务系统 */
    @JsonProperty("object_type")
    private String objectType;

    /** object_id */
    @JsonProperty("object_id")
    private String objectId;

    /** es索引名 */
    @JsonProperty("index_name")
    private String indexName;

    /** 数据类型0：数字1：字符串 */
    @JsonProperty("value_type")
    private String valueType;

    /** 上报类型0：接口接入1：日志接入 */
    @JsonProperty("up_type")
    private String upType;

    /** 上报状态0：成功1：失败 */
    @JsonProperty("up_status")
    private String upStatus;

    /** 最近上报值 */
    @JsonProperty("last_up_value")
    private String lastUpValue;

    /** 上报开关0：开启1：关闭 */
    @JsonProperty("up_switch")
    private String upSwitch;

    /** 状态0：正式1：临时 */
    private String status;


    /** 主题名称 */
    @JsonProperty("theme_name")
    private String themeName;
}
