//package com.aspire.ums.cmdb.v2.code.service;
//
//import com.aspire.ums.cmdb.code.payload.CmdbCode;
//import com.aspire.ums.cmdb.code.payload.CmdbModuleCodeRelation;
//
//import java.util.List;
//
///**
//* 描述：
//* @author
//* @date 2019-05-13 18:39:39
//*/
//public interface ICmdbModuleCodeRelationService {
//     /**
//     * 获取所有实例
//     * @return 返回所有实例数据
//     */
//    List<CmdbModuleCodeRelation> list();
//
//    /**
//     * 根据实例获取相关codeid
//     * @return 返回实例信息的数据信息
//     */
//    List<CmdbCode> getCodeIdByModuleId(CmdbModuleCodeRelation entity);
//
//    /**
//     * 根据主键ID 获取数据信息
//     * @param entity 实例信息
//     * @return 返回实例信息的数据信息
//     */
//    CmdbModuleCodeRelation get(CmdbModuleCodeRelation entity);
//
//    /**
//     * 新增实例
//     * @param entity 实例数据
//     * @return
//     */
//    void insert(CmdbModuleCodeRelation entity);
//
//    /**
//     * 批量新增实例
//     * @param entities 实例数据
//     * @return
//     */
//    void insertByBatch(List<CmdbModuleCodeRelation> entities);
//
//    /**
//     * 修改实例
//     * @param entity 实例数据
//     * @return
//     */
//    void update(CmdbModuleCodeRelation entity);
//
//    /**
//     * 删除实例
//     * @param entity 实例数据
//     * @return
//     */
//    void delete(CmdbModuleCodeRelation entity);
//
//    /**
//     * 根据模型ID、分组ID查询分组下的所有码表字段信息
//     * @param moduleId 模型ID
//     * @param groupId 分组ID
//     * @return
//     */
//    List<CmdbModuleCodeRelation> getCodeListByModuleIdAndGroupId(String moduleId, String groupId);
//}