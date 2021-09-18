package com.aspire.ums.cmdb.maintain.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.Circle;

public interface CircleService {
	
	@SuppressWarnings("rawtypes")
    List<Map> getAll(Circle circle);
	
	Circle getOne(String id);

	void insert(Circle circle);

	void update(Circle circle);

	void delete(Circle circle);
	
    List<Circle> getByCondition(Circle circle);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceByView(Map map);
    
    public void deleteInstance(String id) ;
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceByModule(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> getHistoryByActionType(Map map);
}
