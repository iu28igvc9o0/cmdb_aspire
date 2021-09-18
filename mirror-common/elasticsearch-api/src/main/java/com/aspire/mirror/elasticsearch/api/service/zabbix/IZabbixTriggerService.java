package com.aspire.mirror.elasticsearch.api.service.zabbix;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.elasticsearch.api.dto.TriggerDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: ITriggerService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:52
 */
@RequestMapping("${version}/trigger")
public interface IZabbixTriggerService {

    @PostMapping("")
    void insert(@RequestBody List<TriggerDto> triggerList);

    @DeleteMapping("")
    void deleteAll();

    @GetMapping("")
    PageResult<TriggerDto> query(@RequestParam(value = "itemId", required = false) String itemId,
                              @RequestParam(value = "idc", required = false) String idc,
                                 @RequestParam(value = "prefix", required = false) String prefix,
                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize);

    @GetMapping("/queryTriggerThreshold")
    List<Map<String, Object>> queryTriggerThreshold(@RequestParam(value = "itemIds", required = false) List<String> itemIds,
                                                    @RequestParam(value = "idc", required = false) String idc,
                                                    @RequestParam(value = "prefix", required = false) String prefix);
}
