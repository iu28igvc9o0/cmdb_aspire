package com.aspire.mirror.inspection.server.dao.po ;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_task_receiver持久对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao.po
 * 类名称:    TaskReceiver.java
 * 类描述:    inspection_task_receiver持久类，定义与表字段对应的属性
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReceiver implements Serializable {
	
	private static final long serialVersionUID = -7724816063137457554L;

    /** 序列号 */
    private String receiverId;

    /** 任务ID */
    private String taskId;

    /** 用户ID */
    private String userId;

} 
