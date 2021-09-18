package com.aspire.mirror.theme.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 主题数据创建
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto
 * 类名称:    ThemeDataDTO.java
 * 类描述:    主题数据创建
 * 创建人:    JinSu
 * 创建时间:  2018/10/25 11:14
 * 版本:      v1.0
 */
@Data
@ToString
public class ThemeDataCreateRequest implements Serializable {
    private static final long serialVersionUID = 6158847233943628080L;
    @JsonProperty("SysCode")
    private String sysCode;

    @JsonProperty("DeviceIp")
    private String deviceIp;

    @NotBlank
    @JsonProperty("Time")
    private String time;

    @NotEmpty
    @JsonProperty("Datas")
    private List<String> datas;

    @JsonProperty("Host")
    private String host;

    @NotBlank
    @JsonProperty("ThemeCode")
    private String themeCode;
}
