package com.aspire.mirror.inspection.api.dto.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 
* @author ZhangSheng 
* @version 2018年9月1日 上午10:14:57 
* @describe 任务运行页面实体类
*/
@Data
@NoArgsConstructor
@ToString
public class TaskRunDTO implements Serializable{

	private static final long serialVersionUID = -1398306359499627418L;
	
	/** 
	 * 任务ID
	 */
    @JsonProperty("task_id")
    private String taskId;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 任务状态
     */
    private String status;
    /**
     * 模板名称
     */
    @JsonProperty("template_name")
    private String templateName;
    
    /**
     * 开始时间
     */
    @JsonProperty("exec_time_start")
    private Date execTimeStart;
    
    /**
     * 结束时间
     */
    @JsonProperty("exec_time_end")
    private Date execTimeEnd;
    /**
     * 巡检结果
     */
    private  String source;
}
