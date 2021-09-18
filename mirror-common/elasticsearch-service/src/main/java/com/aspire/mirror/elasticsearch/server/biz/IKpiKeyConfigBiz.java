package com.aspire.mirror.elasticsearch.server.biz;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.elasticsearch.api.dto.monitor.KpiKeyConfig;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.util.List;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.server.biz
 * @Author: baiwenping
 * @CreateTime: 2020-06-11 15:25
 * @Description: ${Description}
 */
public interface IKpiKeyConfigBiz {

    /**
     * 新增
     *
     * @param kpiKeyConfig
     */
    void insert(KpiKeyConfig kpiKeyConfig);

    /**
     * 批量新增
     *
     * @param list
     */
    void insert(List<KpiKeyConfig> list);

    /**
     * 查询
     *
     * @param kpiType
     * @param kpiKey
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult<KpiKeyConfig> query(String kpiType, String kpiKey, int pageNum, int pageSize);

    /**
     * 查询
     *
     * @param kpiType
     * @return
     */
    BoolQueryBuilder query4Builder(String kpiType, String field);

    /**
     * 查询
     *
     * @param kpiTypes
     * @return
     */
    BoolQueryBuilder query4Builder(String field, String... kpiTypes);
}
