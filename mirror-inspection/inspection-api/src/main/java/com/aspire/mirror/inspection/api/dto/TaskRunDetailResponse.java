package com.aspire.mirror.inspection.api.dto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/** 
* @author ZhangSheng 
* @version 2018年8月31日 下午3:58:13 
* @describe 巡检任务运行任务界面 对象封装
*/
@Data
@NoArgsConstructor
@ToString
public class TaskRunDetailResponse implements Serializable{
	
	private static final long serialVersionUID = 3056082982807663378L;

	/** 
	 * 任务ID
	 */
    @JsonProperty("task_id")
    private String taskId;
    
    /**
     * 任务状态
     */
    private String status;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 模板ID
     */
    @JsonProperty("template_ids")
    private List<String> templateIds;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("exec_time_start")
    private Date execTimeStart;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("exec_time_end")
    private Date execTimeEnd;
    /**
     * 巡检结果
     */
    private  String source;
    
    
    
   
	
}
