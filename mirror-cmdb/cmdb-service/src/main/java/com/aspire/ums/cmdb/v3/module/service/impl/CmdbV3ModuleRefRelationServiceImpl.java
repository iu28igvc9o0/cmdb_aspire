package com.aspire.ums.cmdb.v3.module.service.impl;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleRefRelationService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleRefRelation;
import com.aspire.ums.cmdb.v3.module.mapper.CmdbV3ModuleRefRelationMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:21
*/
@Service
public class CmdbV3ModuleRefRelationServiceImpl implements ICmdbV3ModuleRefRelationService {

    @Autowired
    private CmdbV3ModuleRefRelationMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3ModuleRefRelation> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3ModuleRefRelation get(CmdbV3ModuleRefRelation entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbV3ModuleRefRelation entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbV3ModuleRefRelation entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbV3ModuleRefRelation entity) {
        mapper.delete(entity);
    }
}