package com.aspire.mirror.composite.service.theme.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 业务主题维度VO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto.vo
 * 类名称:    CompBizThemeDimVO.java
 * 类描述:    业务主题维度VO
 * 创建人:    JinSu
 * 创建时间:  2018/10/22 17:29
 * 版本:      v1.0
 */
@Data
@ToString
public class CompBizThemeDimVO implements Serializable {

    private static final long serialVersionUID = -6864082533525659747L;

    /** 维度名称 */
    @JsonProperty("dim_name")
    private String dimName;

    /** 维度类型 */
    @JsonProperty("dim_type")
    private String dimType;


    /** 维度序号 */
    @JsonProperty("dim_order")
    private Integer dimOrder;

    /** 维度编码 */
    @JsonProperty("dim_code")
    private String dimCode;

    /** 正则表达式 */
    @JsonProperty("dim_reg")
    private String dimReg;

    /** 匹配类型 */
    @JsonProperty("match_flag")
    private String matchFlag;
}
