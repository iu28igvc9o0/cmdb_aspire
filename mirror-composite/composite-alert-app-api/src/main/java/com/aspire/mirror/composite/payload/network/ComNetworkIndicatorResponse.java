package com.aspire.mirror.composite.payload.network;

import lombok.Data;

import java.util.List;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/12 14:08
 */
@Data
public class ComNetworkIndicatorResponse {

    /**
     * 资源池
     */
    private String idcType;

    /**
     * 机房
     */
    private String roomId;

    /**
     * 配置条件
     */
    private List<QueryField> params;

    /**
     * 用户名称
     */
    private String namespace;

    /**
     *  开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 排序字段名称
     */
    private String sortName;

    /**
     * 展示数量
     */
    private Integer pageSize;
}
