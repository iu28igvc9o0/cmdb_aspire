package com.aspire.mirror.inspection.api.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 
* @author ZhangSheng 
* @version 2018年8月31日 下午3:46:17 
* @describe 任务运行界面查询请求参数封装类
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskRunRequest implements Serializable{
	private static final long serialVersionUID = 8478671313842902417L;
    
    /** 任务ID*/
    @JsonProperty("task_id")
    private String taskId;
    
	/** 任务名称*/
	private String name;
	
	/** 任务类型*/
	private String type;
	
	/** 任务执行运行开始时间*/
	@JsonProperty("exec_time_start")
	private String execTimeStart;
	
	/** 任务执行运行结束时间*/
	@JsonProperty("exec_time_end")
	private String execTimeEnd;
	
	/** 页面大小*/
	@JsonProperty("page_size")
	private Integer pageSize;
	
	/** 第几页*/
	@JsonProperty("page_no")
	private Integer pageNo;
}
