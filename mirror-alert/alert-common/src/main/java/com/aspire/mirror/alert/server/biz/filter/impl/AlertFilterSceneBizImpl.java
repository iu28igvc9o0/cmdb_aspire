package com.aspire.mirror.alert.server.biz.filter.impl;

import java.util.Arrays;
import java.util.List;

import com.aspire.mirror.alert.server.util.StringUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.alert.server.biz.filter.AlertFilterSceneBiz;
import com.aspire.mirror.alert.server.dao.filter.AlertFilterOptionDao;
import com.aspire.mirror.alert.server.dao.filter.AlertFilterSceneDao;
import com.aspire.mirror.alert.server.dao.filter.po.AlertFilterOption;
import com.aspire.mirror.alert.server.dao.filter.po.AlertFilterScene;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AlertFilterSceneBizImpl implements AlertFilterSceneBiz{
	Logger LOGGER = LoggerFactory.getLogger(AlertFilterSceneBizImpl.class);

    @Autowired
    private AlertFilterSceneDao alertFilterSceneDao; 
    @Autowired
    private AlertFilterOptionDao alertFilterOptionDao;
    
	@Override
	public void insert(AlertFilterScene filter) {
		alertFilterSceneDao.insert(filter);
	}

	@Override
	public AlertFilterScene selectByPrimaryKey(String id) {
		return alertFilterSceneDao.selectByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public PageResponse<AlertFilterScene> pageList(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
        int count = alertFilterSceneDao.pageListCount(page);
        PageResponse<AlertFilterScene> pageResponse = new PageResponse<AlertFilterScene>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<AlertFilterScene> listAlertFilter = alertFilterSceneDao.pageList(page);
            pageResponse.setResult(listAlertFilter);
        }
        return pageResponse;
	}


	@Override
	public void update(AlertFilterScene filter) {
		alertFilterSceneDao.update(filter);
		
	}

	@Override
	public void delete(String id) {
		alertFilterSceneDao.deleteByPrimaryKey(Integer.parseInt(id));
		
	}

	@Override
	public AlertFilterScene getByName(String name,String filterId) {
		return alertFilterSceneDao.getByName(name,filterId);
	}
	
	@Override
	public List<AlertFilterOption> getAllOptions() {
		return alertFilterOptionDao.selectAll();
	}

	public List<AlertFilterOption> getOptionsByQueryType(String queryType) {
		List<AlertFilterOption> selectList = alertFilterOptionDao.select(queryType);
		if (StringUtils.isEmpty(queryType)) {
			return selectList;
		}
		List<AlertFilterOption> list = Lists.newArrayList();
		for (AlertFilterOption alertFilterOption: selectList) {
			String queryType1 = alertFilterOption.getQueryType();
			if (StringUtils.isEmpty(queryType1)) {
				continue;
			}
			List<String> queryTypeList = Arrays.asList(queryType1.split(","));
			if (queryTypeList.contains(queryType)) {
				list.add(alertFilterOption);
			}
		}
		return list;
	}
}
