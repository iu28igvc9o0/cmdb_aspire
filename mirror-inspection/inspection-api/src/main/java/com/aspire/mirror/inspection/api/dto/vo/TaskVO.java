package com.aspire.mirror.inspection.api.dto.vo ;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_task视图层对象
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.api.dto.vo
 * 类名称:   TaskVO.java
 * 类描述:   inspection_task视图层属性，属性范围>=表结构属性.
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class TaskVO implements Serializable {
	
	private static final long serialVersionUID = -7506623620590055989L;

    /** 任务ID */
	@JsonProperty("task_id")
    private String taskId;

    /** 任务名 */
    private String name;

    /** 模版ID */
    @JsonProperty("template_id")
    private String templateId;

    /** 任务类型  1-手动   2-自动 */
    private String type;

    /** 时间周期，只有任务类型为自动，才能选择 MIN-分钟 MON-月 WEEK-周 DAY-日  DEFINE-自定义 */
    private String cycle;

    /** 执行时间，需根据周期类型来动态展示。具体参考已有的调度。如任务的执行时间功能。手动任务，是日期选择框 */
    @JsonProperty("exec_time")
    private java.util.Date execTime;

    /** 状态：ON-启动   OFF-禁用 */
    private String status;

    /** 创建时间 */
    @JsonProperty("create_time")
    private java.util.Date createTime;

    /** 更新时间 */
    @JsonProperty("update_time")
    private java.util.Date updateTime;

} 
