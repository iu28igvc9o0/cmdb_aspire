package com.aspire.mirror.alert.api.service.kgEnv;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Api("科管监控首页")
@RequestMapping("/alerts/kg/monitor/index")
public interface KGMonitorIndexService {

    /**
     * 告警总览
     */
    @RequestMapping(value = "/getAlertView", method = RequestMethod.POST)
    @ApiOperation(value = "告警总览", notes = "告警总览", tags = {"KG Monitor Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getAlertView(@RequestBody Map<String,Object> param);

}
