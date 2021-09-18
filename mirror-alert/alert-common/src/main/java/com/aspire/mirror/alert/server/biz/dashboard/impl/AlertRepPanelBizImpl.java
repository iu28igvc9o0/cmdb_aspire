package com.aspire.mirror.alert.server.biz.dashboard.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.alert.server.biz.dashboard.AlertRepPanelBiz;
import com.aspire.mirror.alert.server.dao.dashboard.AlertRepPanelDao;
import com.aspire.mirror.alert.server.dao.dashboard.AlertRepPanelItemDao;
import com.aspire.mirror.alert.server.dao.dashboard.AlertRepPanelMoniterItemDao;
import com.aspire.mirror.alert.server.dao.dashboard.po.AlertRepPanel;
import com.aspire.mirror.alert.server.dao.dashboard.po.AlertRepPanelItem;
import com.aspire.mirror.alert.server.dao.dashboard.po.AlertRepPanelMoniterItem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AlertRepPanelBizImpl implements AlertRepPanelBiz{
	Logger LOGGER = LoggerFactory.getLogger(AlertRepPanelBizImpl.class);
	@Autowired
    private AlertRepPanelDao alertRepPanelDao;
	
	@Autowired
    private AlertRepPanelItemDao alertRepPanelItemDao;
	
	@Autowired
    private AlertRepPanelMoniterItemDao alertRepPanelMoniterItemDao;
	
	private static final String OPERATE_EDIT = "edit";
	
	private static final String OPERATE_COPY = "copy";
	
	@Override
	@Transactional(rollbackFor=Exception.class)  
	public AlertRepPanel insert(AlertRepPanel panel) throws Exception{
		try {
			panel.setId(null);
			alertRepPanelDao.insert(panel);
			if (null!=panel.getOperateFlag() && panel.getOperateFlag().equals(OPERATE_COPY)) {
				List<AlertRepPanelItem> items = panel.getItems();
				if (null != items && items.size() > 0) {
					for (AlertRepPanelItem i : items) {
						i.setPanel_id(panel.getId());
						i.setId(null);
					}
					alertRepPanelItemDao.batchInsert(items);

					for (AlertRepPanelItem i : items) {
						List<AlertRepPanelMoniterItem> moniters = i.getMoniterItems();
						for (AlertRepPanelMoniterItem m : moniters) {
							m.setItem_id(i.getId());
							m.setId(null);
						}
						alertRepPanelMoniterItemDao.batchInsert(moniters);
					}
				}
			}
			return panel;
			
		}catch(Exception e) {
			throw new Exception("新增大屏报错",e);
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void update(AlertRepPanel panel) throws Exception{
		try {
			alertRepPanelDao.update(panel);
			if (null ==panel.getOperateFlag() || !panel.getOperateFlag().equals(OPERATE_EDIT)) {
				List<AlertRepPanelItem> items = panel.getItems();
				//先删除 再 新增
				alertRepPanelMoniterItemDao.deleteByPanelId(panel.getId());
				alertRepPanelItemDao.deleteByPanelId(panel.getId());
				
				if(items.size()>0) {
					for(AlertRepPanelItem i:items) {
						i.setPanel_id(panel.getId());
					}
					alertRepPanelItemDao.batchInsert(items);
					
					for(AlertRepPanelItem i:items) {
						List<AlertRepPanelMoniterItem> moniters = i.getMoniterItems();
						for(AlertRepPanelMoniterItem m:moniters) {
							m.setItem_id(i.getId());
						}
						alertRepPanelMoniterItemDao.batchInsert(moniters);
					}
				}
			}
		}catch(Exception e) {
			throw new Exception("修改大屏报错", e);
		}
	}
	@Override
	public void delete(String id) throws Exception{
		try {
			alertRepPanelDao.deleteByPrimaryKey(id);
			alertRepPanelMoniterItemDao.deleteByPanelId(id);
			alertRepPanelItemDao.deleteByPanelId(id);
		}catch(Exception e) {
			throw new Exception("删除大屏报错",e);
		}
	}
	@Override
	public AlertRepPanel getByName(String name) {
		List<AlertRepPanel> ps = alertRepPanelDao.getByName(name);
		if (ps.size()>0) {
			return ps.get(0);
		}else {
			return null;
		}
	}
	@Override
	public List<AlertRepPanel> getAllPanel() {
		return alertRepPanelDao.selectAll();
	}
	@Override
	public AlertRepPanel selectByPrimaryKey(String id) {
		return alertRepPanelDao.selectByPrimaryKey(id);
	}
}
