package com.aspire.mirror.elasticsearch.api.service.zabbix;

import com.aspire.mirror.elasticsearch.api.dto.NetPerformanceAnalysis;
import com.aspire.mirror.elasticsearch.api.dto.TrendsDto;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: ITrendsService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2116:25
 */
@RequestMapping("${version}/trends")
public interface IZabbixTrendsService {

    @PostMapping("")
    void insert(@RequestBody List<TrendsDto> trendsDtoList);

    @DeleteMapping("")
    void deleteAll();

    @GetMapping("")
    List<TrendsDto> query(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize);

    @GetMapping("/trend")
    List<TrendsDto> queryTrends(@RequestParam(value = "itemId") String itemId,
                                @RequestParam(value = "prefix", required = false) String prefix,
                                @RequestParam(value = "startTime") Long startTime,
                                @RequestParam(value = "endTime") Long endTime);

    @GetMapping("/deviceUsedRate")
    Map<String, Object> deviceUsedRate(@RequestParam(value = "bizSystem", required = false) String bizSystem,
                                       @RequestParam(value = "deviceType", required = false) String deviceType);

    @GetMapping("/storageUsedRate")
	Map<String, Object> storageUsedRate(@RequestParam(value = "startTime",required=false) String startTime,
			@RequestParam(value = "endTime",required=false) String endTime,
			@RequestParam(value = "bizSystem", required = false) String bizSystem) throws ParseException;




    
}
