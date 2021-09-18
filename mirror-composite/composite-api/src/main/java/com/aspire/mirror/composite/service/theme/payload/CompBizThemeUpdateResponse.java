package com.aspire.mirror.composite.service.theme.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 主题修改
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto
 * 类名称:    CompBizThemeUpdateResponse.java
 * 类描述:    主题修改
 * 创建人:    JinSu
 * 创建时间:  2018/10/22 17:16
 * 版本:      v1.0
 */
@Data
@ToString
public class CompBizThemeUpdateResponse implements Serializable {

    private static final long serialVersionUID = -5742708674419025340L;

    /**
     * 主题ID
     */
    @JsonProperty("theme_id")
    private String themeId;

    /**
     * 主题名称
     */
    @JsonProperty("theme_name")
    private String themeName;
}
