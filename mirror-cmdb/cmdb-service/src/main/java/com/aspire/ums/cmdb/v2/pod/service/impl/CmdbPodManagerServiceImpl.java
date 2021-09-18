package com.aspire.ums.cmdb.v2.pod.service.impl;
import com.aspire.ums.cmdb.v2.pod.service.ICmdbPodManagerService;
import com.aspire.ums.cmdb.v2.pod.entity.CmdbPodManager;
import com.aspire.ums.cmdb.v2.pod.mapper.CmdbPodManagerMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-17 17:29:54
*/
@Service
public class CmdbPodManagerServiceImpl implements ICmdbPodManagerService {

    @Autowired
    private CmdbPodManagerMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbPodManager> list() {
        return mapper.list();
    }

    @Override
    public List<CmdbPodManager> listByEntity(CmdbPodManager entity) {
        return mapper.listByEntity(entity);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbPodManager get(CmdbPodManager entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbPodManager entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbPodManager entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbPodManager entity) {
        mapper.delete(entity);
    }
}