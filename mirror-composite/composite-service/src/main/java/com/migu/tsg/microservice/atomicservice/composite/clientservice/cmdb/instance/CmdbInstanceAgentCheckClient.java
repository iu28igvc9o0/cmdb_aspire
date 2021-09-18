package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheck;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheckQuery;

/**
 * @ClassName CmdbInstanceAgentCheckClient
 * @Description TODO
 * @Author luowenbo
 * @Date 2020/5/26 17:51
 * @Version 1.0
 */
@FeignClient(value = "CMDB")
public interface CmdbInstanceAgentCheckClient {

    /**
     * 查询未采集性能数据的Agent设备
     */
    @RequestMapping(value = "/cmdb/agent/list", method = RequestMethod.POST)
    Result<CmdbInstanceAgentCheck> list(@RequestBody CmdbInstanceAgentCheckQuery query);

    /**
     * 批量删除
     */
    @RequestMapping(value = "/cmdb/agent/delete", method = RequestMethod.POST)
    JSONObject batchDelete(@RequestBody CmdbInstanceAgentCheckQuery query);
}
