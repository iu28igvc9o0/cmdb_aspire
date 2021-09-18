package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.report;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceTable;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IProvinceReportOverviewClient
 * Author:   hangfang
 * Date:     2020/5/8
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface IProvinceReportOverviewClient {

    @GetMapping("/cmdb/province/report/getReportOverview")
    Map<String, Object> getReportOverview(@RequestParam("tableId") String tableId,
                                          @RequestParam("month") String month,
                                          @RequestParam("type") String type);

    @GetMapping("/cmdb/province/report/table/listByOwnerAndPage")
    List<Cmdb31ProvinceTable> listByOwnerAndPage(@RequestParam(value = "resourceOwner", required = false) String resourceOwner,
                                                 @RequestParam("type") String type);
}
