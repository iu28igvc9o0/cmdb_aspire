package com.aspire.ums.cmdb.cmic.mapper;

import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsBase;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsReq;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/12 11:44
 */
public interface CmdbModelTabsMapper {

    /**
     * 新增一条模型tabs数据
     *
     * @param entity
     */
    void insertTabs(CmdbModelTabsBase entity);

    /**
     * 批量插入数据
     *
     * @param entityList
     */
    void insertTabsBatch(List<CmdbModelTabsBase> entityList);

    /**
     * 根据id修改模型tabs数据
     *
     * @param entity
     * @return
     */
    Integer updateTabsById(CmdbModelTabsBase entity);

    /**
     * 查询tabs数据
     *
     * @param query
     * @return
     */
    List<CmdbModelTabsResp> selectTabs(CmdbModelTabsReq query);

    /**
     * 返回数据总数
     *
     * @param query
     * @return
     */
    Integer selectTabsCount(CmdbModelTabsReq query);

    /**
     * 根据tabsId批量删除
     *
     * @param tabsIdList
     * @return
     */
    Integer deleteByIdBatch(@Param(value = "tabsIdList") List<String> tabsIdList);

    /**
     * 根据modelId和tabName统计数据总数
     *
     * @param modelId
     * @param tabName
     * @return
     */
    Integer isExistTabs(@Param(value = "modelId") String modelId,
                        @Param(value = "tabName") String tabName);

    /**
     *
     * @param modelId
     * @param tabName
     * @return
     */
    String returnModelName(@Param(value = "modelId") String modelId,
                           @Param(value = "tabName") String tabName);

}
