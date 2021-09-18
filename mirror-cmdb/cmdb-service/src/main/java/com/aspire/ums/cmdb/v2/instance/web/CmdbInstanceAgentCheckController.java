package com.aspire.ums.cmdb.v2.instance.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.ICmdbInstanceAgentCheckAPI;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheck;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheckQuery;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceAgentCheckService;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName CmdbInstanceAgentCheckController
 * @Description
 * @Author luowenbo
 * @Date 2020/5/26 16:06
 * @Version 1.0
 */
@RestController
@Slf4j
public class CmdbInstanceAgentCheckController implements ICmdbInstanceAgentCheckAPI {
    @Autowired
    private ICmdbInstanceAgentCheckService agentCheckService;

    @Override
    public Result<CmdbInstanceAgentCheck> list(@RequestBody CmdbInstanceAgentCheckQuery query) {
        log.info("Request into CmdbInstanceAgentCheckController.list()  params -> {}", query);
        return agentCheckService.list(query);
    }

    @Override
    public JSONObject batchDelete(@RequestBody CmdbInstanceAgentCheckQuery query) {
        log.info("Request into CmdbInstanceAgentCheckController.batchDelete()  params -> {}", query);
        JSONObject rs = new JSONObject();
        try {
            agentCheckService.batchDelete(query);
            rs.put("flag",true);
            rs.put("msg","success");
        } catch (Exception e){
            rs.put("flag",false);
            rs.put("msg",e.getMessage());
        }
        return rs;
    }
}
