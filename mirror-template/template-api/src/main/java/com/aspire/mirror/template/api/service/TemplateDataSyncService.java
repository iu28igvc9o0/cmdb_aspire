package com.aspire.mirror.template.api.service;

import com.aspire.mirror.template.api.dto.*;
import com.aspire.mirror.template.api.dto.vo.StandardDynamicModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 模板数据同步
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.api.service
 * 类名称:    TemplateDataSyncService.java
 * 类描述:    模板数据同步
 * 创建人:    JinSu
 * 创建时间:  2018/9/10 17:09
 * 版本:      v1.0
 */
@Api(value = "模板数据同步")
public interface TemplateDataSyncService {

    //    @Headers({H_CONTENT_TYPE, H_ACCEPT})
//    @RequestLine("GET /basicDataSync/templateList/{syncSeq}")
    @GetMapping(value = "/v1/basicDataSync/templateList/{syncSeq}/{proxyIdentity}")
    @ApiOperation(value = "详情", notes = "获取模板同步列表", tags = {"/v1/template_sync"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    TemplateDataSyncResponse<TemplateDetailResponse> syncMonitorTemplateList(@PathVariable("syncSeq") String startSyncSeq, @PathVariable("proxyIdentity") String proxyIdentity);

    @GetMapping(value = "/v1/basicDataSync/triggerDynamicModelList/{syncSeq}/{proxyIdentity}")
    @ApiOperation(value = "详情", notes = "获取触发器模型列表", tags = {"/v1/basicDataSync"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    TemplateDataSyncResponse<StandardDynamicModel> syncMonitorTriggerDynamicModelList(@PathVariable("syncSeq") String startSyncSeq, @PathVariable("proxyIdentity") String proxyIdentity);

    //    @Headers({H_CONTENT_TYPE, H_ACCEPT})
//    @RequestLine("GET /basicDataSync/itemList/{syncSeq}")
    @GetMapping(value = "/v1/basicDataSync/itemList/{syncSeq}/{proxyIdentity}")
    @ApiOperation(value = "详情", notes = "获取监控项同步列表", tags = {"/v1/template_sync"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    TemplateDataSyncResponse<ItemsDetailResponse> syncMonitorItemList(@PathVariable("syncSeq") String startSyncSeq, @PathVariable("proxyIdentity") String proxyIdentity);

    //    @Headers({H_CONTENT_TYPE, H_ACCEPT})
//    @RequestLine("GET /basicDataSync/triggerList/{syncSeq}")
    @GetMapping(value = "/v1/basicDataSync/triggerList/{syncSeq}/{proxyIdentity}")
    @ApiOperation(value = "详情", notes = "获取触发器同步列表", tags = {"/v1/template_sync"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    TemplateDataSyncResponse<TriggersDetailResponse> syncMonitorTriggerList(@PathVariable("syncSeq") String startSyncSeq, @PathVariable("proxyIdentity") String proxyIdentity);

    //    @Headers({H_CONTENT_TYPE, H_ACCEPT})
//    @RequestLine("GET /basicDataSync/actionList/{syncSeq}")
    @GetMapping(value = "/v1/basicDataSync/actionList/{syncSeq}/{proxyIdentity}")
    @ApiOperation(value = "详情", notes = "获取触发器同步列表", tags = {"/v1/template_sync"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    TemplateDataSyncResponse<ActionsDetailResponse> syncMonitorActionList(@PathVariable("syncSeq") String startSyncSeq, @PathVariable("proxyIdentity") String proxyIdentity);

}
