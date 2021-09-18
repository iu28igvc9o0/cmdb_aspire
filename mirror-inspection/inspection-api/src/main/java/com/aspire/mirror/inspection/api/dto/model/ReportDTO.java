package com.aspire.mirror.inspection.api.dto.model ;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_report持久对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.api.dto.model
 * 类名称:    ReportDTO.java
 * 类描述:    inspection_report业务类，定义与表字段对应的属性
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReportDTO implements Serializable {
	
	private static final long serialVersionUID = -8571234437587028792L;

    /** 报告ID */
    @ApiModelProperty(value = "报告ID")
    private String reportId;

    /** 任务ID */
    @ApiModelProperty(value = "任务ID")
    private String taskId;

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

    /**
     * 业务状态  成功0，失败1，运行中2
     */
    @ApiModelProperty(value = "业务状态")
    private String bizStatus;

    /**
     * 总耗时（s）
     */
    @ApiModelProperty(value = "总耗时")
    private Float totalTime;

    @ApiModelProperty(value = "报告结果")
    private String result;

    public Float calcTotalSeconds() {
        if (finishTime == null || createTime == null) {
            return null;
        }
        return (finishTime.getTime() - createTime.getTime()) / 1000f;
    }

    private String creater;

}
