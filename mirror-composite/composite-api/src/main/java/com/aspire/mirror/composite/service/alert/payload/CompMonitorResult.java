package com.aspire.mirror.composite.service.alert.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.alert.payload
 * 类名称:    CompMonitorResult.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/24 19:23
 * 版本:      v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompMonitorResult {
    /**
     * 监控结果
     */
    private Double value;

}
