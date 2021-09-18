package com.aspire.ums.bills.client;

import com.aspire.ums.bills.common.ListResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: EsClient
 * Author:   hangfang
 * Date:     2021/3/4
 * Description: 描述
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "ELASTICSEARCH-SERVICE")
public interface EsFeignClient {

    @PostMapping("/cmdb/public/insert")
    Map<String, String> insert(@RequestBody List<Map<String, Object>> data,
                               @RequestParam("index") String index,
                               @RequestParam("type") String type);

    @PostMapping("/cmdb/public/listByPage")
    ListResult<Map<String, Object>> list(@RequestBody Map<String, Object> querySetting);
}
