package com.aspire.mirror.alert.api.service.mailAlert;

import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailActionResp;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailFilterQueryReq;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailFilterRequest;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailFilterResponse;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * 邮件告警录入
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.service
 * 类名称:    AlertsService.java
 * 类描述:    告警服务
 * 创建人:    LiangJun
 * 创建时间:  2019/5/22 21:57
 * 版本:      v1.0
 */
public interface AlertMailFilterService {

    @PostMapping(value = "/v1/alerts/mail/strategy", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "创建收件人", tags = {"Alerts Mail API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertMailActionResp.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    AlertMailActionResp saveFilterStrategy(@RequestBody AlertMailFilterRequest filterRequest);

    @PostMapping(value = "/v1/alerts/mail/strategy-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "创建收件人", tags = {"Alerts Mail API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertMailActionResp.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<AlertMailFilterResponse> selectFilterStrategies(@RequestBody AlertMailFilterQueryReq queryReq) throws ParseException;

    @GetMapping(value = "/v1/alerts/mail/strategy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询告警策略", notes = "查询告警策略", tags = {"Alerts Mail API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertMailFilterResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertMailFilterResponse.class)})
    AlertMailFilterResponse selectAlertMailFilterResponseById(@PathVariable("id") String id);

    @PutMapping(value = "/v1/alerts/mail/strategy", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "创建收件人", tags = {"Alerts Mail API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertMailActionResp.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    AlertMailActionResp modifyAlertMailFilterStrategy(@RequestBody AlertMailFilterRequest filterRequest);

    @DeleteMapping(value = "/v1/alerts/mail/strategy/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "获取指定收件人", tags = {"Alerts Mail API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertMailActionResp.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertMailActionResp.class)})
    AlertMailActionResp removeAlertMailFilterStrategy(@PathVariable("ids") String ids);
}
