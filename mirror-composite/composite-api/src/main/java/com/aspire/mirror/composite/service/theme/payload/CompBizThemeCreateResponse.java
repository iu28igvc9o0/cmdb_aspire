package com.aspire.mirror.composite.service.theme.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 主题创建返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto
 * 类名称:    CompBizThemeCreateResponse.java
 * 类描述:    主题创建返回
 * 创建人:    JinSu
 * 创建时间:  2018/10/22 17:15
 * 版本:      v1.0
 */
@Data
@ToString
public class CompBizThemeCreateResponse implements Serializable {
    private static final long serialVersionUID = -540608267910555134L;
    /**
     * 主题ID
     */
    @JsonProperty("theme_id")
    private String themeId;

    /**
     *主题名称
     */
    @JsonProperty("theme_name")
    private String themeName;
}
