package com.aspire.mirror.inspection.api.dto ;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_report新增对象类
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.api.dto
 * 类名称:   ReportCreateRequest.java
 * 类描述:   inspection_report创建请求对象
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportCreateRequest implements Serializable {
	
	private static final long serialVersionUID = -8513521579430618517L;

    /** 报告ID */
	@JsonProperty("report_id")
    private String reportId;

    /** 任务ID */
	@JsonProperty("task_id")
    private String taskId;

    /** 报告名称 */
    private String name;

    /** 创建时间 */
    @JsonProperty("create_time")
    private java.util.Date createTime;

    /** RUNNING-运行中  FINNISHED运行完成 */
    private String status;

    /** 结束时间 */
    @JsonProperty("finish_time")
    private java.util.Date finishTime;

} 
