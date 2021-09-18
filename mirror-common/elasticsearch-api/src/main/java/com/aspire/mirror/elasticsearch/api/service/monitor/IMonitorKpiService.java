package com.aspire.mirror.elasticsearch.api.service.monitor;

import com.aspire.mirror.elasticsearch.api.dto.monitor.MonitorCommonRequest;

import net.sf.json.JSONObject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.api.service.zabbix
 * @Author: baiwenping
 * @CreateTime: 2020-04-22 16:37
 * @Description: ${Description}
 */
@RequestMapping("${version}/monitorKpi")
public interface IMonitorKpiService {

    /**
     * 插入数据
     * @param index
     * @param type
     * @param map
     */
    @PostMapping("/insert/{index}/{type}")
    void insert(@PathVariable("index") String index, @PathVariable("type") String type,
                     @RequestBody Map<String, Object> map);

    /**
     * 插入数据
     * @param index
     * @param type
     * @param list
     */
    @PostMapping("/insertBatch/{index}/{type}")
    void insertBatch(@PathVariable("index") String index, @PathVariable("type") String type,
                @RequestBody List<Map<String, Object>> list);
    /**
     * 查询
     * @param monitorCommonRequest
     * @return
     */
    @PostMapping("/query")
    List<Map<String, Object>> query(@RequestBody MonitorCommonRequest monitorCommonRequest);
    
    @PostMapping("/getClusterIndex")
    String[] getClusterIndexOut(@RequestBody JSONObject jsonObject,@RequestParam(value = "indices") String... indices);
}
