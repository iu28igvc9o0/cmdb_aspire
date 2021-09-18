package com.aspire.ums.cmdb.resource.service;

import com.aspire.ums.cmdb.resource.entity.ResourceEstimate;
import com.aspire.ums.cmdb.resource.entity.ResourceEstimateResponse;

import java.util.List;
import java.util.Map;

public interface ResourceEstimateService {
    /**
     * 查询字典表中所有资源池
     * @return
     */
    List<String> getResourcePoolAll_config();

    /**
     * 查询列表
     * @param params
     * @param startIndex
     * @param limit
     * @return
     */
    ResourceEstimateResponse queryDataGrid(Map<String, Object> params, Integer startIndex, Integer limit);

    ResourceEstimateResponse queryCollectDataGrid(Map<String, Object> params, Integer startIndex, Integer limit);

    ResourceEstimate getCollectByCollectIds(String collectIdList) throws Exception;

    /**
     * 更新
     * @param estimate
     * @throws Exception
     */
    void updateEstimate(ResourceEstimate estimate)throws Exception;
    /**
     * 新增
     * @param estimate
     * @throws Exception
     */
    void addEstimate(ResourceEstimate estimate)throws Exception;

    int isClosedByPoolName(String resourcePool);

    /**
     * 关闭资源预估
     * @param id
     * @return
     */
    void closeEstimate(String id, String user)throws Exception;
}
