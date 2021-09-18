package com.aspire.ums.cmdb.demand.service.impl;
import com.aspire.ums.cmdb.demand.service.ICmdbDemandResourceTypeService;
import com.aspire.ums.cmdb.demand.entity.CmdbDemandResourceType;
import com.aspire.ums.cmdb.demand.mapper.CmdbDemandResourceTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2019-05-09 16:28:20
*/
@Service
public class CmdbDemandResourceTypeServiceImpl implements ICmdbDemandResourceTypeService {

    @Autowired
    private CmdbDemandResourceTypeMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbDemandResourceType> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbDemandResourceType get(CmdbDemandResourceType entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbDemandResourceType entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbDemandResourceType entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbDemandResourceType entity) {
        mapper.delete(entity);
    }
}