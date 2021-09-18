package com.aspire.ums.cmdb.sync.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.sync.entity.CmdbSyncFieldMapping;
import com.aspire.ums.cmdb.sync.mapper.CmdbSyncFieldMappingMapper;
import com.aspire.ums.cmdb.sync.service.ICmdbSyncFieldMappingService;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 描述：
 * 
 * @author
 * @date 2020-05-19 19:25:57
 */
@Service("cmdbSyncFieldMappingService")
public class CmdbSyncFieldMappingServiceImpl implements ICmdbSyncFieldMappingService {

    @Autowired
    private CmdbSyncFieldMappingMapper mapper;

    @Autowired
    private IRedisService redisService;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbSyncFieldMapping> list() {
        return mapper.list();
    }

    @Override
    public List<CmdbSyncFieldMapping> list(CmdbSyncFieldMapping entity) {
        return mapper.listByEntity(entity);
    }

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回指定ID的数据信息
     */
    @Override
    public CmdbSyncFieldMapping get(CmdbSyncFieldMapping entity) {
        return mapper.get(entity);
    }

    @Override
    public CmdbSyncFieldMapping getOne(CmdbSyncFieldMapping entity) {
        return mapper.getOne(entity);
    }

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    @Transactional
    public void insert(CmdbSyncFieldMapping entity) {
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
    @Transactional
    public void update(CmdbSyncFieldMapping entity) {
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
    @Transactional
    public void delete(CmdbSyncFieldMapping entity) {
        mapper.delete(entity);
    }

    @Override
    public void redisRefresh(String mappingType, String mappingKey) {
        String key = mappingType + ":" + mappingKey;
        // 清理原先缓存
        redisService.syncRemove(key);
        CmdbSyncFieldMapping query = new CmdbSyncFieldMapping();
        query.setMappingType(mappingType);
        query.setMappingKey(mappingKey);
        List<CmdbSyncFieldMapping> resultList = list(query);
        redisService.set(key, resultList);
    }

    @Override
    public void redisSet(String mappingType, String mappingKey) {
        String key = mappingType + ":" + mappingKey;
        CmdbSyncFieldMapping query = new CmdbSyncFieldMapping();
        query.setMappingType(mappingType);
        query.setMappingKey(mappingKey);
        List<CmdbSyncFieldMapping> resultList = list(query);
        redisService.set(key, resultList);
    }

    @Override
    public List<CmdbSyncFieldMapping> redisGet(String mappingType, String mappingKey) {
        String key = mappingType + ":" + mappingKey;
        Object object = redisService.get(key);
        if (object == null) {
            redisSet(mappingType, mappingKey);
            object = redisService.get(key);
        }
        return JsonMapper.getInstance().convertValue(object, new TypeReference<List<CmdbSyncFieldMapping>>() {});
    }
}
