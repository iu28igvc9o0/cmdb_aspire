package com.aspire.mirror.theme.api.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 主题维度业务实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto.model
 * 类名称:    BizThemeDimDTO.java
 * 类描述:    主题维度业务实体
 * 创建人:    JinSu
 * 创建时间:  2018/10/23 17:18
 * 版本:      v1.0
 */
@Data
@ToString
public class BizThemeDimDTO {
    private Integer id;
    /**
     * 维度名称
     */
    private String dimName;

    /**
     * 维度类型
     */
    private String dimType;

    /** 维度序号 */
    private Integer dimOrder;

    /** 维度编码 */
    private String dimCode;

    /** 正则表达式 */
    private String dimReg;

    /** 匹配类型 */
    private String matchFlag;

    /** 主题ID */
    private String themeId;
}
