package com.aspire.mirror.elasticsearch.api.service.zabbix;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.elasticsearch.api.dto.NetPerformanceAnalysis;
import com.aspire.mirror.elasticsearch.api.dto.NetSetDto;

/**
 * @author baiwp
 * @title: INetworkStrategyService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/7/2716:07
 */
@RequestMapping("${version}/network/NetPerformanceAnalysis")
public interface INetPerformanceAnalysisService {
   

    /**
     * @param networkStrategyDtoList
     * @return 
     * @throws ParseException 
     */
	@PostMapping("getData")
    List<NetPerformanceAnalysis> getData(@RequestBody NetSetDto netSetDto) throws ParseException;

	@GetMapping("getTrendsData")
	Map<String, Object> getTrendsData(@RequestParam(value = "startTime") String startTime,
			@RequestParam(value = "endTime") String endTime, @RequestParam(value = "ip") String ip,
			@RequestParam(value = "granularity") String granularity,
			@RequestParam(value = "monitorFlag") String monitorFlag,
			@RequestParam(value = "idcType", required = false) String idcType) throws ParseException;

}
