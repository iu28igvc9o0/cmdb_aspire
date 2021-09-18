package com.aspire.mirror.inspection.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/** 
* @author ZhangSheng 
* @version 2018年8月2日 下午6:58:35 
* @describe 巡检任务报告查询对象
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportPageRequest implements Serializable{
	
	private static final long serialVersionUID = 1681929628273541087L;
	
	/** 报告ID */
	@JsonProperty("report_id")
    private String reportId;

    /** 任务ID */
	@JsonProperty("task_id")
    private String taskId;
	
	/** 巡检任务名称*/
	private String name;
	
	/** RUNNING-运行中FINNISHED运行完成 */
    private String status;
	
	/** 巡检任务开始时间*/
	@JsonProperty("inspection_time_start")
	private String inspectionTimeStart;
	
	/** 巡检任务结束时间*/
	@JsonProperty("inspection_time_end")
	private String inspectionTimeEnd;
	
	/** 页面大小*/
	@JsonProperty("page_size")
	private int pageSize;
	
	/** 第几页*/
	@JsonProperty("page_no")
	private int pageNo;
	
	

}
