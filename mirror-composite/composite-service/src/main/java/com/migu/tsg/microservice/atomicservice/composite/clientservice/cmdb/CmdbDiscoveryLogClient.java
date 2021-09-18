package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.payload.CompAutoDiscoveryLogVO;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDiscoveryLogClient
 * Author:   HANGFANG
 * Date:     2019/4/22 19:27
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbDiscoveryLogClient {

    @RequestMapping(value = "/cmdb/discovery/log/list/{moduleId}", method = RequestMethod.POST)
    JSONObject getLogList(@PathVariable("moduleId") String moduleId,
                                  @RequestParam(value = "pageNumber") Integer pageNumber,
                                  @RequestParam(value = "pageSize") Integer pageSize,
                                  @RequestBody(required = false) JSONObject queryData);


    @RequestMapping(value = "/cmdb/discovery/log/shield", method = RequestMethod.POST)
    Map<String, Object> shieldIp(@RequestBody List<CompAutoDiscoveryLogVO> discoveryLogs);

    @RequestMapping(value = "/cmdb/discovery/log/detail/{id}", method = RequestMethod.GET)
    CompAutoDiscoveryLogVO getLogDetailById(@PathVariable("id") String logId);

    @RequestMapping(value = "/cmdb/discovery/log/bind/{instanceId}", method = RequestMethod.POST)
    Map<String, Object> bind(@PathVariable("instanceId") String instanceId,
                                    @RequestBody CompAutoDiscoveryLogVO discoveryLog);

    @RequestMapping(value = "/cmdb/discovery/log/update/{id}", method = RequestMethod.PUT)
    Map<String, Object> update(@PathVariable("id") String id, @RequestParam(value = "status") String status);

    @RequestMapping(value = "/cmdb/discovery/log/listInstance/{moduleId}", method = RequestMethod.GET)
    JSONObject listInstanceByModuleId(@PathVariable(value = "moduleId") String moduleId);
    }
