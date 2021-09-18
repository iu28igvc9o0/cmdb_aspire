package com.aspire.ums.cmdb.v3.module.service;
import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleGroup;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleGroupRelation;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:21
*/
public interface ICmdbV3ModuleGroupService {

    /**
     * 获取模型分组表头
     * @param moduleId 模型ID
     * @return
     */
    List<Map<String, Object>> getGroupHeader(String moduleId, String tableHeaderCode);

    /**
     * 获取模型分组列表
     */
    List<CmdbV3ModuleGroup> getModuleGroupList(String moduleId);

    /**
     * 获取父分组下所有的分组信息
     * @param parentGroupId 父分组ID
     * @return 子模型分组列表
     */
    List<CmdbV3ModuleGroup> getChildModuleGroupList(String moduleId, String tableHeaderCode, String parentGroupId);

    /**
     * 获取分组下所有的码表配置信息
     * @param groupId 分组ID
     * @return 所有的分组码表关系
     */
    List<CmdbV3ModuleGroupRelation> getAllGroupCodeList(String groupId);


//     /**
//     * 获取所有实例
//     * @return 返回所有实例数据
//     */
//    List<CmdbV3ModuleGroup> list();
//
//    /**
//     * 根据主键ID 获取数据信息
//     * @param entity 实例信息
//     * @return 返回实例信息的数据信息
//     */
//    CmdbV3ModuleGroup get(CmdbV3ModuleGroup entity);
//
//    /**
//     * 新增实例
//     * @param entity 实例数据
//     * @return
//     */
//    void insert(CmdbV3ModuleGroup entity);
//
//    /**
//     * 修改实例
//     * @param entity 实例数据
//     * @return
//     */
//    void update(CmdbV3ModuleGroup entity);
//
//    /**
//     * 删除实例
//     * @param entity 实例数据
//     * @return
//     */
//    void delete(CmdbV3ModuleGroup entity);
}