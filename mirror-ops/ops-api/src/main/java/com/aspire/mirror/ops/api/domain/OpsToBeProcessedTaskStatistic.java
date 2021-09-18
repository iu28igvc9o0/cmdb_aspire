package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * 待处理自动化任务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsToBeProcessedTaskStatistic.java
 * 类描述:    待处理自动化任务
 * 创建人:    JinSu
 * 创建时间:  2020/4/10 15:45
 * 版本:      v1.0
 */
@Data
public class OpsToBeProcessedTaskStatistic {
    @JsonProperty("task_type")
    private String taskType;

    @JsonProperty("task_name")
    private String taskName;

    @JsonProperty("task_id")
    private String taskId;

    @JsonProperty("create_time")
    private Date createTime;
}
