package com.aspire.ums.cmdb.v2.code.service;

import com.aspire.ums.cmdb.code.payload.CmdbModuleCodeGroup;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:39
*/
public interface ICmdbModuleCodeGroupService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbModuleCodeGroup> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbModuleCodeGroup get(CmdbModuleCodeGroup entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbModuleCodeGroup entity);

    /**
     * 批量新增实例
     * @param groups 实例数据
     * @return
     */
    void insertByBatch(List<CmdbModuleCodeGroup> groups);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbModuleCodeGroup entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbModuleCodeGroup entity);

    /**
     * 根据模型ID 查询模型下的所有分组
     * @param id 模型ID
     * @return
     */
    List<CmdbModuleCodeGroup> getGroupListByModuleId(String id);
}