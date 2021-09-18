package com.aspire.mirror.composite.payload.filter;

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
public class ComAlertFilterOption {
	 @JsonProperty("id")
	    private Integer id;

	    /**告警过滤名称*/
	    @JsonProperty("name")
	    private String name;

	    /** 创建人*/
	    @JsonProperty("type")
	    private String type;
	    
	    /**
	                   修改人
	     */
	    @JsonProperty("code")
	    private String code;

	    /** 业务系统 */
	    @JsonProperty("operate")
	    private String operate;

	    /** 监控指标/内容，关联触发器name */
	    @JsonProperty("source")
	    private String source;

	    /** 监控对象 */
	    @JsonProperty("content")
	    private String content;
		@JsonProperty("transform")
		private String transform;
	    /** 监控对象 */
	    @JsonProperty("method")
	    private String method;
	    
	    @JsonProperty("status")
	    private Integer status;
	    
	    @JsonProperty("jdbc_type")
	    private String jdbcType;
		@JsonProperty("query_type")
		private String queryType;
    
}
