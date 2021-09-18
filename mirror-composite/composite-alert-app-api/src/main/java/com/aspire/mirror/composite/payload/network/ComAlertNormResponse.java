package com.aspire.mirror.composite.payload.network;

import lombok.Data;

/**
 * @author hewang
 * @date  2021-03-03
 * @version 1.0
 */

@Data
public class ComAlertNormResponse {

    private Integer id;

    /**
     * 指标名称
     */
    private String indicatorName;

    /**
     * 指标key
     */
    private String IndicatorKey;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 是否排序
     */
    private Integer isSort;

    /**
     * 是否删除
     */
    private Integer isDelete;
}
