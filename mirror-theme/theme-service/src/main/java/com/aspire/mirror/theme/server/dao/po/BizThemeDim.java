package com.aspire.mirror.theme.server.dao.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 主题维度PO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.dao.po
 * 类名称:    BizThemeDim.java
 * 类描述:    主题维度PO
 * 创建人:    JinSu
 * 创建时间:  2018/10/23 20:12
 * 版本:      v1.0
 */
@Data
public class BizThemeDim {
    /**
     * id
     */
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
