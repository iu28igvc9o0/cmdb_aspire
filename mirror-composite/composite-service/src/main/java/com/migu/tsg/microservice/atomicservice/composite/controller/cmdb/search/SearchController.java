package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.search;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.search.ISearchAPI;
import com.aspire.ums.cmdb.search.payload.ColumnFilter;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.search.SearchClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SearchController
 * Author:   zhu.juwang
 * Date:     2019/5/21 20:06
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class SearchController implements ISearchAPI {
    @Autowired
    private SearchClient searchClient;
	@Override
	public JSONObject getOrInsertColumnFilter(@PathVariable("menuType") String menuType,
											  @PathVariable("moduleId") String moduleId) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        return searchClient.getOrInsertColumnFilter(menuType, moduleId,authCtx.getUser().getUsername());
	}

    @Override
    public Map<String, Object> updateColumnFilter(@RequestBody ColumnFilter columnFilter) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        columnFilter.setLoginName(authCtx.getUser().getUsername());
        return searchClient.updateColumnFilter(columnFilter);
    }


}
