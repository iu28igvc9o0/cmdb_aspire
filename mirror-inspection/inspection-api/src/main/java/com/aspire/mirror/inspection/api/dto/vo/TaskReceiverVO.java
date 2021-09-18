package com.aspire.mirror.inspection.api.dto.vo ;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_task_receiver视图层对象
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.api.dto.vo
 * 类名称:   TaskReceiverVO.java
 * 类描述:   inspection_task_receiver视图层属性，属性范围>=表结构属性.
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class TaskReceiverVO implements Serializable {
	
	private static final long serialVersionUID = -7726159557056898568L;

    /** 序列号 */
	@JsonProperty("receiver_id")
    private String receiverId;

    /** 任务ID */
	@JsonProperty("task_id")
    private String taskId;

    /** 用户ID */
    @JsonProperty("user_id")
    private String userId;

} 
