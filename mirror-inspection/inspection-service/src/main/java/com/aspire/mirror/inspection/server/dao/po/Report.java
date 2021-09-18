package com.aspire.mirror.inspection.server.dao.po ;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_report持久对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao.po
 * 类名称:    Report.java
 * 类描述:    inspection_report持久类，定义与表字段对应的属性
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report implements Serializable {
	
	private static final long	serialVersionUID	= -8757357510964447404L;

	public static final String	STATUS_RUNNING		= "RUNNING";
	public static final String	STATUS_FINISHED		= "FINNISHED";

    public static final String	BIZ_STATUS_RUNNING		= "2";
    public static final String	BIZ_STATUS_SUCCESS		= "0";
    public static final String	BIZ_STATUS_FAIL	= "1";

    /** 报告ID */
    private String reportId;

    /** 任务ID */
    private String taskId;

    /** 报告名称 */
    private String name;

    /** 创建时间 */
    private java.util.Date createTime;

    /** RUNNING-运行中
		FINNISHED运行完成 */
    private String status;

    /** 结束时间 */
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

    @ApiModelProperty(value = "报表结果")
    private String result;

    public Float calcTotalSeconds() {
        if (finishTime == null || createTime == null) {
            return null;
        }
        return (finishTime.getTime() - createTime.getTime()) / 1000f;
    }
    private String creater;
} 
