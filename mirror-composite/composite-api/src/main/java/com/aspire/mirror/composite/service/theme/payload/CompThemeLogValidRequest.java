package com.aspire.mirror.composite.service.theme.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.theme.payload
 * 类名称:    CompThemeLogValidRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/1/29 17:30
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class CompThemeLogValidRequest implements Serializable {

    @NotBlank
    @JsonProperty("log_content")
    private String logContent;

    @NotBlank
    @JsonProperty("dim_list")
    private String dimList;
}
