package com.aspire.mirror.inspection.api.dto ;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_report详情对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.api.dto
 * 类名称:    ReportDetailResponse.java
 * 类描述:    巡检任务创建响应对象
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailResponse implements Serializable {
	
	private static final long serialVersionUID = -8711185528109255228L;

    /** 报告ID */
	@JsonProperty("report_id")
    private String reportId ;

    /** 任务ID */
	@JsonProperty("task_id")
    private String taskId ;

    /** 报告名称 */
    private String name ;
    
    /** RUNNING-运行中 FINNISHED运行完成 */
    private String status ;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("create_time")
    private java.util.Date createTime ;
    
    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("finish_time")
    private java.util.Date finishTime ;

    /** 任务ID */
    @JsonProperty("task_name")
    private String taskName;

    private String creater;
}
