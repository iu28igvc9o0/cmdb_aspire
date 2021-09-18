package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 当日任务执行状态
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.ops.api.domain
 * 类名称:    TodayOpsTaskStatusStatistic.java
 * 类描述:    当日任务执行状态
 * 创建人:    JinSu
 * 创建时间:  2020/4/3 18:42
 * 版本:      v1.0
 */
@Data
public class TodayOpsTaskStatusStatistic {
    @JsonProperty("task_success_rate")
    private Double taskSuccessRate;
    @JsonProperty("device_num")
    private Integer deviceNum;
    @JsonProperty("task_average_time")
    private Double taskAverageTime;
    @JsonProperty("task_max_time")
    private Double taskMaxTime;
}
