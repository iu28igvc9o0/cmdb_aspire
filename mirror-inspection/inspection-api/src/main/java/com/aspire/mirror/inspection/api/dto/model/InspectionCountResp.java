package com.aspire.mirror.inspection.api.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 巡检数返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto.model
 * 类名称:    InspectionCountResp.java
 * 类描述:    巡检数返回
 * 创建人:    JinSu
 * 创建时间:  2020/4/3 15:02
 * 版本:      v1.0
 */
@Data
public class InspectionCountResp {
    @JsonProperty("total_num")
    private int totalNum;

    @JsonProperty("running_num")
    private int runningNum;

    @JsonProperty("failed_num")
    private int failedNum;

    @JsonProperty("success_num")
    private int successNum;
}
