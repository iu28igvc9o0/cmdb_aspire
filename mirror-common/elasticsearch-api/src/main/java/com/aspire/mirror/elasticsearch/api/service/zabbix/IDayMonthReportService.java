package com.aspire.mirror.elasticsearch.api.service.zabbix;

import java.text.ParseException;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;

/**
 * @author longfeng
 * @title: IItemService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:52
 */
@RequestMapping("${version}/report/day")
public interface IDayMonthReportService {




    @PostMapping("/idcTypeDeviceUsedRateByDay")
    Map<String, Map<String, Object>> idcTypeDeviceUsedRateByDay(@RequestBody MonthReportRequest monthReportRequest) throws Exception;

	 @PostMapping("/queryByDay")
	Map<String, Map<String, Object>> queryByDay(@RequestBody MonthReportRequest monthReportRequest) throws ParseException;
	 @PostMapping("/bizSystemUsedRateByDay")
	Map<String, Map<String, Object>> bizSystemUsedRateByDay(@RequestBody MonthReportRequest monthReportRequest) throws Exception;
}
