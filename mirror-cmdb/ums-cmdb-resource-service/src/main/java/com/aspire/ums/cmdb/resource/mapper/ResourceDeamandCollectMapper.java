package com.aspire.ums.cmdb.resource.mapper;

import com.aspire.ums.cmdb.resource.entity.ResourceBuildManageQueryEntity;
import com.aspire.ums.cmdb.resource.entity.ResourceDemandCollect;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ResourceDeamandCollectMapper {

    List<ResourceDemandCollect> getResourceCollectList(@Param("paramsDto") ResourceBuildManageQueryEntity paramsDto);
    List<ResourceDemandCollect> getResourceCollectCount(@Param("paramsDto") ResourceBuildManageQueryEntity paramsDto);
    List<Map<String, Object>> getResourceCollectCountXls(@Param("paramsDto") ResourceBuildManageQueryEntity paramsDto);
    List<ResourceDemandCollect> getCollectList(Map<String, Object> params);
    Map<String, Object> getCollectList_count(Map<String, Object> params);
    void saveResourceCollectExcelData(Map<String, Object> params);
    List<ResourceDemandCollect> getCollectAll(Map<String, Object> params);
    Map<String, Object> getCollectAll_count(@Param(value = "estimateId") String estimateId);
}
