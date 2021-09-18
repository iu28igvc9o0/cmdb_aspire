package com.aspire.mirror.theme.server.dao.po;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.dao.po
 * 类名称:    ThemeKey.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/1/21 14:50
 * 版本:      v1.0
 */
@Data
@ToString
public class ThemeKey implements Serializable {

    private String themeId;

    private String dimCode;
}
