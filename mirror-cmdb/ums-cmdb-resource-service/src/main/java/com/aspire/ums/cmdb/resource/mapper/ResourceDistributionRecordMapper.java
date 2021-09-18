package com.aspire.ums.cmdb.resource.mapper;


import com.aspire.ums.cmdb.resource.entity.ResourceDistributionRecord;

import java.util.List;

public interface ResourceDistributionRecordMapper {
	int insertBatch(List<ResourceDistributionRecord> recordList);
}
