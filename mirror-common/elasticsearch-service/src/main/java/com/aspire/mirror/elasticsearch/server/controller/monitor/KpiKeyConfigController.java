package com.aspire.mirror.elasticsearch.server.controller.monitor;

import com.alibaba.fastjson.JSONArray;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.elasticsearch.api.dto.monitor.KpiKeyConfig;
import com.aspire.mirror.elasticsearch.api.service.monitor.IKpiKeyConfigService;
import com.aspire.mirror.elasticsearch.server.biz.IKpiKeyConfigBiz;
import com.aspire.mirror.elasticsearch.server.enums.KpiTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.server.controller.monitor
 * @Author: baiwenping
 * @CreateTime: 2020-06-11 15:22
 * @Description: ${Description}
 */
@RestController
public class KpiKeyConfigController implements IKpiKeyConfigService {

    @Autowired
    private IKpiKeyConfigBiz iKpiKeyConfigBiz;
    /**
     * 新增
     *
     * @param kpiKeyConfigList
     */
    @Override
    public void insert(@RequestBody List<KpiKeyConfig> kpiKeyConfigList) {
        if (CollectionUtils.isEmpty(kpiKeyConfigList)) {
            return;
        }
        if (kpiKeyConfigList.size() == 1) {
            iKpiKeyConfigBiz.insert(kpiKeyConfigList.get(0));
        } else {
            iKpiKeyConfigBiz.insert(kpiKeyConfigList);
        }
    }

    /**
     * 查询
     *
     * @param kpiType
     * @param kpiKey
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult<KpiKeyConfig> query(@RequestParam(value = "kpiType", required = false) String kpiType,
                                          @RequestParam(value = "kpiKey", required = false) String kpiKey,
                                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return iKpiKeyConfigBiz.query(kpiType, kpiKey, pageNum, pageSize);
    }

    /**
     *
     * @return
     */
    public JSONArray getTypeEnum () {
        return KpiTypeEnum.toJson();
    }
}
