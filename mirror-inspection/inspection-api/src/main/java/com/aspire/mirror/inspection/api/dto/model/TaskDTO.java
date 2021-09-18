package com.aspire.mirror.inspection.api.dto.model ;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.aspire.mirror.inspection.api.dto.TaskObjectCreateRequest;
import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_task持久对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.api.dto.model
 * 类名称:    TaskDTO.java
 * 类描述:    inspection_task业务类，定义与表字段对应的属性
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TaskDTO implements Serializable {
	
	private static final long serialVersionUID = -4671460472116244925L;

    /** 任务ID */
    @ApiModelProperty(value = "任务ID")
    private String taskId;
  
    /** 任务名 */
    @ApiModelProperty(value = "任务名")
    private String name;
    
    /** 巡检结果接收人 */
    @ApiModelProperty(value="巡检结果接收人")
    private String receivers;


    /** 任务类型  1-手动   2-自动 */
    @ApiModelProperty(value = "任务类型1-手动 2-自动")
    private String type;

    /** 时间周期，只有任务类型为自动,才能选择 MIN-分钟 MON-月 WEEK-周  DAY-日  DEFINE-自定义 */
    @ApiModelProperty(value = "时间周期，只有任务类型为自动，才能选择MIN-分钟 MON-月 WEEK-周 DAY-日 DEFINE-自定义")
    private String cycle;

    /** 执行时间，需根据周期类型来动态展示。具体参考已有的调度。如任务的执行时间功能。手动任务，是日期选择框 */
    @ApiModelProperty(value = "执行时间，需根据周期类型来动态展示。具体参考已有的调度。如任务的执行时间功能。手动任务，是日期选择框")
    private String execTime;

    /** 状态：ON-启动  OFF-禁用  */
    @ApiModelProperty(value = "状态:ON-启动 OFF-禁用")
    private String status;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    
    /** 最近运行时间*/
    @ApiModelProperty(value="最近运行时间")
    private java.util.Date recentRunTime;

    private List<TaskObjectDTO> objectList;
//    private Integer deviceNum;
    private String creater;

    private String updater;
} 
