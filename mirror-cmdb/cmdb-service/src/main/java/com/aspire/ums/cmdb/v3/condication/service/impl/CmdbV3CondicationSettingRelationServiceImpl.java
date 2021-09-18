package com.aspire.ums.cmdb.v3.condication.service.impl;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3CondicationSettingRelationService;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingRelation;
import com.aspire.ums.cmdb.v3.condication.mapper.CmdbV3CondicationSettingRelationMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
@Service
public class CmdbV3CondicationSettingRelationServiceImpl implements ICmdbV3CondicationSettingRelationService {

    @Autowired
    private CmdbV3CondicationSettingRelationMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3CondicationSettingRelation> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3CondicationSettingRelation get(CmdbV3CondicationSettingRelation entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbV3CondicationSettingRelation entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbV3CondicationSettingRelation entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param relationId 关系ID
     * @return
     */
    public void deleteById(String relationId) {
        mapper.deleteById(relationId);
    }

    /**
     * 删除实例
     * @param condicationSettingId 查询条件ID
     * @return
     */
    public void deleteByCondicationSettingId(String condicationSettingId) {
        mapper.deleteByCondicationSettingId(condicationSettingId);
    }
}