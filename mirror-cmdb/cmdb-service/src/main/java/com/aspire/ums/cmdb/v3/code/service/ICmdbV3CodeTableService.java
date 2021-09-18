package com.aspire.ums.cmdb.v3.code.service;

import com.aspire.ums.cmdb.code.payload.CmdbV3CodeTable;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-02-20 17:49:53
*/
public interface ICmdbV3CodeTableService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeTable> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeTable get(CmdbV3CodeTable entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CodeTable entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CodeTable entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3CodeTable entity);

    /**
     * 根据码表ID 删除
     * @param codeId 码表ID
     */
    void deleteByCodeId(String codeId);

    /**
     * 批量新增
     * @param tableList
     */
    void insertByBatch(List<CmdbV3CodeTable> tableList);
}