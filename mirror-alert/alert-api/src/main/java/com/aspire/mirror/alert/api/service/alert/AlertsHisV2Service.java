package com.aspire.mirror.alert.api.service.alert;

import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.api.v2.service
 * @Author: baiwenping
 * @CreateTime: 2020-03-06 16:05
 * @Description: ${Description}
 */
@RequestMapping("/v2/alerts/alert_his")
@Api(value = "历史告警管理v2")
public interface AlertsHisV2Service {


    /**
     *  告警查询
     * @param queryParams
     * @return
     */
    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警查询", notes = "告警查询", response = PageResponse.class, tags = {"AlertHis v2 API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<Map<String, Object>> query(@RequestBody QueryParams queryParams);

    /**
     * 导出历史告警列表数据
     */
    @ApiOperation(value = "导出警报列表数据", notes = "导出警报列表数据", tags = {"AlertHis v2 API"})
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> export(@RequestBody QueryParams queryParams);

    /**
     * 查询详情
     * @param alertId
     * @return
     */
    @GetMapping(value = "/detail/{alert_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询详情", notes = "查询详情", response = Map.class, tags = {"AlertHis v2 API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> detail(@PathVariable(name = "alert_id") String alertId);
}
