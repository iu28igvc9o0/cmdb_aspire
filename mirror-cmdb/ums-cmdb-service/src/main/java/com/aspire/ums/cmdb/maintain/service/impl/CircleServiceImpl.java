package com.aspire.ums.cmdb.maintain.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.maintain.entity.Circle;
import com.aspire.ums.cmdb.maintain.entity.MaintainView;
import com.aspire.ums.cmdb.maintain.mapper.CircleMapper;
import com.aspire.ums.cmdb.maintain.mapper.InstanceMapper;
import com.aspire.ums.cmdb.maintain.mapper.InstanceRelationMapper;
import com.aspire.ums.cmdb.maintain.mapper.MaintainViewMapper;
import com.aspire.ums.cmdb.maintain.service.CircleService;
import com.aspire.ums.cmdb.util.UUIDUtil;

@Service
@Transactional
public class CircleServiceImpl implements CircleService {
	
	@Autowired
	private CircleMapper circleMapper;
	
    @Autowired
    private MaintainViewMapper maintainViewMapper;
    
    @Autowired
    private InstanceRelationMapper instanceRelationMapper;
    
    @Autowired
    private InstanceMapper instanceMapper;

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getAll(Circle circle) {
        // TODO Auto-generated method stub
        return circleMapper.getAll(circle);
    }

    @Override
    public Circle getOne(String id) {
        // TODO Auto-generated method stub
        return circleMapper.getOne(id);
    }

    @Override
    public void insert(Circle circle) {
        // TODO Auto-generated method stub
        MaintainView mainView = new MaintainView();
        mainView.setCircleId(circle.getId());
        mainView.setDefaultView("true");
        mainView.setId(UUIDUtil.getUUID());
        mainView.setName("默认视图");
        mainView.setSort(1);
        circleMapper.insert(circle);
        maintainViewMapper.insert(mainView);
        
    }

    @Override
    public void update(Circle circle) {
        // TODO Auto-generated method stub
        circleMapper.update(circle);
        
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void delete(Circle circle) {
        // TODO Auto-generated method stub
        circleMapper.update(circle);
        Map inMap = new HashMap();
        inMap.put("circleId", circle.getId());
        instanceMapper.deleteInstanceCirlce(inMap);
        
    }

    @Override
    public List<Circle> getByCondition(Circle circle) {
        // TODO Auto-generated method stub
        return circleMapper.getByCondition(circle);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getInstanceByView(Map map) {
        // TODO Auto-generated method stub
        return circleMapper.getInstanceByView(map);
    }
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void deleteInstance(String id) {
        // TODO Auto-generated method stub
            circleMapper.deleteInstance(id);
            //删除关系
            Map inMap = new HashMap();
            inMap.put("targerInstanceId", id);
            instanceRelationMapper.delete(inMap);
            inMap.clear();
            inMap.put("sourceInstanceId", id);
            instanceRelationMapper.delete(inMap);

    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getInstanceByModule(Map map) {
        // TODO Auto-generated method stub
        return circleMapper.getInstanceByModule(map);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getHistoryByActionType(Map map) {
        if("Relation".equals(map.get("actionType"))){
            return circleMapper.getHistoryByRelation(map);
        }else if("Instance".equals(map.get("actionType"))){
            return circleMapper.getHistoryByInstance(map);
        }
        // TODO Auto-generated method stub
        return null;
    }
}
