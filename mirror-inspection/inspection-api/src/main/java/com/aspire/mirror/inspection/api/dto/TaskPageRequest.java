package com.aspire.mirror.inspection.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** 
* @author ZhangSheng
* @version 2018年7月30日 下午4:25:40 
* @describe 巡检任务分页查询查询请求  封装对象
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskPageRequest  implements Serializable {
	
	private static final long serialVersionUID = -2709990555454910041L;
	/**
     * 模板ID
     */
    @JsonProperty("template_id")
    private String templateId;
	/** 任务名称*/
	private String name;
    /** 巡检结果接收人 */
    private String receivers;
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
