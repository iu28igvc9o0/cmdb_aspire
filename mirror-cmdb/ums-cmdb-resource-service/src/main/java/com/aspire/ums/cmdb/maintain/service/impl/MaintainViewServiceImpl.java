package com.aspire.ums.cmdb.maintain.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.aspire.ums.cmdb.maintain.entity.MaintainView;
import com.aspire.ums.cmdb.maintain.mapper.MaintainViewMapper;
import com.aspire.ums.cmdb.maintain.service.MaintainViewService;
import com.aspire.ums.cmdb.util.UUIDUtil;

@Service
@Transactional
public class MaintainViewServiceImpl implements MaintainViewService {
	
	@Autowired
	private MaintainViewMapper maintainViewMapper;

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getAll(MaintainView mainView) {
        // TODO Auto-generated method stub
        return maintainViewMapper.getAll(mainView);
    }

    @Override
    public MaintainView getOne(String id) {
        // TODO Auto-generated method stub
        return maintainViewMapper.getOne(id);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void insert(MaintainView maintainView, String[] moduleIds) {
        // TODO Auto-generated method stub
        maintainView.setId(UUIDUtil.getUUID());
        maintainViewMapper.insert(maintainView);
        
        for(String moduleId:moduleIds){
            Map map = new HashMap();
            map.put("moduleId", moduleId);
            map.put("viewId", maintainView.getId());
            maintainViewMapper.addViewCondition(map);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void update(MaintainView mainView, String[] moduleIds) {
        // TODO Auto-generated method stub
        maintainViewMapper.update(mainView);
        maintainViewMapper.deleteViewCondition(mainView.getId());
        for(String moduleId:moduleIds){
            Map map = new HashMap();
            map.put("moduleId", moduleId);
            map.put("viewId", mainView.getId());
            maintainViewMapper.addViewCondition(map);
        }
        
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        maintainViewMapper.delete(id);
        maintainViewMapper.deleteViewCondition(id);
        
    }

    @Override
    public List<MaintainView> getByCondition(MaintainView mainView) {
        // TODO Auto-generated method stub
        return maintainViewMapper.getByCondition(mainView);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getModule(Map map) {
        // TODO Auto-generated method stub
        return maintainViewMapper.getModule(map);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addViewCondition(Map map) {
        // TODO Auto-generated method stub
        maintainViewMapper.addViewCondition(map);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getModuleIds(Map map) {
        // TODO Auto-generated method stub
        return maintainViewMapper.getModuleIds(map);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void updateTagName(String instanceId, String[] tags) {
        // TODO Auto-generated method stub
        maintainViewMapper.deleteInstanceTag(instanceId);
        if(tags!=null && tags.length>0){
        for(String tag:tags){
            Map map = new HashMap();
            map.put("id", UUIDUtil.getUUID());
            map.put("name", tag);
            map.put("instanceId", instanceId);
            maintainViewMapper.addInstanceTag(map);
        }
        }
        
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> selectInstanceTag(String id) {
        // TODO Auto-generated method stub
        return maintainViewMapper.selectInstanceTag(id);
    }
}
