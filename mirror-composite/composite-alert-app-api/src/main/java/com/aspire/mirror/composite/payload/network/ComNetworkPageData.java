package com.aspire.mirror.composite.payload.network;

import lombok.Data;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/16 11:35
 */
@Data
public class ComNetworkPageData {

    private String idcType;

    private String roomId;

    private String item;

    private String host;

    private String port;

    private String resourceId;

    private String startTime;

    private String endTime;

    private Integer pageSize;

    private Integer pageNum;

}
