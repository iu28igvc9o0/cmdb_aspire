package com.aspire.ums.cmdb.maintain.mapper;

import java.util.List;
import java.util.Map;
import com.aspire.ums.cmdb.maintain.entity.ModuleRelation;
import com.aspire.ums.cmdb.maintain.entity.Relation;

public interface ModuleRelationMapper {
	
	@SuppressWarnings("rawtypes")
    List<Map> getAll(ModuleRelation moduleRelation);
	
	ModuleRelation getOne(String id);

	void insert(ModuleRelation moduleRelation);

	void update(ModuleRelation moduleRelation);

	void delete(String id);
	
    @SuppressWarnings("rawtypes")
    List<Map> getRetionByCondition(ModuleRelation moduleRelation);
    
    @SuppressWarnings("rawtypes")
    List<Map> getModuleByCondition(ModuleRelation moduleRelation);
    
    void addRelation(Relation relation);
    
    void delRelation(Relation relation);
    
    List<Relation> getAllRelation();
    
    @SuppressWarnings("rawtypes")
    List<Map> checkRelationName(Map map);
    
    ModuleRelation getModuleRelation(ModuleRelation moduleRelation);

    @SuppressWarnings("rawtypes")
    List<Map> checkRelationInstanceList(Map map);
}
