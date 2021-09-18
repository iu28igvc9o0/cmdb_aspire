package com.aspire.mirror.inspection.server.dao.po ;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_task持久对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao.po
 * 类名称:    Task.java
 * 类描述:    inspection_task持久类，定义与表字段对应的属性
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task implements Serializable {
	
	private static final long serialVersionUID = -8210159014315034025L;

    /** 任务ID */
    private String taskId;

    /** 任务名 */
    private String name;

    /** 任务类型   1-手动    2-自动 */
    private String type;

    /** 时间周期，只有任务类型为自动，才能选择  MIN-分钟   MON-月  WEEK-周  DAY-日   DEFINE-自定义 */
    private String cycle;

    /** 执行时间，需根据周期类型来动态展示。具体参考已有的调度。如任务的执行时间功能。手动任务，是日期选择框 */
    private String execTime;

    /** 状态： ON-启动  OFF-禁用 */
    private String status;

    /** 创建时间 */
    private java.util.Date createTime;

    /** 更新时间 */
    private java.util.Date updateTime;

    /**
     * 最新运行时间
     */
    private java.util.Date recentRunTime;

    private String creater;

    private String updater;
} 
