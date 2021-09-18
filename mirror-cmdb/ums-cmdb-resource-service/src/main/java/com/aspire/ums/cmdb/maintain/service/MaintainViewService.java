package com.aspire.ums.cmdb.maintain.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.MaintainView;

public interface MaintainViewService {
	
    @SuppressWarnings("rawtypes")
    List<Map> getAll(MaintainView mainView);
    
    MaintainView getOne(String id);

    void insert(MaintainView mainView, String[] moduleIds);

    void update(MaintainView mainView, String[] moduleIds);

    void delete(String id);
    
    List<MaintainView> getByCondition(MaintainView mainView);
    
    @SuppressWarnings("rawtypes")
    List<Map> getModule(Map map);
    
    @SuppressWarnings("rawtypes")
    void addViewCondition(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> getModuleIds(Map map);
    
    void updateTagName(String instanceId, String[] tags) ;
    
    @SuppressWarnings("rawtypes")
    List<Map> selectInstanceTag(String id);
    
}
