package com.aspire.ums.cmdb.v3.module.service.impl;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleAuthorizationService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleAuthorization;
import com.aspire.ums.cmdb.v3.module.mapper.CmdbV3ModuleAuthorizationMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
@Service
public class CmdbV3ModuleAuthorizationServiceImpl implements ICmdbV3ModuleAuthorizationService {

    @Autowired
    private CmdbV3ModuleAuthorizationMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3ModuleAuthorization> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3ModuleAuthorization get(CmdbV3ModuleAuthorization entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbV3ModuleAuthorization entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbV3ModuleAuthorization entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbV3ModuleAuthorization entity) {
        mapper.delete(entity);
    }
}