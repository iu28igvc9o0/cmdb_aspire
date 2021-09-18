package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 今日任务统计
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    TodayOpsTaskStatistic.java
 * 类描述:    今日任务统计
 * 创建人:    JinSu
 * 创建时间:  2020/4/3 18:32
 * 版本:      v1.0
 */
@Data
public class TodayOpsTaskStatistic {
    @JsonProperty("auto_ops_task_num")
    private Integer autoOpsTaskNum;
    @JsonProperty("handle_ops_task_num")
    private Integer handleOpsTaskNum;
    @JsonProperty("inspection_task_num")
    private Integer inspectionTaskNum;
    @JsonProperty("auto_repair_task_num")
    private Integer autoRepairTaskNum;
    @JsonProperty("audit_task_num")
    private Integer auditTaskNum;
    @JsonProperty("failed_task_num")
    private Integer failedTaskNum;

}
