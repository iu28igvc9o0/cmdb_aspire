package com.aspire.mirror.ops.api.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自动化自定义参数实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsParam.java
 * 类描述:    自动化自定义参数实体
 * 创建人:    JinSu
 * 创建时间:  2020/6/10 17:20
 * 版本:      v1.0
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class OpsParam extends OpsGroupObject {
    @JsonProperty("param_id")
    private Long paramId;
    @JsonProperty("param_code")
    private String paramCode;
    @JsonProperty("param_name")
    private String paramName;
    @JsonProperty("param_desc")
    private String paramDesc;
    @JsonProperty("param_type")
    private String paramType;
    @JsonProperty("param_type_def")
    private OpsParamType paramTypeDef;
    private Integer length;
    @JsonProperty("param_default_value")
    private String paramDefaultValue;
    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonProperty("last_update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;
}
