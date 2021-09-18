package com.aspire.mirror.inspection.api.dto.model ;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_task_receiver持久对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.api.dto.model
 * 类名称:    TaskReceiverDTO.java
 * 类描述:    inspection_task_receiver业务类，定义与表字段对应的属性
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TaskReceiverDTO implements Serializable {
	
	private static final long serialVersionUID = -5640964403545454600L;

    /** 序列号 */
    @ApiModelProperty(value = "序列号")
    private String receiverId;

    /** 任务ID */
    @ApiModelProperty(value = "任务ID")
    private String taskId;

    /** 用户ID */
    @ApiModelProperty(value = "用户ID")
    private String userId;

} 
