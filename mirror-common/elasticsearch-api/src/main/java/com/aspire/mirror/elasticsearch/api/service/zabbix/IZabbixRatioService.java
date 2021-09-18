package com.aspire.mirror.elasticsearch.api.service.zabbix;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;
import com.aspire.mirror.elasticsearch.api.dto.NetPerformanceAnalysis;

/**
 * @author baiwp
 * @title: ITrendsService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2116:25
 */
@RequestMapping("${version}/trends")
public interface IZabbixRatioService {

  
    @GetMapping("/getServerRatioData")
	Map<String,NetPerformanceAnalysis> getServerRatioData(@RequestParam(value = "deviceType", required = false)String deviceType
			,@RequestParam(value = "idcType", required = false) String idcType,
			@RequestParam(value = "ipList", required = false) List<String> ipList);

    @PostMapping("/getDepartmentRatioData")
    Map<String,Map<String,Object>> getDepartmentRatioData(@RequestBody MonthReportRequest monthReportRequests) throws ParseException;
    
    
}
