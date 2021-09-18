package com.aspire.mirror.composite.service.notify;

import com.aspire.mirror.composite.payload.notify.CompAlertVoiceNotifyContentReq;
import com.aspire.mirror.composite.payload.notify.CompAlertVoiceNotifyReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "告警语音通知")
@RequestMapping("/${version}/alerts/voiceNotify")
public interface IComAlertVoiceNotifyService {

    /**
     * 创建告警语音通知配置
     */
    @PostMapping(value = "/createdAlertVoiceNotify",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警语音通知配置", notes = "创建告警语音通知配置", tags = {"Alerts Voice Notify API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String createdAlertVoiceNotify(@RequestBody CompAlertVoiceNotifyReq request);

    /**
     * 获取告警语音通知配置
     */
    @GetMapping(value = "/getAlertVoiceNotify",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警语音通知配置", notes = "获取告警语音通知配置", tags = {"Alerts Voice Notify API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getAlertVoiceNotify();

    /**
     * 获取告警语音通知内容
     */
    @PostMapping(value = "/getAlertVoiceNotifyContent",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警语音通知内容", notes = "获取告警语音通知内容", tags = {"Alerts Voice Notify API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResponseEntity<String> getAlertVoiceNotifyContent(@RequestBody CompAlertVoiceNotifyContentReq request);

}
