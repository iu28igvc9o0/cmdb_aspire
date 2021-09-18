package com.aspire.mirror.composite.service.theme.payload;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 主题分页请求对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto
 * 类名称:    BizThemePageReqest.java
 * 类描述:    主题分页请求对象
 * 创建人:    JinSu
 * 创建时间:  2018/10/22 17:15
 * 版本:      v1.0
 */
@Data
@ToString
public class CompBizThemePageRequest extends GeneralAuthConstraintsAware implements Serializable {
    private static final long serialVersionUID = 3608280371321886019L;


    @NotNull
    @JsonProperty("page_size")
    private Integer pageSize;

    @NotNull
    @JsonProperty("page_no")
    private Integer pageNo;

    @JsonProperty("sort_name")
    private String sortName;

    private String order;

    @JsonProperty("theme_name")
    private String themeName;

    @JsonProperty("object_id")
    private String objectId;

    @JsonProperty("object_type")
    private String objectType;

    private String status;
}
