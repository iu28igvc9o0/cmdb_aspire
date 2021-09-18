package com.aspire.mirror.composite.service.alert;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.composite.service.alert.payload.ItemDto;
import com.aspire.mirror.composite.service.alert.payload.TrendsDto;
import com.aspire.mirror.composite.service.alert.payload.TriggerDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: IComAlertItemService
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/6/2410:11
 */
@RequestMapping("${version}/alerts/zabbix")
public interface ICompAlertItemService {
    @GetMapping("/item")
    PageResult<ItemDto> query(@RequestParam(value = "itemId", required = false) String itemId,
                              @RequestParam(value = "idc", required = false) String idc,
                              @RequestParam(value = "prefix", required = false) String prefix,
                              @RequestParam(value = "ip", required = false) String ip,
                              @RequestParam(value = "resourceId", required = false) String resourceId,
                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize);

    @GetMapping("/trends")
    List<TrendsDto> queryTrends(@RequestParam(value = "itemId") String itemId,
                                @RequestParam(value = "prefix", required = false) String prefix,
                                @RequestParam(value = "startTime") Long startTime,
                                @RequestParam(value = "endTime") Long endTime);
    @GetMapping("/trigger")
    PageResult<TriggerDto> query(@RequestParam(value = "itemId", required = false) String itemId,
                                 @RequestParam(value = "idc", required = false) String idc,
                                 @RequestParam(value = "prefix", required = false) String prefix,
                                 @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "20") int pageSize);

    @GetMapping("/deviceUsedRate")
    Map<String, Object> deviceUsedRate(@RequestParam(value = "bizSystem", required = false) String bizSystem,
                                              @RequestParam(value = "deviceType", required = false) String deviceType);

    @GetMapping("/storageUsedRate")
	Map<String, Object> storageUsedRate(@RequestParam(value = "startTime",required=false) String startTime,
			@RequestParam(value = "endTime",required=false) String endTime,
			@RequestParam(value = "bizSystem", required = false) String bizSystem) throws ParseException;

    @GetMapping("/deviceUsedRateByMonitor")
	Map<String, Object> deviceUsedRateByMonitor(
			@RequestParam(value = "bizSystem", required = false) String bizSystem,
			@RequestParam(value = "monitor", required = false) String monitor) throws Exception;

    @PostMapping("/deviceUsedRateForKG")
    String deviceUsedRateForKG();

    /**
     * 根据设备id查询监控项列表以及对应阈值
     * @param resourceId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/itemByResourceId")
    PageResult<ItemDto> queryByResourceId(@RequestParam(value = "resourceId") String resourceId,
                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize);
}
