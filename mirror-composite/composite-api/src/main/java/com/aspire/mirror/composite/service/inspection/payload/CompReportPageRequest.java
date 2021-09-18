package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 报告列表查询对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompReportPageRequest.java
 * 类描述:    报告列表查询对象
 * 创建人:    JinSu
 * 创建时间:  2018/8/12 10:41
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompReportPageRequest implements Serializable {
    private static final long serialVersionUID = -2966939185152089231L;

    /** 报告ID */
    @JsonProperty("report_id")
    private String reportId;

    /** 任务ID */
    @JsonProperty("task_id")
    private String taskId;

    /** 巡检任务名称*/
    private String name;

    /** RUNNING-运行中FINNISHED运行完成 */
    private String status;

    @JsonProperty("exec_status")
    private String execStatus;

    /** 巡检任务开始时间*/
    @JsonProperty("inspection_time_start")
    private String inspectionTimeStart;

    /** 巡检任务结束时间*/
    @JsonProperty("inspection_time_end")
    private String inspectionTimeEnd;

    /** 页面大小*/
    @JsonProperty("page_size")
    private int pageSize;

    /** 第几页*/
    @JsonProperty("page_no")
    private int pageNo;

}
