package com.aspire.ums.cmdb.resource.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ResourceHighChartMapper {

	List<Map<String, String>> getResourcePool();

	int getServerOverview(@Param("resourcepool") String resourcepool,
                                 @Param("type") String type, @Param("sclass") String sclass);

	int getServerCount(@Param("type") String type, @Param("sclass") String sclass);

	String getFutureplanServerCount();

	String getFutureplanStorageCount();

	List<Map<String,Object>> getServerEstimateCount(@Param("resourcepool") String resourcepool, @Param("sclass") String sclass);

	List<Map<String,Object>> getServerEstimateAssignCount(@Param("resourcepool") String resourcepool, @Param("sclass") String sclass);

	int getBuildOverview(@Param("type") String type);

	int getCollectOverview(@Param("type") String type);

}
