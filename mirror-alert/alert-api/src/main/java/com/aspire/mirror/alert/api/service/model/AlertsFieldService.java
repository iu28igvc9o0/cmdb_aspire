package com.aspire.mirror.alert.api.service.model;

import com.aspire.mirror.alert.api.dto.model.AlertFieldDetail;
import com.aspire.mirror.alert.api.dto.model.AlertFieldRequest;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 监控告警产品配置化-告警模型字段
 */
@RequestMapping("/v2/alerts/model/field")
@Api(value = "告警模型字段")
public interface AlertsFieldService {

    @PostMapping(value = "/insertAlertField", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增告警模型字段数据", notes = "新增告警模型字段数据", tags = {"AlertField API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void insertAlertModel(@RequestBody AlertFieldRequest request);

    @GetMapping(value = "/getAlertFieldDetailById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据ID获取告警模型字段详情", notes = "根据ID获取告警模型字段详情", tags = {"AlertField API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    AlertFieldDetail getAlertFieldDetailById(@RequestParam("id") String id);

    @DeleteMapping(value = "/deleteAlertFieldDetailById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据ID删除告警模型字段详情", notes = "根据ID删除告警模型字段详情", tags = {"AlertField API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void deleteAlertFieldDetailById(@RequestParam("id") String id,
                                    @RequestParam("modelId") String modelId,
                                    @RequestParam("userName") String userName);

    @PostMapping(value = "/updateAlertField", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改告警模型字段数据", notes = "修改告警模型字段数据", tags = {"AlertField API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void updateAlertField(@RequestBody AlertFieldRequest request);

    @PostMapping(value = "/getAlertFieldListByModelId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据模型ID获取告警模型字段列表", notes = "根据模型ID获取告警模型字段列表", tags = {"AlertField API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageResponse<AlertFieldDetail> getAlertFieldListByModelId(@RequestBody Map<String,Object> request);

    @PostMapping(value = "/updateLockStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改锁定状态", notes = "修改锁定状态", tags = {"AlertField API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void updateLockStatus(@RequestParam("id") String id,
                          @RequestParam("modelId") String modelId,
                          @RequestParam("isLock") String isLock,
                          @RequestParam("userName") String userName);

    @PostMapping(value = "/synchronizeField", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "同步告警字段", notes = "同步告警字段", tags = {"AlertField API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void synchronizeField(@RequestParam("modelId") String modelId,
                            @RequestParam("userName") String userName);

    /**
     * 根据表名查询模型字段列表
     * @param tableName
     * @return
     */
    @GetMapping(value = "/listByTableName/{table_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据表名查询模型字段列表", notes = "根据表名查询模型字段列表", tags = {"AlertField API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<AlertFieldDetail> getModelFromRedis (@PathVariable(name = "table_name") String tableName, @RequestParam(name="sort", required = false) String sort);

}