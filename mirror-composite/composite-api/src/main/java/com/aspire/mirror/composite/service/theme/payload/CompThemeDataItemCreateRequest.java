package com.aspire.mirror.composite.service.theme.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

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
public class CompThemeDataItemCreateRequest implements Serializable {

    private static final long serialVersionUID = 64829448210415528L;

    @NotBlank
    @JsonProperty("JCZB_Code")
    private String jczbCode;

    @JsonProperty("Aggregate_Flag")
    private String aggregateFlag;

    @NotBlank
    @JsonProperty("Log_Time")
    private String logTime;

    @NotBlank
    @JsonProperty("JCZB_Dim_Value")
    private String jczbDimValue;

    @NotBlank
    @JsonProperty("JCZB_Value")
    private String jczbValue;
}
