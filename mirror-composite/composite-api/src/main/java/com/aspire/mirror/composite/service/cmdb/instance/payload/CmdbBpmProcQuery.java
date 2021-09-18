package com.aspire.mirror.composite.service.cmdb.instance.payload;

import lombok.Data;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbBpmProcQuery
 * Author:   hangfang
 * Date:     2019/9/6
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class CmdbBpmProcQuery {

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 工单类型key
     */
    private String procDefKey;

    /**
     * 流程开始时间
     */
    private String startTime;
    /**
     * 流程结束时间
     */
    private String endTime;

    /**
     * 当前页
     */
    private Integer pageNo;

    /**
     * 每页显示数量
     */
    private Integer pageSize;

}
