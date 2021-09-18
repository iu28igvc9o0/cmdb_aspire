package com.aspire.ums.cmdb.v3.code.service.impl;
import com.aspire.ums.cmdb.v3.code.service.ICmdbV3CodeCollectService;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeCollect;
import com.aspire.ums.cmdb.v3.code.mapper.CmdbV3CodeCollectMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:19
*/
@Service
public class CmdbV3CodeCollectServiceImpl implements ICmdbV3CodeCollectService {

    @Autowired
    private CmdbV3CodeCollectMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3CodeCollect> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3CodeCollect get(CmdbV3CodeCollect entity) {
        return mapper.get(entity);
    }

    @Override
    public CmdbV3CodeCollect getByCodeId(String codeId) {
        return mapper.getByCodeId(codeId);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbV3CodeCollect entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbV3CodeCollect entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbV3CodeCollect entity) {
        mapper.delete(entity);
    }

    @Override
    public void deleteByCodeId(String codeId) {
        mapper.deleteByCodeId(codeId);
    }
}