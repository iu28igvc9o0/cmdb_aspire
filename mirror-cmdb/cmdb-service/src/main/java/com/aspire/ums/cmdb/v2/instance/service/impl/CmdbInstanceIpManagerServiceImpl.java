package com.aspire.ums.cmdb.v2.instance.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.instance.payload.CmdbInstanceIpManager;
import com.aspire.ums.cmdb.v2.instance.mapper.CmdbInstanceIpManagerMapper;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceIpManagerService;

/**
 * 描述：
 * 
 * @author
 * @date 2019-06-04 09:51:05
 */
@Service
public class CmdbInstanceIpManagerServiceImpl implements ICmdbInstanceIpManagerService {

    @Autowired
    private CmdbInstanceIpManagerMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbInstanceIpManager> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回指定ID的数据信息
     */
    @Override
    public CmdbInstanceIpManager get(CmdbInstanceIpManager entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(CmdbInstanceIpManager entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CmdbInstanceIpManager entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CmdbInstanceIpManager entity) {
        mapper.delete(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertByBatch(String instanceId, List<CmdbInstanceIpManager> ipManagers) {
        // 先删除之前的数据
        if (ipManagers != null && ipManagers.size() > 0) {
            ipManagers.forEach((ipManager) -> {
                CmdbInstanceIpManager deleteIp = new CmdbInstanceIpManager();
                deleteIp.setInstanceId(instanceId);
                deleteIp.setCodeId(ipManager.getCodeId());
                delete(deleteIp);
            });
        }
        mapper.insertByBatch(ipManagers);
    }

    @Override
    public List<Map<String, Object>> getAllIpManagerList(String instanceId) {
        return mapper.getAllIpManagerList(instanceId);
    }
}
