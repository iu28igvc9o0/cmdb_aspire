package com.aspire.mirror.alert.api.service.alert;

import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsOverViewResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsQueryRequest;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/alerts/intelligent")
public interface AlertIntelligentService {

    /**
     *  活动/确认/删除 告警 查询列表
     */
    @PostMapping(value = "/queryAlertIntelligent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警智能收敛列表", notes = "告警智能收敛列表", 
    				response = PageResponse.class, tags = {"Alert Intelligent API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<AlertsDetailResponse> queryAlertIntelligent(@RequestBody AlertsQueryRequest queryRequest,
                                                             @RequestParam(value = "alertId", required = false) String alertId) throws ParseException;

    /**
     * (工作台)告警数量总览
     */
    @PostMapping(value = "/alertIntelligentOverview", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警智能收敛数量总览", notes = "告警智能收敛数量总览", 
    				response = AlertsOverViewResponse.class, tags = {"Alert Intelligent API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = AlertsOverViewResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = AlertsOverViewResponse.class)})
    AlertsOverViewResponse alertIntelligentOverview(@RequestBody AlertsQueryRequest queryRequest) throws ParseException;

    /**
     * 导出警报列表数据
     */
    @ApiOperation(value = "告警智能收敛列表导出", notes = "告警智能收敛列表导出")
    @RequestMapping(value = "/exportAlertIntelligentData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    List<Map<String, Object>> exportAlertIntelligentData(@RequestBody AlertsQueryRequest queryRequest) throws Exception;

}
