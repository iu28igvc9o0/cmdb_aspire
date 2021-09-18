package com.aspire.ums.cmdb.resource.service.impl;

import com.aspire.ums.cmdb.resource.mapper.AssetsMapper;
import com.aspire.ums.cmdb.resource.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.resource.mapper.FinancialBusinessMapper;
import com.aspire.ums.cmdb.resource.service.ComponentDataService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 定义组件数据加载服务类
 * Title: osa_web
 * Description:
 * Copyright: aspire Copyright (C) 2009
 * 
 * @author <a href="mailto:qinjianqiu@aspirecn.com">覃剑秋</a>
 * @e-mail: qinjianqiu@aspirecn.com
 * @version 1.0 
 * @creatdate 2015年7月13日 上午11:03:07
 *
 */
@Service
public class ComponentDataServiceImpl implements ComponentDataService {

	@Autowired
	private ConfigDictMapper configDictMapper;

	@Autowired
	private AssetsMapper assetsMapper;

	private FinancialBusinessMapper financialBusinessMapper;

	private static final String totalKey = "total";
	private static final String suffix = "_count";

	@Override
	public List<Map> queryDatas(Map<String, Object> params) {
		if (params.containsKey("depart_id")) {
			return financialBusinessMapper.findCmdbBusiness1Info(params);
		}
		return configDictMapper.getDictConfig(params);
	}

	@Override
	public Object[] queryDataGrid(Map<String, Object> params,Integer startIndex, Integer limit) {
		Long total = 0L ;
		List<Map<String, Object>> list = null;
		Object[] objs = null;
		Object object = assetsMapper.advancedSearchDeviceDistributionList_count(params);
		if(object!=null){
			total =   ((HashMap<String,Long>)object).get(totalKey);
			if(total==null){
				total =   ((HashMap<String,Long>)object).get(totalKey.toUpperCase());
			}
		}
		list = assetsMapper.advancedSearchDeviceDistributionList(params,new RowBounds(startIndex, limit));
		objs = new Object[]{total,list};
		return objs;
	}

}
