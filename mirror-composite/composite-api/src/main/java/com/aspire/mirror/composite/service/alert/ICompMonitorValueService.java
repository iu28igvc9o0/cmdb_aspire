package com.aspire.mirror.composite.service.alert;

import com.aspire.mirror.composite.service.alert.payload.CompAlertValueRequest;
import com.aspire.mirror.composite.service.alert.payload.CompMonitorRequest;
import com.aspire.mirror.composite.service.alert.payload.CompMonitorResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 监控值服务（topo图关联）
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.alert
 * 类名称:    ICompMonitorValueService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/24 19:18
 * 版本:      v1.0
 */
@RequestMapping("/v1/alerts/monitor")
public interface ICompMonitorValueService {
    /**
     * 监控值获取
     */
    @PostMapping(value = "/getMonitorValue",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取监控结果", notes = "获取监控结果", response = CompMonitorResult.class, tags = { "topo" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = CompMonitorResult.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompMonitorResult.class) })
    CompMonitorResult getMonitorValue(@RequestBody @Validated CompMonitorRequest monitorRequest) throws Exception;

    @PostMapping(value = "/getAlertValue",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警值结果", notes = "获取监控结果", response = CompMonitorResult.class, tags = { "topo" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = CompMonitorResult.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompMonitorResult.class) })
    CompMonitorResult getAlertValue(@RequestBody @Validated CompAlertValueRequest monitorRequest) throws Exception;
}
