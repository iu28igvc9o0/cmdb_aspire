package com.aspire.mirror.ops.domain;

import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.domain
 * 类名称:    OpsTimeConsumeStatisticBase.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/4/10 13:55
 * 版本:      v1.0
 */
@Data
public class OpsTimeConsumeStatisticBase {
    private double averageTime;
    private double maxTime;
}
