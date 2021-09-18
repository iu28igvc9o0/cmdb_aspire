package com.aspire.mirror.inspection.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto
 * 类名称:    ReportItemPageRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/12/26 16:21
 * 版本:      v1.0
 */
@Data
public class ReportItemPageRequest {

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

    @JsonProperty("object_id")
    private String objectId;

    @JsonProperty("item_id")
    private String itemId;

    private List<String> logs;

    @JsonProperty("item_group")
    private String itemGroup;
}
