package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.collectUnknown;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbCollectUnknownClient
 * Author:   hangfang
 * Date:     2019/10/11
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbCollectUnknownClient {

    /**
     * 根据条件筛选未知采集设备
     */
    @RequestMapping(value = "/cmdb/collectUnknown/list", method = RequestMethod.POST)
    Result<CmdbCollectUnknown> list(@RequestBody CmdbCollectUnknownQuery collectUnknownQuery);

    /**
     * 新增未知采集设备
     */
    @RequestMapping(value = "/cmdb/collectUnknown/save", method = RequestMethod.POST)
    Map<String, Object> insert(@RequestBody CmdbCollectUnknown collectUnknown);

    /**
     * 更新未知采集设备
     */
    @RequestMapping(value = "/cmdb/collectUnknown/update", method = RequestMethod.POST)
    Map<String, Object> update(@RequestBody CmdbCollectUnknown collectUnknown);
}
