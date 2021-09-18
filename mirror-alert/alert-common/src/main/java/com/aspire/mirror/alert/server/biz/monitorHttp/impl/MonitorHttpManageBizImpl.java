package com.aspire.mirror.alert.server.biz.monitorHttp.impl;

import java.util.List;

import com.aspire.mirror.alert.server.biz.monitorHttp.MonitorHttpManageBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.alert.server.dao.monitorHttp.MonitorHttpConfigDao;
import com.aspire.mirror.alert.server.dao.monitorHttp.MonitorHttpHisDao;
import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpConfig;
import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpHis;
import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpReq;
import com.aspire.mirror.common.entity.PageResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class MonitorHttpManageBizImpl implements MonitorHttpManageBiz {
	Logger LOGGER = LoggerFactory.getLogger(MonitorHttpManageBizImpl.class);

    @Autowired
    private MonitorHttpConfigDao monitorHttpConfigDao; 
    @Autowired
    private MonitorHttpHisDao monitorHttpHisDao;
    
	

	@Override
	public PageResponse<MonitorHttpConfig> pageList(MonitorHttpReq pageRequset) {
        int count = monitorHttpConfigDao.pageListCount(pageRequset);
        PageResponse<MonitorHttpConfig> pageResponse = new PageResponse<MonitorHttpConfig>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<MonitorHttpConfig> res = monitorHttpConfigDao.pageList(pageRequset);
            pageResponse.setResult(res);
        }
        return pageResponse;
	}

	@Override
	public void delete(String id) {
		monitorHttpConfigDao.deleteByPrimaryKey(Integer.parseInt(id));
		
	}

	@Override
	public MonitorHttpConfig selectByPrimaryKey(String id) {
		return monitorHttpConfigDao.selectByPrimaryKey(id);
	}

	@Override
	public MonitorHttpConfig insert(MonitorHttpConfig res) {
		monitorHttpConfigDao.insert(res);
		return res;
	}

	@Override
	public void update(MonitorHttpConfig res) {
		monitorHttpConfigDao.update(res);
		
	}
	
	@Override
	public void updateStatus(MonitorHttpConfig res) {
		monitorHttpConfigDao.updateStatus(res);
		
	}

	@Override
	public MonitorHttpConfig getByName(String name) {
		return monitorHttpConfigDao.getByName(name);
	}

	@Override
	public PageResponse<MonitorHttpHis> pageListHis(MonitorHttpReq pageRequset) {
		int count = monitorHttpHisDao.pageListCount(pageRequset);
        PageResponse<MonitorHttpHis> pageResponse = new PageResponse<MonitorHttpHis>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<MonitorHttpHis> res = monitorHttpHisDao.pageList(pageRequset);
            pageResponse.setResult(res);
        }
        return pageResponse;
	}

	@Override
	public void batchInsertHis(List<MonitorHttpHis> listAlert) {
		monitorHttpHisDao.batchInsert(listAlert);
		
	}

	@Override
	public MonitorHttpHis selectHisByPrimaryKey(String id) {
		return monitorHttpHisDao.selectByPrimaryKey(Integer.parseInt(id));
	}

}
