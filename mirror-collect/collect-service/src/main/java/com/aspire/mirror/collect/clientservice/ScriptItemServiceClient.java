package com.aspire.mirror.collect.clientservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.mirror.collect.api.payload.GeneralResponse;
import com.aspire.mirror.collect.api.payload.MonitorScriptConfig;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.collect.clientservice
 * 类名称:    ScriptItemServiceClient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/11/15 14:29
 */
@FeignClient(value = "ops-service",url = "http://10.1.203.100:30303")
public interface ScriptItemServiceClient {
    @PutMapping(value = "/v1/ops-service/opsManage/executeIndexValueScriptCollect",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    GeneralResponse execSciptItem(
                    @RequestBody MonitorScriptConfig monitorScriptConfig);
}
