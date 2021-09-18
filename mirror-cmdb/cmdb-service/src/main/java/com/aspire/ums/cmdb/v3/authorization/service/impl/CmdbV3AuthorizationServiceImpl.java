package com.aspire.ums.cmdb.v3.authorization.service.impl;
import com.aspire.ums.cmdb.v3.authorization.service.ICmdbV3AuthorizationService;
import com.aspire.ums.cmdb.v3.authorization.payload.CmdbV3Authorization;
import com.aspire.ums.cmdb.v3.authorization.mapper.CmdbV3AuthorizationMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:17
*/
@Service
public class CmdbV3AuthorizationServiceImpl implements ICmdbV3AuthorizationService {

    @Autowired
    private CmdbV3AuthorizationMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3Authorization> list(String authOwner) {
        CmdbV3Authorization auth = new CmdbV3Authorization();
        auth.setAuthOwner(authOwner);
        return mapper.listByEntity(auth);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3Authorization get(CmdbV3Authorization entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbV3Authorization entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbV3Authorization entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbV3Authorization entity) {
        mapper.delete(entity);
    }
}