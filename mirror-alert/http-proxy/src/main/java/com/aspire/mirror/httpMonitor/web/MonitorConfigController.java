package com.aspire.mirror.httpMonitor.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/httpMonitor")
public class MonitorConfigController {
    private static final Logger logger = LoggerFactory.getLogger(MonitorConfigController.class);
    
    @ApiOperation(value = "删除用户信息", notes = "删除用户", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/test")
    public void addConfig() {
    	logger.info("测试成功");

    }
}
