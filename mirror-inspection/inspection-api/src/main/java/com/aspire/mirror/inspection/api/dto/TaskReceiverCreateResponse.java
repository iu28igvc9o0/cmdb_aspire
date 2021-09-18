package com.aspire.mirror.inspection.api.dto ;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_task_receiver新增对象类
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.api.dto
 * 类名称:   TaskReceiverCreateResponse.java
 * 类描述:   inspection_task_receiver创建响应对象
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReceiverCreateResponse implements Serializable {
	
	private static final long serialVersionUID = -6198895993464780637L;

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
