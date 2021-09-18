package com.aspire.ums.cmdb.resource.mapper;

import com.aspire.ums.cmdb.resource.entity.ResourceEstimate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ResourceEstimateMapper {
    List<String> getResourcePoolAll_config();
    List<ResourceEstimate> getResourceEstimate_All(Map<String, Object> params);
    Map<String, Object> getResourceEstimate_All_count(Map<String, Object> params);
    ResourceEstimate getCollectByCollectIds(@Param(value = "collectIdList") List<String> collectIdList);
    void addEstimate(ResourceEstimate estimate);
    void updateEstimate(ResourceEstimate estimate);
    void updateResourceDeamandCollectEstimateId(@Param(value = "estimateId") String id, @Param(value = "collectIdList") List<String> collectIdList);
    int isClosedByPoolName(@Param("resourcePool") String resourcePool);
    void closeEstimate(@Param("id") String id,@Param("username") String username);
}
