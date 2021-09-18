package com.aspire.ums.cmdb.cmic.service;

import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsBase;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsReq;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsResp;

import java.util.List;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/12 11:44
 */
public interface CmdbModelTabsService {

    /**
     * 新增一条模型tabs数据
     *
     * @param entity
     */
    String insertTabs(CmdbModelTabsBase entity);


    /**
     * 根据id修改模型tabs数据
     *
     * @param entity
     * @return
     */
    String updateTabsById(CmdbModelTabsBase entity);

    /**
     * 查询tabs数据
     *
     * @param query
     * @return
     */
    List<CmdbModelTabsResp> findPage(CmdbModelTabsReq query);

    /**
     * 返回数据总数
     *
     * @param query
     * @return
     */
    Integer findPageCount(CmdbModelTabsReq query);

    /**
     * 根据id查询详情
     *
     * @param tabsId
     * @return
     */
    CmdbModelTabsResp findOneDataById(String tabsId);

    /**
     * 根据tabsId批量删除 tabs标签
     *
     * @param tabsIdList
     * @return
     */
    Boolean deleteByIdBatch(List<String> tabsIdList);

    /**
     * 根据modelId和tabName判断数据是否存在
     *
     * @param modelId 模型ID
     * @param tabName tab名
     * @return 存在-true,不存在-false
     */
    Boolean isExistTabs(String modelId, String tabName);

    /**
     * 根据modelId和tabName查存在的模型名称
     *
     * @param modelId 模型ID
     * @param tabName tab名
     * @return
     */
    String returnModelName(String modelId, String tabName);

}
