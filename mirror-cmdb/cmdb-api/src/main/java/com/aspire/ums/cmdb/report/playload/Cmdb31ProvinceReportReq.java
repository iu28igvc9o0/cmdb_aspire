package com.aspire.ums.cmdb.report.playload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: Cmdb31ProvinceReportReq
 * Author:   hangfang
 * Date:     2020/4/16
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cmdb31ProvinceReportReq {
    /**
     * 资源归属
     */
    private String resourceOwner;
    /**
     *
     */
    private String provinceName;
    /**
     *
     */
    private String submitMonth;
    /**
     * 表名
     */
    private String resourceClass;
    /**
     * 表名
     */
    private String operator;

    /**
     * 查询类型（填报界面"update",查询界面"query"）
     */
    private String type;
}
