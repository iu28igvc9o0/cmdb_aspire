package com.aspire.mirror.alert.api.dto.filter;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 历史告警详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsHisDetailResponse.java
 * 类描述:    历史告警详情
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:18
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class AlertFilterSceneResponse {
	 @NotNull
    @JsonProperty("page_size")
    private Integer pageSize;

    @NotNull
    @JsonProperty("page_no")
    private Integer pageNo;

    @NotEmpty
    @JsonProperty("sort_name")
    private String sortName;

    @NotEmpty
    private String order;
    
    /**告警过滤名称*/
    @JsonProperty("name")
    private String name;
    
    /**告警过滤名称*/
    @JsonProperty("filterName")
    private String filterName;
    
    /**告警过滤名称*/
    @JsonProperty("operateUser")
    private String operateUser;
    
    @JsonProperty("filterId")
    private String filterId;
    
}
