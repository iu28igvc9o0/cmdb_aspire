package com.aspire.ums.cmdb.maintain.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.InstanceRelation;

public interface InstanceRelationService {
	
    @SuppressWarnings("rawtypes")
    List<Map> getAll(InstanceRelation instanceRelation);
    
    InstanceRelation getOne(String id);

    void insert(List<InstanceRelation> list);

    void update(InstanceRelation instanceRelation);

    void delete(String id);
    
    @SuppressWarnings("rawtypes")
    List<Map> getRetionByCondition(InstanceRelation instanceRelation);

    @SuppressWarnings("rawtypes")
    List<Map> getInstanceUpRetionByModule(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceDownRetionByModule(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceRetionType(Map map);
    
    @SuppressWarnings("rawtypes")
    void addInstanceRelation(List<InstanceRelation> list, Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> checkInstanceRelation(Map map);
    
    @SuppressWarnings("rawtypes")
    List<InstanceRelation> getRelationHistory(Map map);
}
