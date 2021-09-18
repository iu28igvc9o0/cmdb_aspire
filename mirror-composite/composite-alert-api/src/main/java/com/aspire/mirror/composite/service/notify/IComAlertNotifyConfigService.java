package com.aspire.mirror.composite.service.notify;

import com.aspire.mirror.composite.payload.notify.ComAlertNotifyConfigReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "告警通知配置")
@RequestMapping("/${version}/alerts/notifyConfig")
public interface IComAlertNotifyConfigService {

    /**
     * 获取告警通知配置列表
     */
    @GetMapping(value = "/getAlertNotifyConfigList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警通知配置列表", notes = "获取告警通知配置列表", tags = {"Alerts Notify Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = Object.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    Object getAlertNotifyConfigList(@RequestParam( value = "name", required = false) String name,
                                    @RequestParam( value = "isOpen", required = false) String isOpen,
                                    @RequestParam( value = "notifyType", required = false) String notifyType,
                                    @RequestParam( value = "alertFilter", required = false) String alertFilter,
                                    @RequestParam( value = "notifyObj", required = false) String notifyObj,
                                    @RequestParam( value = "isRecurrenceInterval", required = false) String isRecurrenceInterval,
                                    @RequestParam( value = "sendTimeStart", required = false) String sendTimeStart,
                                    @RequestParam( value = "sendTimeEnd", required = false) String sendTimeEnd,
                                    @RequestParam( value = "pageNum", required = false) int pageNum,
                                    @RequestParam( value = "pageSize", required = false) int pageSize);

    /**
     * 创建告警通知配置
     */
    @PostMapping(value = "/createAlertNotifyConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建告警通知配置", notes = "创建告警通知配置", tags = {"Alerts Notify Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String createAlertNotifyConfig(@RequestBody ComAlertNotifyConfigReq request);

    /**
     * 修改告警通知配置
     */
    @PutMapping(value = "/updateAlertNotifyConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改告警通知配置", notes = "修改告警通知配置", tags = {"Alerts Notify Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String updateAlertNotifyConfig(@RequestBody ComAlertNotifyConfigReq request);

    /**
     * 获取告警通知配置详情
     */
    @GetMapping(value = "/getAlertNotifyConfigDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警通知配置详情", notes = "获取告警通知配置详情", tags = {"Alerts Notify Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = Object.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    Object getAlertNotifyConfigDetail(@RequestParam("uuid") String uuid);

    /**
     * 删除告警通知配置
     */
    @DeleteMapping(value = "/deleteAlertNotifyConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除告警通知配置", notes = "删除告警通知配置", tags = {"Alerts Notify Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String deleteAlertNotifyConfig(@RequestBody List<String> uuidList);

    /**
     * 打开告警通知配置
     */
    @PostMapping(value = "/openAlertNotifyConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "打开告警通知配置", notes = "打开告警通知配置", tags = {"Alerts Notify Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String openAlertNotifyConfig(@RequestBody List<String> uuidList);

    /**
     * 关闭告警通知配置
     */
    @PostMapping(value = "/closeAlertNotifyConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "关闭告警通知配置", notes = "关闭告警通知配置", tags = {"Alerts Notify Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String closeAlertNotifyConfig(@RequestBody List<String> uuidList);

    /**
     * 拷贝告警通知配置
     */
    @GetMapping(value = "/copyAlertNotifyConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "拷贝告警通知配置", notes = "拷贝告警通知配置", tags = {"Alerts Notify Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String copyAlertNotifyConfig(@RequestParam("uuid") String uuid);

    /**
     * 获取告警通知配置规则
     */
    @GetMapping(value = "/getAlertNotifyConfigRule", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警通知配置规则", notes = "获取告警通知配置规则", tags = {"Alerts Notify Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    Map<String,String> getAlertNotifyConfigRule();

    /**
     * 更新告警通知配置规则
     */
    @PutMapping(value = "/updateAlertNotifyConfigRule", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "更新告警通知配置规则", notes = "更新告警通知配置规则", tags = {"Alerts Notify Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    String updateAlertNotifyConfigRule(@RequestBody  Map<String,String> req);

    /**
     * 判断当前用户是否拥有操作权限
     */
    @GetMapping(value = "/getOperationPermission", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "判断当前用户是否拥有操作权限", notes = "判断当前用户是否拥有操作权限", tags = {"Alerts Notify Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = boolean.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    boolean getOperationPermission(@RequestParam("creator") String creator);
}
