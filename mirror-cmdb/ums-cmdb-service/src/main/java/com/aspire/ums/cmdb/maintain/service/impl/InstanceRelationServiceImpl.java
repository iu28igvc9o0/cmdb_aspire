package com.aspire.ums.cmdb.maintain.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.aspire.ums.cmdb.maintain.entity.InstanceRelation;
import com.aspire.ums.cmdb.maintain.mapper.InstanceRelationMapper;
import com.aspire.ums.cmdb.maintain.service.InstanceRelationService;

@Service
@Transactional
public class InstanceRelationServiceImpl implements InstanceRelationService {
	
	@Autowired
	private InstanceRelationMapper InstanceRelationMapper;

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getAll(InstanceRelation instanceRelation) {
        // TODO Auto-generated method stub
        return InstanceRelationMapper.getAll(instanceRelation);
    }

    @Override
    public InstanceRelation getOne(String id) {
        // TODO Auto-generated method stub
        return InstanceRelationMapper.getOne(id);
    }

    @Override
    public void insert(List<InstanceRelation> list) {
        // TODO Auto-generated method stub
        for(InstanceRelation m:list){
        InstanceRelationMapper.insert(m);
        }
    }

    @Override
    public void update(InstanceRelation instanceRelation) {
        // TODO Auto-generated method stub
        InstanceRelationMapper.update(instanceRelation);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        Map inMap =new HashMap();
        inMap.put("id", id);
        InstanceRelationMapper.delete(inMap);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getRetionByCondition(InstanceRelation instanceRelation) {
        // TODO Auto-generated method stub
        return InstanceRelationMapper.getRetionByCondition(instanceRelation);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getInstanceUpRetionByModule(Map map) {
        // TODO Auto-generated method stub
        return InstanceRelationMapper.getInstanceUpRetionByModule(map);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getInstanceDownRetionByModule(Map map) {
        // TODO Auto-generated method stub
        return InstanceRelationMapper.getInstanceDownRetionByModule(map);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getInstanceRetionType(Map map) {
        // TODO Auto-generated method stub
        return InstanceRelationMapper.getInstanceRetionType(map);
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public void addInstanceRelation(List<InstanceRelation> list, Map map) {
        // TODO Auto-generated method stub
    
        InstanceRelationMapper.delete(map);
        for(InstanceRelation i:list){
            InstanceRelationMapper.insert(i);
        }
        
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> checkInstanceRelation(Map map) {
        // TODO Auto-generated method stub
        return InstanceRelationMapper.checkInstanceRelation(map);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<InstanceRelation> getRelationHistory(Map map) {
        // TODO Auto-generated method stub
        return InstanceRelationMapper.getRelationHistory(map);
    }
    
    
}
