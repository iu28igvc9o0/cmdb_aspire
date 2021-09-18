package com.aspire.ums.cmdb.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2020/2/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(name="ALERT-SERVICE")
public interface IAlertClient {

    @GetMapping(value = "/v1/alerts/ExternalInterfaceService/getReportMonthAllDdata", produces = MediaType.APPLICATION_JSON_VALUE)
    List<Map<String, String>> getReportMonthAllDdata();

}
