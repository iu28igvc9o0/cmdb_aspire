package com.aspire.mirror.composite.service.bpm;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.bpm.CompAlertAutoOrderConfigDetail;
import com.aspire.mirror.composite.payload.bpm.CompAlertAutoOrderConfigReq;
import com.aspire.mirror.composite.payload.bpm.CompAlertAutoOrderLog;
import com.aspire.mirror.composite.payload.bpm.CompAlertAutoOrderLogReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping(value = "/${version}/alerts/autoOrderConfig")
public interface ICompAlertAutoOrderConfigService {

    /**
     * 获取告警自动派单配置列表
     */
    @GetMapping(value = "/getAlertAutoOrderConfigList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警自动派单配置列表", notes = "获取告警自动派单配置列表", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = CompAlertAutoOrderConfigDetail.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<CompAlertAutoOrderConfigDetail> getAlertAutoOrderConfigList(@RequestParam(value = "configName", required = false) String configName,
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
    String createAlertAutoOrderConfig(@RequestBody CompAlertAutoOrderConfigReq request);

    /**
     * 校验配置名称
     */
    @GetMapping(value = "/checkName", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "校验配置名称", notes = "校验配置名称", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String checkName(@RequestParam(value = "configName") String configName);

    /**
     * 修改告警自动派单配置
     */
    @PutMapping(value = "/updateAlertAutoOrderConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改告警自动派单配置", notes = "修改告警自动派单配置", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String updateAlertAutoOrderConfig(@RequestBody CompAlertAutoOrderConfigReq request);

    /**
     * 获取告警自动派单配置详情
     */
    @GetMapping(value = "/getAlertAutoOrderConfigDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警自动派单配置详情", notes = "获取告警自动派单配置详情", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = CompAlertAutoOrderConfigDetail.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    CompAlertAutoOrderConfigDetail getAlertAutoOrderConfigDetail(@RequestParam("uuid") String uuid);

    /**
     * 删除告警自动派单配置详情
     */
    @DeleteMapping(value = "/deleteAlertAutoOrderConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除告警自动派单配置详情", notes = "删除告警自动派单配置详情", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String deleteAlertAutoOrderConfig(@RequestBody List<String> uuidList);

    /**
     * 更改告警自动派单配置状态
     */
    @PutMapping(value = "/updateAlertAutoOrderConfigStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "更改告警自动派单配置详情状态", notes = "更改告警自动派单配置详情状态", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String updateAlertAutoOrderConfigStatus(@RequestBody List<String> uuidList,
                                          @RequestParam("configStatus") String configStatus);

    /**
     * 拷贝告警自动派单配置
     */
    @PostMapping(value = "/copyAlertAutoOrderConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "拷贝告警自动派单配置", notes = "拷贝告警自动派单配置", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String copyAlertAutoOrderConfig(@RequestParam("uuid") String uuid);

    /**
     * 获取告警自动派单配置日志列表
     */
    @PostMapping(value = "/getAlertAutoOrderLogList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警自动派单配置日志列表", notes = "获取告警自动派单配置日志列表", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = CompAlertAutoOrderLog.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<CompAlertAutoOrderLog> getAlertAutoOrderLogList(@RequestBody CompAlertAutoOrderLogReq request);

    /**
     * 导出告警自动派单配置日志
     */
    @PostMapping(value = "/exportAlertAutoOrderLogList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出告警自动派单配置日志", notes = "导出告警自动派单配置日志", tags = {"Alerts Auto Order Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void exportAlertAutoOrderLogList(@RequestBody CompAlertAutoOrderLogReq request,HttpServletResponse response);


}
