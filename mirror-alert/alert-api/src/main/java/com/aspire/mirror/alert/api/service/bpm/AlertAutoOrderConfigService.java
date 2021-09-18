package com.aspire.mirror.alert.api.service.bpm;

import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderConfigDetail;
import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderConfigReq;
import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderLog;
import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderLogReq;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(value = "/alerts/autoOrderConfig")
public interface AlertAutoOrderConfigService {

    /**
     * 获取告警自动派单配置列表
     */
    @GetMapping(value = "/getAlertAutoOrderConfigList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警自动派单配置列表", notes = "获取告警自动派单配置列表", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = AlertAutoOrderConfigDetail.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<AlertAutoOrderConfigDetail> getAlertAutoOrderConfigList(@RequestParam(value = "configName", required = false) String configName,
                                                                         @RequestParam(value = "isOpen", required = false) String isOpen,
                                                                         @RequestParam(value = "startTime", required = false) String startTime,
                                                                         @RequestParam(value = "endTime", required = false) String endTime,
                                                                         @RequestParam(value = "orderType", required = false) String orderType,
                                                                         @RequestParam(value = "orderTimeInterval", required = false) String orderTimeInterval,
                                                                         @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 创建告警自动派单配置
     */
    @PostMapping(value = "/createAlertAutoOrderConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警自动派单配置", notes = "创建告警自动派单配置", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void createAlertAutoOrderConfig(@RequestBody AlertAutoOrderConfigReq request);

    /**
     * 校验配置名称
     */
    @GetMapping(value = "/checkName", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "校验配置名称", notes = "校验配置名称", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    Map<String, String> checkName(@RequestParam(value = "configName") String configName);

    /**
     * 修改告警自动派单配置
     */
    @PutMapping(value = "/updateAlertAutoOrderConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改告警自动派单配置", notes = "修改告警自动派单配置", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void updateAlertAutoOrderConfig(@RequestBody AlertAutoOrderConfigReq request);

    /**
     * 获取告警自动派单配置详情
     */
    @GetMapping(value = "/getAlertAutoOrderConfigDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警自动派单配置详情", notes = "获取告警自动派单配置详情", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = AlertAutoOrderConfigDetail.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    AlertAutoOrderConfigDetail getAlertAutoOrderConfigDetail(@RequestParam("uuid") String uuid);

    /**
     * 删除告警自动派单配置详情
     */
    @DeleteMapping(value = "/deleteAlertAutoOrderConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除告警自动派单配置详情", notes = "删除告警自动派单配置详情", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void deleteAlertAutoOrderConfig(@RequestBody List<String> uuidList);

    /**
     * 更改告警自动派单配置状态
     */
    @PutMapping(value = "/updateAlertAutoOrderConfigStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "更改告警自动派单配置详情状态", notes = "更改告警自动派单配置详情状态", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void updateAlertAutoOrderConfigStatus(@RequestBody List<String> uuidList,
                                          @RequestParam("configStatus") String configStatus);

    /**
     * 拷贝告警自动派单配置
     */
    @PostMapping(value = "/copyAlertAutoOrderConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "拷贝告警自动派单配置", notes = "拷贝告警自动派单配置", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void copyAlertAutoOrderConfig(@RequestParam("uuid") String uuid,
                                  @RequestParam("userName") String userName);

    /**
     * 获取告警自动派单配置日志列表
     */
    @PostMapping(value = "/getAlertAutoOrderLogList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警自动派单配置日志列表", notes = "获取告警自动派单配置日志列表", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = AlertAutoOrderLog.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<AlertAutoOrderLog> getAlertAutoOrderLogList(@RequestBody AlertAutoOrderLogReq request);

    /**
     * 导出告警自动派单配置日志
     */
    @PostMapping(value = "/exportAlertAutoOrderLogList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出告警自动派单配置日志", notes = "导出告警自动派单配置日志", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<Map<String, Object>> exportAlertAutoOrderLogList(@RequestBody AlertAutoOrderLogReq request);

    /**
     * 告警自动派单定时任务
     */
    @PostMapping(value = "/alertAutoOrderSchedule", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警自动派单定时任务", notes = "告警自动派单定时任务", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    ResponseEntity<String> alertAutoOrderSchedule();
}
