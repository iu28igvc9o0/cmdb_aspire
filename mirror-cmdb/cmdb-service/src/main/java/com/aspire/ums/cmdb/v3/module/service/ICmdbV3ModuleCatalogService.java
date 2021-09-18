package com.aspire.ums.cmdb.v3.module.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
public interface ICmdbV3ModuleCatalogService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3ModuleCatalog> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3ModuleCatalog get(CmdbV3ModuleCatalog entity);

    /**
     * 根据主键ID 获取数据信息
     * @param catalogId ID
     * @return 返回实例信息的数据信息
     */
    CmdbV3ModuleCatalog getById(String catalogId);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    JSONObject insert(CmdbV3ModuleCatalog entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    JSONObject update(CmdbV3ModuleCatalog entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    JSONObject delete(CmdbV3ModuleCatalog entity);

    /**
     * 获取根模型分组列表
     * @return
     */
    List<CmdbV3ModuleCatalog> getFirstLevel();

    /**
     * 获取模型分组树
     * @param
     * @return
     */
    JSONObject getAllLevelTree(String catalogId);

    /**
     * 模型分组排序
     * @param catalogId
     * @param sortType
     */
    void updateSort(String catalogId, String sortType);

    /**
     * 验证模型分组编码、模型分组名称是否已经存在
     * @param parentCatalogId
     * @param catalogCode
     * @param catalogName
     * @return
     */
    JSONObject validCatalog(String parentCatalogId, String catalogCode, String catalogName);

    /**
     * 根据catalogId获取顶级catalogId
     * @param catalogId 模型分组ID
     * @return
     */
    CmdbV3ModuleCatalog getTopCatalog(String catalogId);

    /**
     * 根据catalogId获取顶级catalogId
     * @param moduleId 模型分组ID
     * @return
     */
    CmdbV3ModuleCatalog getByModuleId(String moduleId);
}