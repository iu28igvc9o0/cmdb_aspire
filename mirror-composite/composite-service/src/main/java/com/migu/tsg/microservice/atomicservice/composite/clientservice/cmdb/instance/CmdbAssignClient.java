package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbAssign;
import com.aspire.ums.cmdb.instance.payload.CmdbAssignQuery;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbAssignClient
 * Author:   hangfang
 * Date:     2019/11/14
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbAssignClient {

    /**
     * 获取资源分配分析列表
     */
    @RequestMapping(value = "/cmdb/assign/list", method = RequestMethod.POST)
    Result<CmdbAssign> list(@RequestBody CmdbAssignQuery query);

    /**
     * 保存资源分配分析数据
     */
    @RequestMapping(value = "/cmdb/assign/save", method = RequestMethod.POST)
    Map<String, Object> save(@RequestBody CmdbAssign assign);

    /**
     * 删除资源分配分析数据
     */
    @RequestMapping(value = "/cmdb/assign/delete", method = RequestMethod.DELETE)
    Map<String, Object> delete(@RequestParam("id") String id);

}
