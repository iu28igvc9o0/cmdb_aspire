package com.aspire.mirror.inspection.api.dto ;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_task修改对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.api.dto
 * 类名称:    TaskUpdateRequest.java
 * 类描述:    inspection_task修改请求参数对象
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateRequest implements Serializable {
	
	private static final long serialVersionUID = -8179714988069594075L;

    /** 任务ID */
	//@JsonProperty("task_id")
	//@NotBlank
    //private String taskId ;

    /** 任务名 */
	@NotBlank
    private String name;

    /** 巡检结果接收人 */
    private String receivers;

    /** 任务类型
		1-手动
		2-自动 */
    @NotBlank
    private String type;

    /** 时间周期，只有任务类型为自动，才能选择 MIN-分钟 MON-月 WEEK-周 DAY-日 DEFINE-自定义 */
    private String cycle;

    /** 执行时间，需根据周期类型来动态展示。具体参考已有的调度。如任务的执行时间功能。手动任务，是日期选择框 */
    @JsonProperty("exec_time")
    private String execTime;

    /** 状态：ON-启动  OFF-禁用 */
    @NotBlank
    private String status;
    
    /** 最新运行时间*/
    @JsonProperty("recent_run_time")
    private java.util.Date recentRunTime;

    @JsonProperty("object_list")
    private List<TaskObjectCreateRequest> objectList;

} 
