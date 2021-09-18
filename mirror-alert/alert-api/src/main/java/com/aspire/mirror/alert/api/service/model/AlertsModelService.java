package com.aspire.mirror.alert.api.service.model;

import com.aspire.mirror.alert.api.dto.model.AlertModelDetail;
import com.aspire.mirror.alert.api.dto.model.AlertModelRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 监控告警产品配置化-告警模型
 */
@RequestMapping("/v2/alerts/model")
@Api(value = "告警模型")
public interface AlertsModelService {

    @PostMapping(value = "/insertAlertModel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增告警模型数据", notes = "新增告警模型数据", tags = {"AlertModel API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "Unexpected error")})
    void insertAlertModel(@RequestBody AlertModelRequest request);

    @PostMapping(value = "/getAlertModelList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警模型数据列表", notes = "获取告警模型数据列表", tags = {"AlertModel API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<AlertModelDetail> getAlertModelList(@RequestParam(value = "modelName", required = false) String modelName,
                                              @RequestParam(value = "tableName", required = false) String tableName);

    @PostMapping(value = "/getAlertModelTreeData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警模型树", notes = "获取告警模型树", tags = {"AlertModel API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    Object getAlertModelTreeData();

    @DeleteMapping(value = "/deleteAlertModel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除告警模型数据", notes = "删除告警模型数据", tags = {"AlertModel API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void deleteAlertModel(@RequestParam(value = "userName") String userName,@RequestBody List<String> request);

    @GetMapping(value = "/getAlertModelDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取告警模型数据详情", notes = "获取告警模型数据详情", tags = {"AlertModel API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    AlertModelDetail getAlertModelDetail(@RequestParam(value = "id") String id);

    @PostMapping(value = "/updateAlertModel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "编辑告警模型数据", notes = "编辑告警模型数据", tags = {"AlertModel API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "Unexpected error")})
    void updateAlertModel(@RequestBody AlertModelRequest request);
}