package com.aspire.ums.cmdb.maintain.mapper;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.Circle;

public interface CircleMapper {
	
	@SuppressWarnings("rawtypes")
    List<Map> getAll(Circle circle);
	
	Circle getOne(String id);

	void insert(Circle circle);

	void update(Circle circle);

	void delete(String id);
	
    List<Circle> getByCondition(Circle circle);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceByView(Map map);
    
    void deleteInstance(String id);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceByModule(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> getHistoryByInstance(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> getHistoryByRelation(Map map);

}
