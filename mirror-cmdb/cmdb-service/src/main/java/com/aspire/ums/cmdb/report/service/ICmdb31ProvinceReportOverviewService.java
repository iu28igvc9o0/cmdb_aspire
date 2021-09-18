package com.aspire.ums.cmdb.report.service;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdb31ProvinceReportOverviewService
 * Author:   hangfang
 * Date:     2020/5/7
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdb31ProvinceReportOverviewService {

    Map<String, Object> getReportOverview(String tableId, String month, String type);
}
