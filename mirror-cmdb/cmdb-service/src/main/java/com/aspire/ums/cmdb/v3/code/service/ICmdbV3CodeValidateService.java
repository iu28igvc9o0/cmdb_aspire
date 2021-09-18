package com.aspire.ums.cmdb.v3.code.service;

import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:19
*/
public interface ICmdbV3CodeValidateService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeValidate> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeValidate get(CmdbV3CodeValidate entity);

    /**
     * 获取所有实例
     * @param codeId 码表ID
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeValidate> getByCodeId(String codeId);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CodeValidate entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CodeValidate entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3CodeValidate entity);

    /**
     * 根据码表ID 删除验证信息
     * @param codeId 码表ID
     */
    void deleteByCodeId(String codeId);

    /**
     * 批量新增验证信息
     * @param validates 验证信息集合
     */
    void insertByBatch(List<CmdbV3CodeValidate> validates);
}