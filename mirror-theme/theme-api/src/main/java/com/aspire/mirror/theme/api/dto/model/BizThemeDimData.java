package com.aspire.mirror.theme.api.dto.model;

import lombok.Data;
import lombok.ToString;


/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.entity
 * 类名称:    BizThemeDimData.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/10/26 14:44
 * 版本:      v1.0
 */
@Data
@ToString
public class BizThemeDimData {
    private String dimName;

    private String dimCode;

    private String dimValue;

    private String dimType;

    private String pattern;
}
