package com.aspire.mirror.inspection.api.dto.model;

import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto.model
 * 类名称:    OpsTimeConsumeStatisticBase.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/4/10 14:45
 * 版本:      v1.0
 */
@Data
public class OpsTimeConsumeStatisticBase {
    private double averageTime;
    private double maxTime;
}
