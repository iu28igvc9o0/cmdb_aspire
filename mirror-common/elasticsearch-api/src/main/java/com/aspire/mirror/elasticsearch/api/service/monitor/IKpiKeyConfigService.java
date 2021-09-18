package com.aspire.mirror.elasticsearch.api.service.monitor;

import com.alibaba.fastjson.JSONArray;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.elasticsearch.api.dto.ItemDto;
import com.aspire.mirror.elasticsearch.api.dto.monitor.KpiKeyConfig;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.api.service.monitor
 * @Author: baiwenping
 * @CreateTime: 2020-06-11 15:10
 * @Description: ${Description}
 */
@RequestMapping("${version}/kpi-key")
public interface IKpiKeyConfigService {
    /**
     * 新增
     * @param kpiKeyConfigList
     */
    @PostMapping("")
    void insert(@RequestBody List<KpiKeyConfig> kpiKeyConfigList);

    /**
     * 查询
     * @param kpiType
     * @param kpiKey
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("")
    PageResult<KpiKeyConfig> query(@RequestParam(value = "kpiType", required = false) String kpiType,
                              @RequestParam(value = "kpiKey", required = false) String kpiKey,
                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize);

    /**
     *
     * @return
     */
    @GetMapping("type-enum")
    JSONArray getTypeEnum ();
}
