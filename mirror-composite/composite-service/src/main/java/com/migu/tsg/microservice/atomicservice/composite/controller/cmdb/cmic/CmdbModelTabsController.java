package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.cmic;

import com.aspire.mirror.composite.service.cmdb.cmic.IModelTabsAPI;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsBase;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsReq;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsResp;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.cmic.CmdbModelTabsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/13 9:59
 */
@RestController
@Slf4j
public class CmdbModelTabsController implements IModelTabsAPI {

    @Autowired
    private CmdbModelTabsClient cmdbModelTabsClient;

    @Override
    public Result<CmdbModelTabsResp> getModelTabsList(@RequestBody CmdbModelTabsReq cmdbModelTabsReq) {
        return cmdbModelTabsClient.getModelTabsList(cmdbModelTabsReq);
    }

    @Override
    public ResultVo<CmdbModelTabsResp> getModelTabsById(@RequestParam(value = "tabsId") String tabsId) {
        return cmdbModelTabsClient.getModelTabsById(tabsId);
    }

    @Override
    public ResultVo saveModelTabs(@RequestBody CmdbModelTabsBase cmdbModelTabsBase) {
        return cmdbModelTabsClient.saveModelTabs(cmdbModelTabsBase);
    }

    @Override
    public ResultVo deleteModelTabsById(@RequestParam(value = "idList") String[] idList) {
        return cmdbModelTabsClient.deleteModelTabsById(idList);
    }
}
