package com.aspire.ums.cmdb.v3.code.service;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbV3CodeCascade;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-02-20 17:49:52
*/
public interface ICmdbV3CodeCascadeService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeCascade> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeCascade get(CmdbV3CodeCascade entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CodeCascade entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CodeCascade entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3CodeCascade entity);

    /**
     * 根据码表ID删除
     * @param codeId 码表ID
     */
    void deleteByCodeId(String codeId);

    /**
     * 批量新增
     * @param cascadeList
     */
    void insertByBatch(List<CmdbV3CodeCascade> cascadeList);

    /**
     * 查询级联的子码表信息
     * @param codeId 码表ID
     * @return
     */
    List<CmdbV3CodeCascade> getByCodeId(String codeId);

    /**
     * 查询子码表的父级联码表信息
     * @param codeId 子码表信息
     * @return
     */
    List<CmdbV3CodeCascade> getByChildCodeId(String codeId);
}