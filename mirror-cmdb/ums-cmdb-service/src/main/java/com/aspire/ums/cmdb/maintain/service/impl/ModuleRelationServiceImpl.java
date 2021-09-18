package com.aspire.ums.cmdb.maintain.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.aspire.ums.cmdb.maintain.entity.ModuleRelation;
import com.aspire.ums.cmdb.maintain.entity.Relation;
import com.aspire.ums.cmdb.maintain.mapper.InstanceRelationMapper;
import com.aspire.ums.cmdb.maintain.mapper.ModuleRelationMapper;
import com.aspire.ums.cmdb.maintain.service.ModuleRelationService;

@Service
@Transactional
public class ModuleRelationServiceImpl implements ModuleRelationService {
	
	@Autowired
	private ModuleRelationMapper moduleRelationMapper;
	
    @Autowired
    private InstanceRelationMapper instanceRelationMapper;

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getAll(ModuleRelation moduleRelation) {
        // TODO Auto-generated method stub
        return moduleRelationMapper.getAll(moduleRelation);
    }

    @Override
    public ModuleRelation getOne(String id) {
        // TODO Auto-generated method stub
        return moduleRelationMapper.getOne(id);
    }

    @Override
    public void insert(List<ModuleRelation> list) {
        // TODO Auto-generated method stub
        for(ModuleRelation m:list){
        moduleRelationMapper.insert(m);
        }
    }

    @Override
    public void update(ModuleRelation moduleRelation) {
        // TODO Auto-generated method stub
        moduleRelationMapper.update(moduleRelation);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        moduleRelationMapper.delete(id);
        Map inMap = new HashMap();
        inMap.put("moduleRelationId", id);
        instanceRelationMapper.delete(inMap);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getRetionByCondition(ModuleRelation moduleRelation) {
        // TODO Auto-generated method stub
        return moduleRelationMapper.getRetionByCondition(moduleRelation);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getModuleByCondition(ModuleRelation moduleRelation) {
        // TODO Auto-generated method stub
        return moduleRelationMapper.getModuleByCondition(moduleRelation);
    }

    @Override
    public void addRelation(Relation relation) {
        // TODO Auto-generated method stub
        moduleRelationMapper.addRelation(relation);
        
    }

    @Override
    public void delRelation(Relation relation) {
        // TODO Auto-generated method stub
        moduleRelationMapper.delRelation(relation);
    }

    @Override
    public List<Relation> getAllRelation() {
        // TODO Auto-generated method stub
        return moduleRelationMapper.getAllRelation();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> checkRelationName(Map map) {
        // TODO Auto-generated method stub
        return moduleRelationMapper.checkRelationName(map);
    }

	
}
