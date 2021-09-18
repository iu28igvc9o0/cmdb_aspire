package com.aspire.mirror.inspection.api.dto.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
* @author ZhangSheng 
* @version 2018年8月21日 下午3:09:47 
* @describe 
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReportTaskDTO implements Serializable{

	private static final long serialVersionUID = 898803104314217192L;

	/** 报告ID */
    @ApiModelProperty(value = "报告ID")
    private String reportId;

    /** 任务ID */
    @ApiModelProperty(value = "任务ID")
    private String taskId;
    /** 任务名称 */
    @ApiModelProperty(value = "任务名称")
    private String taskName;
    /** 任务类型 */
    @ApiModelProperty(value = "任务类型")
    private String taskType;

    /** 报告名称 */
    @ApiModelProperty(value = "报告名称")
    private String name;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    /** RUNNING-运行中   FINNISHED运行完成 */
    @ApiModelProperty(value = "RUNNING-运行中FINNISHED运行完成")
    private String status;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间")
    private java.util.Date finishTime;

    private String reportFilePath;

    private String result;
}
