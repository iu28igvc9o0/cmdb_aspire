package com.aspire.mirror.composite.service.third;

import com.aspire.mirror.composite.payload.third.CompThirdCreateAlertReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "告警（第三方）")
@RequestMapping("/${version}/alerts/third")
public interface ICompAlertThirdService {

    /**
     * 告警上报（第三方）
     */
    @PostMapping(value="/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "告警上报（第三方）",notes = "告警上报（第三方）",tags = {"Third Alert API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "内部错误")
    })
    Object createdAlerts(@RequestBody CompThirdCreateAlertReq thirdCreateAlertReqList);
}
