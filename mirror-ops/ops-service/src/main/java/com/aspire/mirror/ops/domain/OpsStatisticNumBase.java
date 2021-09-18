package com.aspire.mirror.ops.domain;

import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.domain
 * 类名称:    OpsStatisticNumBase.java
 * 类描述:    成功、失败、总数
 * 创建人:    JinSu
 * 创建时间:  2020/4/9 17:54
 * 版本:      v1.0
 */
@Data
public class OpsStatisticNumBase {
    private int totalNum;
    private int successNum;
    private int failNum;
    private int auditNum;
}
