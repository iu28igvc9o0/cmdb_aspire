package com.aspire.ums.cmdb.v3.condication.service.impl;

import com.aspire.ums.cmdb.v3.condication.mapper.CmdbV3CondicationSortRelationMapper;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSortRelation;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3CondicationSortRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-03-16 09:19:57
*/
@Service
public class CmdbV3CondicationSortRelationServiceImpl implements ICmdbV3CondicationSortRelationService {

    @Autowired
    private CmdbV3CondicationSortRelationMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3CondicationSortRelation> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3CondicationSortRelation get(CmdbV3CondicationSortRelation entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbV3CondicationSortRelation entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbV3CondicationSortRelation entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbV3CondicationSortRelation entity) {
        mapper.delete(entity);
    }

    @Override
    public List<CmdbV3CondicationSortRelation> getSortRelationByCondicationId(String condicationSettingId) {
        return mapper.getSortRelationByCondicationId(condicationSettingId);
    }
}