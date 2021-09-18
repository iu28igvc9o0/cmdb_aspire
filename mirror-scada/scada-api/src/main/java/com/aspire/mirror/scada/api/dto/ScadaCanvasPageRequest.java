package com.aspire.mirror.scada.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.scada.api.dto
 * 类名称:    ScadaCanvasPageRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/5 12:44
 * 版本:      v1.0
 */
@Data
public class ScadaCanvasPageRequest {
    private String pod;

    private String idc;

    private String bizSystem;

    private String name;

    private String pictureType;

    private String projectName;

    @NotNull
    @JsonProperty("page_size")
    private Integer pageSize;

    @NotNull
    @JsonProperty("page_no")
    private Integer pageNo;
}

