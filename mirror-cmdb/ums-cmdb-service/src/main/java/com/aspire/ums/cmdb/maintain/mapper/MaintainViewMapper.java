package com.aspire.ums.cmdb.maintain.mapper;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.MaintainView;
import com.aspire.ums.cmdb.module.entity.Module;

public interface MaintainViewMapper {
	
	@SuppressWarnings("rawtypes")
    List<Map> getAll(MaintainView mainView);
	
	MaintainView getOne(String id);

	void insert(MaintainView mainView);

	void update(MaintainView mainView);

	void delete(String id);
	
    List<MaintainView> getByCondition(MaintainView mainView);
    
    @SuppressWarnings("rawtypes")
    List<Map> getModule(Map map);
    
    @SuppressWarnings("rawtypes")
    void addViewCondition(Map map);
    
    void deleteViewCondition(String viewId);
    
    @SuppressWarnings("rawtypes")
    List<Map> getModuleIds(Map map);
    
    void deleteInstanceTag(String id);
    
    @SuppressWarnings("rawtypes")
    void addInstanceTag(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> selectInstanceTag(String id);
    
    List<Module> selectModule();

}
