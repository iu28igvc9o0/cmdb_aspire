package com.aspire.mirror.inspection.api.dto ;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_task修改对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.api.dto
 * 类名称:    TaskUpdateResponse.java
 * 类描述:    inspection_task修改响应对象
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateResponse implements Serializable {
	private static final long serialVersionUID = -8811342784247411434L;
    /** 任务ID */
	@JsonProperty("task_id")
    private String taskId ;
    /** 任务名 */
    private String name ;
} 
