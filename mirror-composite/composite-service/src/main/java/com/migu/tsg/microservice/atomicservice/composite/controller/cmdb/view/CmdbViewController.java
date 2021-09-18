package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.view;

import com.aspire.mirror.composite.service.cmdb.view.ICmdbViewAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.view.payload.CmdbView;
import com.aspire.ums.cmdb.view.payload.CmdbViewData;
import com.aspire.ums.cmdb.view.payload.CmdbViewQuery;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.view.ICmdbViewClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewController
 * Author:   hangfang
 * Date:     2020/5/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbViewController implements ICmdbViewAPI {

    @Autowired
    private ICmdbViewClient viewClient;
    @Override
    public Result<CmdbView> listByQuery(@RequestBody CmdbViewQuery query) {
        return viewClient.listByQuery(query);
    }

    @Override
    public CmdbViewData getDataStructure(@RequestParam("id")String id) {
        return viewClient.getDataStructure(id);
    }

    @Override
    public CmdbViewData getNextNodeData(@RequestBody CmdbViewData viewData) {
        return viewClient.getNextNodeData(viewData);
    }

    @Override
    public CmdbView getStructure(@RequestParam("id")String id) {
        return viewClient.getStructure(id);
    }

    @Override
    public Map<String, Object> save(@RequestBody CmdbView cmdbView) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String userName = authCtx.getUser().getUsername();
        return viewClient.save(userName, cmdbView);
    }

    @Override
    public Map<String, Object> deleteView(@RequestParam("id") String id) {
        return viewClient.deleteView(id);
    }

    @Override
    public Map<String, Object> deleteNode(@RequestParam("id")String id) {
        return viewClient.deleteNode(id);
    }
}
