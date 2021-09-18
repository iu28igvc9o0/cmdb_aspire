package com.aspire.mirror.theme.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto
 * 类名称:    ThemeLogValidRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/1/29 17:34
 * 版本:      v1.0
 */
@Data
@ToString
public class ThemeLogValidRequest {
    @NotBlank
    @JsonProperty("log_content")
    private String logContent;

    @NotBlank
    @JsonProperty("dim_list")
    private String dimList;
}
