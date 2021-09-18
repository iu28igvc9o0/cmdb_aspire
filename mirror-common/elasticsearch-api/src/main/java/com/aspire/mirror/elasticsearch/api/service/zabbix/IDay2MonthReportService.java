package com.aspire.mirror.elasticsearch.api.service.zabbix;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.mirror.elasticsearch.api.dto.MonthBizSystemDayRequest;
import com.aspire.mirror.elasticsearch.api.dto.MonthBizSystemDayResponse;
import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;

/**
 * @author longfeng
 * @title: IItemService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:52
 */
@RequestMapping("${version}/report/day2")
public interface IDay2MonthReportService {




    @PostMapping("/idcTypeDeviceUsedRateByDay")
    Map<String, Map<String, Object>> idcTypeDeviceUsedRateByDay(@RequestBody MonthReportRequest monthReportRequest) throws Exception;

   
	 @PostMapping("/queryByDay")
	Map<String, Map<String, Object>> queryByDay(@RequestBody MonthReportRequest monthReportRequest) throws ParseException;
	 @PostMapping("/idcTypeDeviceUsedRate")
	Map<String, Map<String, Object>> idcTypeDeviceUsedRate(MonthReportRequest monthReportRequest) throws Exception;
	 @PostMapping("/bizSystemDeviceUsedRate")
	Map<String, Map<String, Object>> bizSystemDeviceUsedRate(MonthReportRequest monthReportRequest) throws Exception;

	 @PostMapping("/queryIdcTypeByDay")
	Map<String, Map<String, Object>> queryIdcTypeByDay(@RequestBody MonthReportRequest monthReportRequest) throws ParseException;

	 /**
	  * 提供cmdb的查询业务系统下的设备的每天均峰值明细
	  * @param monthRequest
	  * @return
	  * @throws ParseException
	  */
	 @PostMapping("/getBizSytemDataByDay")
	List<MonthBizSystemDayResponse> getBizSytemDataByDay(@RequestBody MonthBizSystemDayRequest monthRequest) throws ParseException;
	 
	 @PostMapping("/getBizSytemDataByDayReturnMap")
		List<Map<String, Object>> getBizSytemDataByDayReturnMap(@RequestBody MonthBizSystemDayRequest monthRequest) throws ParseException;
	 
	 @PostMapping("/queryIdcTypeByMonth")
	 public Map<String, Map<String, Object>> queryIdcTypeByMonth(@RequestBody MonthReportRequest monthReportRequest)
				throws ParseException;
	 
	 @PostMapping("/queryIdcTypeAllByMonth")
	 public  Map<String, Object> queryIdcTypeAllByMonth(@RequestBody MonthReportRequest monthReportRequest)
				throws ParseException ;

	 @PostMapping("/getPoorEfficiencyDeviceMonthData")
	List<Map<String, Object>> getPoorEfficiencyDeviceMonthData(@RequestBody MonthReportRequest request)
			throws ParseException;

	 @PostMapping("/getPoorEfficiencyDeviceWeekData")
	List<Map<String, Object>> getPoorEfficiencyDeviceWeekData(@RequestBody MonthReportRequest monthRequest) throws ParseException;
}
