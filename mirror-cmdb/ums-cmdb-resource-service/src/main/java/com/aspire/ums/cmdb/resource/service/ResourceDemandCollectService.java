package com.aspire.ums.cmdb.resource.service;

import com.aspire.ums.cmdb.resource.entity.ResourceBuildManageQueryEntity;
import com.aspire.ums.cmdb.resource.entity.ResourceDemandCollect;
import com.aspire.ums.cmdb.resource.entity.ResourceEstimateResponse;

import java.util.List;
import java.util.Map;

public interface ResourceDemandCollectService {

    Map<String, Object> selectResBuildMaData(ResourceBuildManageQueryEntity entity);
    void saveResourceCollectExcelData(List<ResourceDemandCollect> resourceDemandCollectList) throws Exception;
    ResourceEstimateResponse getCollectAll(Map<String, Object> params);
    List<Map<String, Object>> getResourceDemandCollectList(ResourceBuildManageQueryEntity entity);
}
