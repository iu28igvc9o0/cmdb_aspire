package com.aspire.mirror.alert.api.service.third;

import com.aspire.mirror.alert.api.dto.third.CommonResp;
import com.aspire.mirror.alert.api.dto.third.ThirdCreateAlertReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ThirdAlertService {

    /**
     * 告警上报（第三方）
     * @param thirdCreateAlertReq
     * @return
     */
    @PostMapping("/v1/alerts/third/create")
    @ApiOperation(value = "告警上报（第三方）",notes = "告警上报（第三方）",tags = {"Third API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "内部错误")
    })
    CommonResp createdAlert(@RequestBody ThirdCreateAlertReq thirdCreateAlertReq);

    /**
     * 批量告警上报（第三方）
     * @param thirdCreateAlertReqList
     * @return
     */
    @PostMapping("/v1/alerts/third/batch")
    @ApiOperation(value = "批量告警上报（第三方）",notes = "批量告警上报（第三方）",tags = {"Third API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "内部错误")
    })
    CommonResp createdAlerts(@RequestBody List<ThirdCreateAlertReq> thirdCreateAlertReqList);
}
