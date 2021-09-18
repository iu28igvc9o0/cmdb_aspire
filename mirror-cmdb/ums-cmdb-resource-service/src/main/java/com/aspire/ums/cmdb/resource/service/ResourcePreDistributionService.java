package com.aspire.ums.cmdb.resource.service;

import com.aspire.ums.cmdb.resource.entity.ResourcePreDistribution;

import java.util.List;
import java.util.Map;

public interface ResourcePreDistributionService {
	
	int assertDevice(Map<String, Object> params, String username)throws Exception;

	int getResourcePreDistributionCount(Map<String, Object> params);

	List<Map<String,String>> getResourcePreDistributionList(Map<String, Object> params);

	int saveResourcePreDistributionExcelData(List<ResourcePreDistribution> resourcePreDistributionList);

}
