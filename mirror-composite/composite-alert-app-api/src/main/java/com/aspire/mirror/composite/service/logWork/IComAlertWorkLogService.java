package com.aspire.mirror.composite.service.logWork;

import com.aspire.mirror.composite.payload.logWork.ComAlertWorkConfigData;
import com.aspire.mirror.composite.payload.logWork.CompAlertWorkConfigDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(value = "值班信息配置")
@RequestMapping("/${version}/alerts/workLog")
public interface IComAlertWorkLogService {

    /**
     * 创建值班时间配置
     */
    @PostMapping(value = "/createdAlertWorkConfig",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建值班时间配置", notes = "创建值班时间配置", tags = {"Alerts Work Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String createdAlertWorkConfig(@RequestBody ComAlertWorkConfigData comAlertWorkConfigData);

    /**
     * 获取值班时间配置
     */
    @GetMapping(value = "/getAlertWorkConfig",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取值班时间配置", notes = "获取值班时间配置", tags = {"Alerts Work Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompAlertWorkConfigDetail.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompAlertWorkConfigDetail getAlertWorkConfig();

    /**
     * 获取值班日志详情
     */
    @GetMapping(value = "/getWorkLogInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取值班日志详情", notes = "获取值班日志详情", tags = {"Alerts Work Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getWorkLogInfo(@RequestParam(value = "workName") String workName,
                          @RequestParam(value = "workDate") String workDate,
                          @RequestParam(value = "workTime") String workTime,
                          @RequestParam(value = "work") String work);

    /**
     * 获取值班日志列表
     */
    @GetMapping(value = "/getWorkLogList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取值班日志列表", notes = "获取值班日志列表", tags = {"Alerts Work Log API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = Object.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object getWorkLogList(@RequestParam(value = "workName", required = false) String workName,
                          @RequestParam(value = "workMonth", required = false) String workMonth);

}
