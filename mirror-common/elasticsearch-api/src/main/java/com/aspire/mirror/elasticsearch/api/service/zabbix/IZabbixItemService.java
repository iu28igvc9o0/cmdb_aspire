package com.aspire.mirror.elasticsearch.api.service.zabbix;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.elasticsearch.api.dto.ItemDto;
import com.aspire.mirror.elasticsearch.api.dto.ItemIndexDto;
import com.aspire.mirror.elasticsearch.api.dto.ItemRequest;
import com.aspire.mirror.elasticsearch.api.dto.ItemRequestPage;
import com.aspire.mirror.elasticsearch.api.dto.MonitorKeyDto;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author baiwp
 * @title: IItemService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:52
 */
@RequestMapping("${version}/item")
public interface IZabbixItemService {

    @PostMapping("")
    void insert(@RequestBody List<ItemDto> itemDtoList);

    @DeleteMapping("")
    void deleteAll();

    @GetMapping("")
    PageResult<ItemDto> query(@RequestParam(value = "itemId", required = false) String itemId,
                              @RequestParam(value = "idcType", required = false) String idcType,
                              @RequestParam(value = "prefix", required = false) String prefix,
                              @RequestParam(value = "ip", required = false) String ip,
                              @RequestParam(value = "resourceId", required = false) String resourceId,
                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize);

    @PostMapping("/object")
    Map<String, Set<ItemIndexDto>> queryObject(@RequestBody ItemRequest itemRequest);

    @PostMapping("/getMoniObjects")
    List<String> queryMoniObjects(@RequestBody ItemRequest itemRequest);

    @PostMapping("/queryMonitorObjectList")
	MonitorKeyDto queryMonitorObjectList(ItemRequest itemRequest);
}
