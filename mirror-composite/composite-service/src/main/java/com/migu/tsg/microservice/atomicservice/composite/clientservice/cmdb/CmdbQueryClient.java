package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectController
 * Author:   zhu.juwang
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbQueryClient {

    /**
     * 获取自定义查询条件集合
     * @return 自定义查询条件集合
     */
    @RequestMapping(value = "/cmdb/query/condition/", method = RequestMethod.GET)
    JSONArray getQueryList(@RequestParam(value = "moduleId", required = false) String moduleId,
                           @RequestParam(value = "user", required = false) String user,
                           @RequestParam(value = "menuType", required = false) String menuType);


    /**
     * 新增自定义条件
     */
    @RequestMapping(value = "/cmdb/query/condition/update", method = RequestMethod.PUT)
    Map<String, String> insertOrUpdateQuery(@RequestBody JSONObject entity);


    /**
     * 删除自定义条件
     */
    @RequestMapping(value = "/cmdb/query/condition/delete/{queryId}", method = RequestMethod.DELETE)
    Map<String, String> deleteQuery(@PathVariable("queryId") String queryId);
}
