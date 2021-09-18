package com.aspire.mirror.inspection.api.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 
* @author ZhangSheng 
* @version 2018年8月21日 下午3:32:25 
* @describe 
*/
@Data
@ToString
@NoArgsConstructor
public class ReportTaskDetailResponse implements Serializable{
	
	private static final long serialVersionUID = 3583043174748017588L;

	/** 报告ID */
	@JsonProperty("report_id")
    private String reportId ;

    /** 任务ID */
	@JsonProperty("task_id")
    private String taskId ;
	
	/** 任务ID */
	@JsonProperty("task_name")
    private String taskName;
	
	/** 任务类型*/
	@JsonProperty("task_type")
    private String taskType;

    /** 报告名称 */
    private String name;
    
    /** RUNNING-运行中 FINNISHED运行完成 */
    private String status;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("create_time")
    private java.util.Date createTime;
    
    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("finish_time")
    private java.util.Date finishTime;

    @JsonProperty("report_file_path")
    private String reportFilePath;

    private String result;
}
