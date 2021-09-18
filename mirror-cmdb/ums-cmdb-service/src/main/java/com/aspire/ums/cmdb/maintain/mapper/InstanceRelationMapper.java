package com.aspire.ums.cmdb.maintain.mapper;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.InstanceRelation;

public interface InstanceRelationMapper {
    
    @SuppressWarnings("rawtypes")
    List<Map> getAll(InstanceRelation instanceRelation);
    
    InstanceRelation getOne(String id);

    void insert(InstanceRelation instanceRelation);

    void update(InstanceRelation instanceRelation);

    @SuppressWarnings("rawtypes")
    void delete(Map map);
    
    void deleteByInstanceId(String instanceId);
    
    @SuppressWarnings("rawtypes")
    List<Map> getRetionByCondition(InstanceRelation instanceRelation);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceUpRetionByModule(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceDownRetionByModule(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceRetionType(Map map);
    
    public List<Map<String,String>> getMuduleRelationIdAndInstanceName(String instanceId);
    
    @SuppressWarnings("rawtypes")
    List<Map> checkInstanceRelation(Map map);
    
    @SuppressWarnings("rawtypes")
    List<InstanceRelation> getRelationHistory(Map map);
}
