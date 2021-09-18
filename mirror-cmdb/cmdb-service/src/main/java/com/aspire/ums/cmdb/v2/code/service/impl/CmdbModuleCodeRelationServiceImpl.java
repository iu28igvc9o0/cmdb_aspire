//package com.aspire.ums.cmdb.v2.code.service.impl;
//
//import com.aspire.ums.cmdb.code.payload.CmdbCode;
//import com.aspire.ums.cmdb.code.payload.CmdbModuleCodeRelation;
//import com.aspire.ums.cmdb.v2.code.mapper.CmdbModuleCodeRelationMapper;
//import com.aspire.ums.cmdb.v2.code.service.ICmdbModuleCodeRelationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
//* 描述：
//* @author
//* @date 2019-05-13 18:39:39
//*/
//@Service
//public class CmdbModuleCodeRelationServiceImpl implements ICmdbModuleCodeRelationService {
//
//    @Autowired
//    private CmdbModuleCodeRelationMapper mapper;
//
//    /**
//     * 获取所有实例
//     * @return 返回所有实例数据
//     */
//    public List<CmdbModuleCodeRelation> list() {
//        return mapper.list();
//    }
//
//    /**
//     * 根据moduleid获取相关codeids
//     * @return 返回实例信息的数据信息
//     */
//    @Override
//    public List<CmdbCode> getCodeIdByModuleId(CmdbModuleCodeRelation entity) {
//        return mapper.getCodeByModuleId(entity.getModuleId());
//    }
//
//    /**
//     * 根据主键ID 获取数据信息
//     * @param entity 实例信息
//     * @return 返回指定ID的数据信息
//     */
//    public CmdbModuleCodeRelation get(CmdbModuleCodeRelation entity) {
//        return mapper.get(entity);
//    }
//
//    /**
//     * 新增实例
//     * @param entity 实例数据
//     * @return
//     */
//    public void insert(CmdbModuleCodeRelation entity) {
//        mapper.insert(entity);
//    }
//
//    /**
//     * 批量新增实例
//     * @param entities 实例数据
//     * @return
//     */
//    @Override
//    public void insertByBatch(List<CmdbModuleCodeRelation> entities) {
//        mapper.insertByBatch(entities);
//
//    }
//
//    /**
//     * 修改实例
//     * @param entity 实例数据
//     * @return
//     */
//    public void update(CmdbModuleCodeRelation entity) {
//        mapper.update(entity);
//    }
//
//    /**
//     * 删除实例
//     * @param entity 实例数据
//     * @return
//     */
//    public void delete(CmdbModuleCodeRelation entity) {
//        mapper.delete(entity);
//    }
//
//    @Override
//    public List<CmdbModuleCodeRelation> getCodeListByModuleIdAndGroupId(String moduleId, String groupId) {
//        CmdbModuleCodeRelation relation = new CmdbModuleCodeRelation();
//        relation.setModuleId(moduleId);
//        relation.setGroupId(groupId);
//        return mapper.listByEntity(relation);
//    }
//}