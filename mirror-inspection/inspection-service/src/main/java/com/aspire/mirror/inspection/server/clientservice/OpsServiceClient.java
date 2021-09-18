package com.aspire.mirror.inspection.server.clientservice;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.mirror.inspection.api.dto.model.OpsApItemType;
import com.aspire.mirror.inspection.server.clientservice.payload.SimpleAgentHostInfo;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.server.clientservice.feign
 * 类名称:    OpsServiceClient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/20 14:08
 * 版本:      v1.0
 */
@FeignClient(value = "ops-service", url = "http://10.1.203.100:30303")
public interface OpsServiceClient {
    @PostMapping(value="/v1/ops-service/opsAutoRepair/syncApItemList", produces = MediaType.APPLICATION_JSON_VALUE)
    public void syncApItemList(@RequestBody List<OpsApItemType> apItemSyncList);

    @GetMapping(value = "/v1/ops-service/opsManage/queryAgentInfoByProxyIdConcatIP/{proxyIdConcatIP}", produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleAgentHostInfo queryAgentInfoByProxyIdConcatIP(@PathVariable("proxyIdConcatIP") String proxyIdConcatIP);
}
