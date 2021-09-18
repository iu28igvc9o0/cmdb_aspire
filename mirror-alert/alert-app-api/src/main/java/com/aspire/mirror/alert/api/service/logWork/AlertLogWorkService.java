package com.aspire.mirror.alert.api.service.logWork;

import com.aspire.mirror.alert.api.dto.logWork.AlertWorkConfig;
import com.aspire.mirror.alert.api.dto.logWork.AlertWorkConfigDetail;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface AlertLogWorkService {

    /**
     * 值班时间配置
     */
    @PostMapping(value = "/alerts/workLog/createdAlerts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建值班时间配置", notes = "创建值班时间配置", tags = {"Alerts Work Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String createdAlerts(@RequestBody AlertWorkConfig request);

    /**
     * 获取值班时间配置
     */
    @GetMapping(value = "/alerts/workLog/getAlertWorkConfig",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取值班时间配置", notes = "获取值班时间配置", tags = {"Alerts Work Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertWorkConfigDetail.class),
            @ApiResponse(code = 500, message = "内部错误")})
    AlertWorkConfigDetail getAlertWorkConfig();

    /**
     * 获取值班日志详情
     */
    @GetMapping(value = "/alerts/getWorkLogInfo",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取值班日志详情", notes = "获取值班日志详情", tags = {"Alerts Work Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getWorkLogInfo(@RequestParam(value = "workName", required = false) String workName,
                          @RequestParam(value = "workDate", required = false) String workDate,
                          @RequestParam(value = "workTime", required = false) String workTime,
                          @RequestParam(value = "work", required = false) String work);

    /**
     * 获取值班日志列表
     */
    @GetMapping(value = "/alerts/getWorkLogList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取值班日志列表", notes = "获取值班日志列表", tags = {"Alerts Work Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getWorkLogList(@RequestParam(value = "workName", required = false) String workName,
                          @RequestParam(value = "workMonth", required = false) String workMonth);

}
