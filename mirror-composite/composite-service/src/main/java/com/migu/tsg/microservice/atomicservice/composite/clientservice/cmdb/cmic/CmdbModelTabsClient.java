package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.cmic;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsBase;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsReq;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsResp;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/13 10:01
 */
@FeignClient(value = "CMDB")
public interface CmdbModelTabsClient {

    /**
     * 获取模型tab标签列表
     *
     * @param cmdbModelTabsReq
     * @return
     */
    @RequestMapping(value = "/cmdb/modelTabs/getModelTabsList", method = RequestMethod.POST)
    Result<CmdbModelTabsResp> getModelTabsList(@RequestBody CmdbModelTabsReq cmdbModelTabsReq);

    /**
     * 根据ID获取模型tab标签
     *
     * @param tabsId
     * @return
     */
    @RequestMapping(value = "/cmdb/modelTabs/getModelTabsById", method = RequestMethod.GET)
    ResultVo<CmdbModelTabsResp> getModelTabsById(@RequestParam(value = "tabsId") String tabsId);

    /**
     * 更新模型tab标签
     *
     * @param cmdbModelTabsBase
     * @return
     */
    @RequestMapping(value = "/cmdb/modelTabs/saveModelTabs", method = RequestMethod.POST)
    ResultVo saveModelTabs(@RequestBody CmdbModelTabsBase cmdbModelTabsBase);

    /**
     * 根据tabsId删除模型tab标签
     *
     * @param idList
     * @return
     */
    @RequestMapping(value = "/cmdb/modelTabs/deleteModelTabsById", method = RequestMethod.GET)
    ResultVo deleteModelTabsById(@RequestParam(value = "idList") String[] idList);

}
