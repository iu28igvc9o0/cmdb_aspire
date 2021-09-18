package com.aspire.mirror.elasticsearch.api.service.zabbix;

import java.text.ParseException;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.elasticsearch.api.dto.MonthReportNetRequest;
import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;

/**
 * @author baiwp
 * @title: IItemService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:52
 */
@RequestMapping("${version}/report")
public interface IMonthReportService {

    @PostMapping("/queryNetBandwidth")
	Map<String, Map<String, Object>> queryNetBandwidth(@RequestBody MonthReportNetRequest monthReportRequest) throws ParseException;
    @PostMapping("/queryNetUseRatio")
	Map<String, Map<String, Object>> queryNetUseRatio(@RequestBody MonthReportNetRequest monthReportRequest) throws ParseException;

    @PostMapping("/queryNetUseTotal")
	Map<String, Map<String, Object>> queryNetUseTotal(@RequestBody MonthReportNetRequest monthReportRequest) throws ParseException;
    
    //IT云租户业务系统资源利用率
    @PostMapping("/queryBizSystemUserRate")
	Map<String, Map<String, Object>> queryBizSystemUserRate(MonthReportRequest monthReportRequest)
			throws ParseException;
    //IT公司内部资源利用率/IT公司外部资源利用率
    @PostMapping("/queryDepartment2UserRate")
	Map<String, Map<String, Object>> queryDepartment2UserRate(MonthReportRequest monthReportRequest,
			@RequestParam(value = "department1Flag", required = false) boolean department1Flag) throws ParseException;
    
    //资源性能
    @PostMapping("/queryIdcTypeUserRate")
	Map<String, Map<String, Object>> queryIdcTypeUserRate(MonthReportRequest monthReportRequest) throws ParseException;
    
   //运营月报表2-1  指标情况
    @PostMapping("/queryPhyUserRate")
    Map<String, Map<String, Object>> queryPhyUserRate(MonthReportRequest monthReportRequest) throws ParseException;
   
  
}
