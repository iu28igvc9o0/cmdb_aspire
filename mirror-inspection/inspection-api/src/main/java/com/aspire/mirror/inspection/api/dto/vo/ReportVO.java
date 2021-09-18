package com.aspire.mirror.inspection.api.dto.vo ;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_report视图层对象
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.api.dto.vo
 * 类名称:   ReportVO.java
 * 类描述:   inspection_report视图层属性，属性范围>=表结构属性.
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class ReportVO implements Serializable {
	
	private static final long serialVersionUID = -5400183000157995402L;

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

    /** RUNNING-运行中FINNISHED运行完成 */
    private String status;

    /** 结束时间 */
    @JsonProperty("finish_time")
    private java.util.Date finishTime;

} 
