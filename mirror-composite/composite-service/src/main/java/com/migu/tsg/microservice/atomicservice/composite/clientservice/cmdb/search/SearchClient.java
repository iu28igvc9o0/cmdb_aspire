package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.search;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.search.payload.ColumnFilter;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SearchClient
 * Author:   zhu.juwang
 * Date:     2019/5/21 20:07
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface SearchClient {

    /**
     * 获取自定义过滤列
     */
    @RequestMapping(value = "/cmdb/search/getOrInsertColumnFilter/{menuType}/{moduleId}/{loginName}", method = RequestMethod.GET)
    JSONObject getOrInsertColumnFilter(@PathVariable("menuType") String menuType,
                                       @PathVariable("moduleId") String moduleId,
                                       @PathVariable("loginName")String loginName);

    /**
     * 获取自定义过滤列
     */
    @RequestMapping(value = "/cmdb/search/updateColumnFilter", method = RequestMethod.PUT)
    Map<String, Object> updateColumnFilter(@RequestBody ColumnFilter columnFilter);
}
