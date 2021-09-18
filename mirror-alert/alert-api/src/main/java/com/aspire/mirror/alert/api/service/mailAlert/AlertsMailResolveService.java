package com.aspire.mirror.alert.api.service.mailAlert;

import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailActionResp;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailResolveRequest;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailResolveResponse;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;

/**
 * 邮件告警解析记录查询
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.service
 * 类名称:    AlertsService.java
 * 类描述:    告警服务
 * 创建人:    LiangJun
 * 创建时间:  2019/5/28 21:57
 * 版本:      v1.0
 */
public interface AlertsMailResolveService {
    @PostMapping(value = "/v1/alerts/mail/resolve-records", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警", notes = "创建收件人", tags = {"Alerts Mail API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertMailActionResp.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<AlertMailResolveResponse> query(@RequestBody AlertMailResolveRequest resolveRequest) throws ParseException;
}
