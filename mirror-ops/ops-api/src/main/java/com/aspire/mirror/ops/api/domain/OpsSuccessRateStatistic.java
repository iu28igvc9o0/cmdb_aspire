package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 自动化任务成功率统计返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsSuccessRateStatistic.java
 * 类描述:    自动化任务成功率统计返回
 * 创建人:    JinSu
 * 创建时间:  2020/4/10 15:06
 * 版本:      v1.0
 */
@Data
public class OpsSuccessRateStatistic {
    @JsonProperty("auto_success_rate")
    private Double autoSuccessRate;

    @JsonProperty("handle_success_rate")
    private Double handleSuccessRate;

    @JsonProperty("self_repair_success_rate")
    private Double selfRepairSuccessRate;

    @JsonProperty("inspection_success_rate")
    private Double inspectionSuccessRate;
}
