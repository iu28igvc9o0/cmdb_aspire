package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompReportDetailResponse.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/8/12 10:53
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
@ToString
public class CompReportDetailResponse implements Serializable {
    private static final long serialVersionUID = 5047473794823371984L;
    /** 报告ID */
    @JsonProperty("report_id")
    private String reportId ;

    /** 任务ID */
    @JsonProperty("task_id")
    private String taskId ;

    /** 报告名称 */
    private String name ;

    /** RUNNING-运行中 FINNISHED运行完成 */
    private String status ;

    /** 创建时间 */
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    private java.util.Date createTime ;

    /** 结束时间 */
    @JsonProperty("finish_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    private java.util.Date finishTime ;

    /** 任务ID */
    @JsonProperty("task_name")
    private String taskName;

    /** 任务类型*/
    @JsonProperty("task_type")
    private String taskType;

    /**
     * 巡检结果
     */
    private String result;

    /**
     * 模板
     */
    @JsonProperty("template_names")
    private String templateNames;

    @JsonProperty("device_num")
    private Integer deviceNum;

    @JsonProperty("item_num")
    private String itemNum;

    @JsonProperty("report_file_path")
    private String reportFilePath;
}
