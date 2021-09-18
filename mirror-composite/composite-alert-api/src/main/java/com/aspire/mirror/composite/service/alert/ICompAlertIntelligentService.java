package com.aspire.mirror.composite.service.alert;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.alert.CompAlertsDetailResp;
import com.aspire.mirror.composite.payload.alert.CompAlertsOverViewResponse;
import com.aspire.mirror.composite.payload.alert.CompAlertsQueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

@Api(value = "告警管理-告警智能收敛")
@RequestMapping("/${version}/alerts/intelligent")
public interface ICompAlertIntelligentService {

    /**
     *  告警智能收敛列表
     */
    @PostMapping(value = "/queryAlertIntelligent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警智能收敛列表", notes = "告警智能收敛列表", response = PageResponse.class, tags = {"Alert Intelligent API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertsDetailResp> queryAlertIntelligent(@RequestBody CompAlertsQueryRequest queryRequest,
                                                             @RequestParam(value = "alertId", required = false) String alertId) throws ParseException;

    /**
     * 告警智能收敛数量总览
     */
    @PostMapping(value = "/alertIntelligentOverview", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警智能收敛数量总览", notes = "告警智能收敛数量总览", response = CompAlertsOverViewResponse.class, tags = {"Alert Intelligent API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompAlertsOverViewResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompAlertsOverViewResponse.class)})
    CompAlertsOverViewResponse alertIntelligentOverview(@RequestBody CompAlertsQueryRequest queryRequest) throws ParseException;


    /**
     * 告警智能收敛导出
     */
    @ApiOperation(value = "告警智能收敛导出", notes = "告警智能收敛导出")
    @RequestMapping(value = "/exportAlertIntelligentData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    void exportAlertIntelligentData(@RequestBody CompAlertsQueryRequest queryRequest,HttpServletResponse response) throws Exception;

}
