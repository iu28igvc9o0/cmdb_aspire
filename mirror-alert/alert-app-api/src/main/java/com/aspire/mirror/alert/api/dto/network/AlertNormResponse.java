package com.aspire.mirror.alert.api.dto.network;

import lombok.Data;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/11 20:44
 */
@Data
public class AlertNormResponse {
    private Integer id;

    private String userName;

    /**
     * 指标名称
     */
    private String indicatorName;

    /**
     * 指标key
     */
    private String IndicatorKey;

    private Integer isSort;

    private Integer isDelete;
}
