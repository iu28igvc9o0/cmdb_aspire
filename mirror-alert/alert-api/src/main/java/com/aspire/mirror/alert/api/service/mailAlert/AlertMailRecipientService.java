package com.aspire.mirror.alert.api.service.mailAlert;

import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailActionResp;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailReceiverRequest;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailReceiverResponse;
import com.aspire.mirror.common.entity.PageResponse;
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
 * 创建时间:  2019/5/22 11:05
 * 版本:      v1.0
 */
public interface AlertMailRecipientService {

    @PostMapping(value = "/v1/alerts/mail/recipient", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "创建收件人", tags = {"Alerts Mail API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    AlertMailActionResp createAccount(@RequestBody AlertMailReceiverRequest mailReceiverRequest);

    @PutMapping(value = "/v1/alerts/mail/recipient", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "修改收件人", tags = {"Alerts Mail API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    AlertMailActionResp modifyAccount(@RequestBody AlertMailReceiverRequest mailReceiverRequest);

    @DeleteMapping(value = "/v1/alerts/mail/recipient/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "删除收件人", tags = {"Alerts Mail API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertMailReceiverResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertMailReceiverResponse.class)})
    AlertMailActionResp deleteAccount(@PathVariable(value = "ids") String ids);

    @GetMapping(value = "/v1/alerts/mail/recipient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "获取指定收件人", tags = {"Alerts Mail API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertMailReceiverResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertMailReceiverResponse.class)})
    AlertMailReceiverResponse getAccount(@PathVariable(value = "id") String id);

    @PostMapping(value = "/v1/alerts/mail/recipients", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "创建告警", tags = {"Alerts Mail API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询收件人列表", response = AlertMailReceiverResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertMailReceiverResponse.class)})
    PageResponse<AlertMailReceiverResponse> selectAccount(@RequestBody AlertMailReceiverRequest mailReceiverRequest);
}
