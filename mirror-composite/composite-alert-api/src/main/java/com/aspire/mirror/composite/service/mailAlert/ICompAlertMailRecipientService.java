package com.aspire.mirror.composite.service.mailAlert;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.mailAlert.CompAlertMailActionResponse;
import com.aspire.mirror.composite.payload.mailAlert.CompAlertMailReceiverRequest;
import com.aspire.mirror.composite.payload.mailAlert.CompAlertMailReceiverResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 邮件告警录入
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.service
 * 类名称:    AlertsService.java
 * 类描述:    告警服务
 * 创建人:    LiangJun
 * 创建时间:  2019/5/27 11:05
 * 版本:      v1.0
 */
public interface ICompAlertMailRecipientService {
    @PostMapping(value = "/v1/alerts/mail/recipient", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "创建收件人", tags = {"Alerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertMailActionResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    CompAlertMailActionResponse create(@RequestBody CompAlertMailReceiverRequest mailReceiverRequest);

    @PutMapping(value = "/v1/alerts/mail/recipient", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "更新告警", notes = "更新收件人", tags = {"Alerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertMailActionResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    CompAlertMailActionResponse modify(@RequestBody CompAlertMailReceiverRequest mailReceiverRequest);

    @DeleteMapping(value = "/v1/alerts/mail/recipient/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除告警", notes = "删除收件人", tags = {"Alerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertMailActionResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertMailActionResponse.class)})
    CompAlertMailActionResponse delete(@PathVariable(value = "ids") String ids);

    @GetMapping(value = "/v1/alerts/mail/recipient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询告警", notes = "获取指定收件人", tags = {"Alerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertMailReceiverResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertMailReceiverResponse.class)})
    CompAlertMailReceiverResponse get(@PathVariable(value = "id") String id);

    @PostMapping(value = "/v1/alerts/mail/recipients", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "创建告警", tags = {"Alerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询收件人列表", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertMailReceiverResponse> select(@RequestBody CompAlertMailReceiverRequest mailReceiverRequest);
}
