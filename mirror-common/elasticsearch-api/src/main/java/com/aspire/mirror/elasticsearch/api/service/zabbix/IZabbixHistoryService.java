package com.aspire.mirror.elasticsearch.api.service.zabbix;

import com.aspire.mirror.elasticsearch.api.dto.HistorySearchRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.elasticsearch.api.service.zabbix
 * 类名称:    IZabbixHistoryService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/24 20:11
 * 版本:      v1.0
 */
@RequestMapping("${version}/history")
public interface IZabbixHistoryService {
    @PostMapping("")
    Map<String,Object> getMonitorValue(@RequestBody @Validated HistorySearchRequest historySearchRequest);
    @PostMapping("/getInstanceMonitorValue")
	Map<String, List<Map<String, Object>>> getInstanceMonitorValue(@RequestBody HistorySearchRequest historySearchRequest);
    @PostMapping("/getNullValueDeviceList")
    List<String> getNullValueDeviceList(@RequestBody HistorySearchRequest historySearchRequest);
    @PostMapping("/getIdcTypePerformanceData")
    Map<String, Map<String, Object>> getIdcTypePerformanceData(@RequestBody HistorySearchRequest historySearchRequest) throws ParseException;
}
