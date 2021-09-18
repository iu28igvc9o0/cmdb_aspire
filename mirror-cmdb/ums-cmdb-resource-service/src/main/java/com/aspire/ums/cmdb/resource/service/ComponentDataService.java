package com.aspire.ums.cmdb.resource.service;

import java.util.List;
import java.util.Map;

/**
 * 定义组件数据加载service接口
 * Description:
 * Copyright: aspire Copyright (C) 2009
 * 
 * @author hangfang
 * @version 1.0 
 * @creatdate 2019年1月17日 上午11:02:38
 *
 */
public interface ComponentDataService {
	

 	List<Map> queryDatas(Map<String, Object> params);

 	Object[] queryDataGrid(Map<String, Object> params,Integer startIndex,Integer limit);




}
