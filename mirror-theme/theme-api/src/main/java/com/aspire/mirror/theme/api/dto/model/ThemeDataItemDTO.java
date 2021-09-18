package com.aspire.mirror.theme.api.dto.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 主题数据元素
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto
 * 类名称:    ThemeDataItemDTO.java
 * 类描述:    主题数据元素
 * 创建人:    JinSu
 * 创建时间:  2018/10/25 11:24
 * 版本:      v1.0
 */
@Data
@ToString
public class ThemeDataItemDTO implements Serializable {

    private static final long serialVersionUID = 64829448210415528L;

    private String jczbCode;

    private String aggregateFlag;

    private String logTime;

    private String jczbDimValue;

    private String jczbValue;

    private List<BizThemeDimData> lstJczbDim;
}
