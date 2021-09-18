package com.aspire.ums.cmdb.maintain.mapper;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.ConfigLog;



public interface ConfigLogMapper {
	
	void insert(ConfigLog configLog);
	
    @SuppressWarnings("rawtypes")
    List<Map> getRelationInfoList(Map map);

}
