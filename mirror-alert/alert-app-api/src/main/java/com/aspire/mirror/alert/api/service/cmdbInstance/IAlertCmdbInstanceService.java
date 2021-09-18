package com.aspire.mirror.alert.api.service.cmdbInstance;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/v1/alerts/cmdb")
@Api(value = "告警设备服务")
public interface IAlertCmdbInstanceService {

    @GetMapping(value = "/detail/{id}")
    Map<String, Object> detailById (@PathVariable(name = "id") String id);
}
