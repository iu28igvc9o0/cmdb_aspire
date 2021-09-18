package com.aspire.ums.cmdb.v3.condication.service.impl;

import com.aspire.ums.cmdb.v3.condication.mapper.CmdbV3CondicationReturnRelationMapper;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationReturnRelation;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3CondicationReturnRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-03-11 15:11:41
*/
@Service
public class CmdbV3CondicationReturnRelationServiceImpl implements ICmdbV3CondicationReturnRelationService {

    @Autowired
    private CmdbV3CondicationReturnRelationMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3CondicationReturnRelation> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3CondicationReturnRelation get(CmdbV3CondicationReturnRelation entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbV3CondicationReturnRelation entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbV3CondicationReturnRelation entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbV3CondicationReturnRelation entity) {
        mapper.delete(entity);
    }
}