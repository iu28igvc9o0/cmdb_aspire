package com.migu.tsg.microservice.atomicservice.composite.clientservice.template;

import com.aspire.mirror.composite.service.template.payload.CompMonitorApiServerConfig;
import com.aspire.mirror.composite.service.template.payload.CompMonitorDetailResponse;
import com.aspire.mirror.composite.service.template.payload.CompTemplateItemListRequest;
import com.aspire.mirror.composite.service.template.payload.ZabbixTemplateDetailResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.template
 * 类名称:    ItemDatasCollectServiceClient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/6 13:39
 * 版本:      v1.0
 */
@FeignClient(value = "COLLECT-SERVICE")
public interface ItemDatasCollectServiceClient{

    /**
     * 检索ZABBIX下的监控项列表
     * @param paramObj
     */
    @GetMapping(value = "/v1/itemDatas/template/queryItemList")
    @ApiOperation(value = "查询模版下的监控项列表", notes = "查询模版下的监控项列表", tags = {"/v1/itemDatas"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    List<CompMonitorDetailResponse> queryZbxTemplateItemList(@RequestBody CompTemplateItemListRequest paramObj);

    /**
     * 检索ZABBIX下的监控项列表
     * @param zbxApiSvrInfo
     */
    @PostMapping(value = "/v1/itemDatas/template/loadTemplateList", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询模版列表", response = ZabbixTemplateDetailResponse.class,
            notes = "查询模版列表", tags = {"/v1/itemDatas"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    List<ZabbixTemplateDetailResponse> loadZbxTemplateList(@RequestBody CompMonitorApiServerConfig zbxApiSvrInfo);
}
