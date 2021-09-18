package com.aspire.ums.cmdb.v2.project.service.impl;
import com.aspire.ums.cmdb.v2.project.service.ICmdbProjectManagerService;
import com.aspire.ums.cmdb.v2.project.entity.CmdbProjectManager;
import com.aspire.ums.cmdb.v2.project.mapper.CmdbProjectManagerMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-17 17:30:43
*/
@Service
public class CmdbProjectManagerServiceImpl implements ICmdbProjectManagerService {

    @Autowired
    private CmdbProjectManagerMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbProjectManager> list() {
        return mapper.list();
    }

    @Override
    public List<CmdbProjectManager> listByEntity(CmdbProjectManager entity) {
        return mapper.listByEntity(entity);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbProjectManager get(CmdbProjectManager entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbProjectManager entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbProjectManager entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbProjectManager entity) {
        mapper.delete(entity);
    }
}