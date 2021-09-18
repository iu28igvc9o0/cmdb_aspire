package com.aspire.mirror.composite.service.mailAlert;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.mailAlert.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

public interface ICompAlertMailFilterService {
    @PostMapping(value = "/v1/alerts/mail/strategy", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警过滤规则", notes = "创建收件人", tags = {"Alerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertMailActionResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    CompAlertMailActionResponse create(@RequestBody CompAlertMailFilterRequest filterRequest);

    @PutMapping(value = "/v1/alerts/mail/strategy", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "创建收件人", tags = {"Alerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertMailActionResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    CompAlertMailActionResponse modify(@RequestBody CompAlertMailFilterRequest filterRequest);

    @PostMapping(value = "/v1/alerts/mail/strategy-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "创建收件人", tags = {"Alerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<CompAlertMailFilterResponse> selectStrategies(@RequestBody CompAlertMailFilterQueryReq mailFilterQueryReq) throws ParseException;

    @GetMapping(value = "/v1/alerts/mail/strategy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "获取指定收件人", tags = {"Alerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertMailFilterResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertMailFilterResponse.class)})
    CompAlertMailFilterResponse selectById(@PathVariable("id") String id);

    @DeleteMapping(value = "/v1/alerts/mail/strategy/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "获取指定收件人", tags = {"Alerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertMailActionResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertMailActionResponse.class)})
    CompAlertMailActionResponse remove(@PathVariable("ids") String ids);

    @PostMapping(value = "/v1/alerts/mail/resolve-records", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询告警解析记录", notes = "查询告警解析记录", tags = {"Alerts API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询告警解析记录", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertMailResolveResponse> queryFilterRecords(@RequestBody CompAlertMailResolveRequest alertMailResolveRequest) throws ParseException;
}
