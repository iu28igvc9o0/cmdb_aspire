package com.aspire.mirror.collect.api ;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.mirror.collect.api.payload.ObjectItemValueWrap;
import com.aspire.mirror.collect.api.payload.FetchObjectItemsValueRequest;
import com.aspire.mirror.collect.api.payload.MonitorApiServerConfig;
import com.aspire.mirror.collect.api.payload.MonitorItemDetailResponse;
import com.aspire.mirror.collect.api.payload.TemplateItemListRequest;
import com.aspire.mirror.collect.api.payload.ZabbixTemplateDetailResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
* 监控项相关数据收集服务接口    <br/>
* Project Name:collect-api
* File Name:ItemDatasCollectService.java
* Package Name:com.aspire.mirror.collect.api
* ClassName: ItemDatasCollectService <br/>
* date: 2018年9月4日 上午9:58:42 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Api(value = "itemDatas_collect_service")
@RequestMapping(path = "/v1/itemDatas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ItemDatasCollectService {
	
    /**
     * 加载zabbix模板列表
     * @param taskId 
     */
    @PostMapping(value = "/template/loadTemplateList", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "加载zabbix模板列表", response = MonitorItemDetailResponse.class, 
    notes = "加载zabbix模板列表", tags = {"/v1/itemDatas"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    List<ZabbixTemplateDetailResponse> loadZbxTemplateList(@RequestBody MonitorApiServerConfig zbxApiSvrInfo);
    
    /**
     * 检索ZABBIX下的监控项列表
     * @param taskId 
     */
    @PostMapping(value = "/template/queryItemList", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询模版下的监控项列表", response = MonitorItemDetailResponse.class, 
    notes = "查询模版下的监控项列表", tags = {"/v1/itemDatas"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    List<MonitorItemDetailResponse> queryZbxTemplateItemList(@RequestBody TemplateItemListRequest paramObj);
    
    /**
     * 获取设备或业务的监控项列表的值
     * @param taskId 
     */
    @PostMapping(value = "/deviceItems/fetchValues", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "获取设备的监控项列表的值", response = ObjectItemValueWrap.class, 
    notes = "获取设备的监控项列表的值", tags = {"/v1/itemDatas"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    List<ObjectItemValueWrap> fetchDeviceItemsValues(@RequestBody FetchObjectItemsValueRequest request);
}
