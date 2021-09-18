package com.aspire.ums.cmdb.resource.mapper;


import com.aspire.ums.cmdb.resource.entity.ResourcePreDistribution;

import java.util.List;
import java.util.Map;

public interface ResourcePreDistributionMapper {

	List<ResourcePreDistributionMapper> getResourcePreDistribution();
	
	int updateStatus(Map<String, Object> params);
	
	int batchUpdateDeviceAssets(List<Map<String, Object>> deviceList);

	int getResourcePreDistributionCount(Map<String, Object> params);

	List<Map<String,String>> getResourcePreDistributionList(Map<String, Object> params);

	int saveResourcePreDistributionExcelData(List<ResourcePreDistribution> resourcePreDistributionList);

}
